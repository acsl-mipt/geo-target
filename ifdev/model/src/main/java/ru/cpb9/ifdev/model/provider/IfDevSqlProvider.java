package ru.cpb9.ifdev.model.provider;

import com.google.common.base.Preconditions;
import org.jetbrains.annotations.NotNull;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.impl.*;
import ru.cpb9.ifdev.model.domain.message.IfDevMessage;
import ru.cpb9.ifdev.model.domain.message.IfDevMessageParameter;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.IfDevEnumConstant;
import ru.cpb9.ifdev.model.domain.type.IfDevStructField;
import ru.cpb9.ifdev.model.domain.type.IfDevType;
import ru.cpb9.ifdev.model.exporter.TableName;
import ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevMaybeProxy;
import ru.cpb9.ifdev.model.exporter.ModelExportingException;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Artem Shein
 */
public class IfDevSqlProvider
{
    @NotNull
    private Connection connection;
    @NotNull
    private final Map<Long, IfDevType> typeById = new HashMap<>();
    @NotNull
    private final Map<Long, IfDevNamespace> namespaceById = new HashMap<>();
    @NotNull
    private final Map<Long, IfDevUnit> unitById = new HashMap<>();
    @NotNull
    private final Map<Long, IfDevComponent> componentById = new HashMap<>();

    public IfDevRegistry provide(@NotNull IfDevSqlProviderConfiguration config)
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
        }
        catch (ClassNotFoundException e)
        {
            throw new ModelExportingException(e);
        }
        try(Connection connection = DriverManager
                .getConnection(config.getConnectionUrl()))
        {
            this.connection = connection;
            IfDevRegistry registry = SimpleIfDevRegistry.newInstance();
            readNamespaces(registry);
            return registry;
        }
        catch (SQLException e)
        {
            throw new ModelImportingException(e);
        }
    }

    private void readNamespaces(@NotNull IfDevRegistry registry) throws SQLException
    {
        try (ResultSet namespacesSelectRs = connection.prepareStatement(String.format("SELECT id, name FROM %s",
                TableName.NAMESPACE)).executeQuery())
        {
            while (namespacesSelectRs.next())
            {
                namespaceById.put(namespacesSelectRs.getLong("id"), SimpleIfDevNamespace.newInstance(
                        ImmutableIfDevName.newInstanceFromMangledName(namespacesSelectRs.getString("name")),
                        Optional.<IfDevNamespace>empty()));
            }
        }
        try (ResultSet subNamespacesRs = connection.prepareStatement(String.format(
                "SELECT namespace_id, sub_namespace_id FROM %s", TableName.SUB_NAMESPACE)).executeQuery())
        {
            while (subNamespacesRs.next())
            {
                IfDevNamespace parent = namespaceById.get(subNamespacesRs.getLong("namespace_id")),
                        child = namespaceById.get(subNamespacesRs.getLong("sub_namespace_id"));
                child.setParent(parent);
                parent.getSubNamespaces().add(child);
            }
            List<IfDevNamespace> rootNamespaces = registry.getRootNamespaces();
            rootNamespaces.addAll(namespaceById.values().stream().filter(n -> !n.getParent().isPresent())
                    .collect(Collectors.toList()));
        }

        try (ResultSet selectUnitsRs = connection.prepareStatement(
                String.format("SELECT id, namespace_id, name, display, info FROM %s", TableName.UNIT)).executeQuery())
        {
            while (selectUnitsRs.next())
            {
                IfDevNamespace namespace = namespaceById.get(selectUnitsRs.getLong("namespace_id"));
                IfDevUnit unit = ImmutableIfDevUnit.newInstance(
                        ImmutableIfDevName.newInstanceFromMangledName(selectUnitsRs.getString("name")),
                        namespace, selectUnitsRs.getString("display"), selectUnitsRs.getString("info"));
                unitById.put(selectUnitsRs.getLong("id"), unit);
                namespace.getUnits().add(unit);
            }
        }

        try (ResultSet selectTypesRs = connection.prepareStatement(String.format("SELECT id FROM %s", TableName.TYPE))
                .executeQuery())
        {
            while (selectTypesRs.next())
            {
                ensureTypeLoaded(selectTypesRs.getLong("id"));
            }
        }

        try (ResultSet selectComponentsRs = connection.prepareStatement(String.format("SELECT id FROM %s",
                TableName.COMPONENT)).executeQuery())
        {
            while (selectComponentsRs.next())
            {
                ensureComponentLoaded(selectComponentsRs.getLong("id"));
            }
        }
    }

    private IfDevComponent ensureComponentLoaded(long componentId) throws SQLException
    {
        IfDevComponent component = componentById.get(componentId);
        if (component != null)
        {
            return component;
        }

        try(PreparedStatement componentSelect = connection.prepareStatement(
                String.format("SELECT namespace_id, name, base_type_id, info FROM %s WHERE id = ?",
                        TableName.COMPONENT)))
        {
            componentSelect.setLong(1, componentId);
            try (ResultSet componentRs = componentSelect.executeQuery())
            {
                Preconditions.checkState(componentRs.next(), "component not found");

                IfDevNamespace namespace = Preconditions.checkNotNull(namespaceById.get(componentRs.getLong("namespace_id")),
                        "namespace not found");
                long baseTypeId = componentRs.getLong("base_type_id");
                Optional<IfDevMaybeProxy<IfDevType>> baseType =
                        componentRs.wasNull() ? Optional.<IfDevMaybeProxy<IfDevType>>empty()
                                : Optional.of(SimpleIfDevMaybeProxy.object(ensureTypeLoaded(baseTypeId)));

                Set<IfDevMaybeProxy<IfDevComponent>> subComponents = new HashSet<>();
                try(PreparedStatement subComponentsSelect = connection.prepareStatement(
                        String.format(
                                "SELECT sub_component_id FROM %s WHERE component_id = ? ORDER BY sub_component_id ASC",
                                TableName.SUB_COMPONENT)))
                {
                    subComponentsSelect.setLong(1, componentId);
                    try(ResultSet subComponentsRs = subComponentsSelect.executeQuery())
                    {
                        while (subComponentsRs.next())
                        {
                            subComponents
                                    .add(SimpleIfDevMaybeProxy
                                            .object(ensureComponentLoaded(subComponentsRs.getLong("sub_component_id"))));
                        }
                    }
                }

                List<IfDevCommand> commands = new ArrayList<>();
                try (PreparedStatement commandsSelect = connection.prepareStatement(
                        String.format("SELECT id, name, command_id, info FROM %s WHERE component_id = ?",
                                TableName.COMMAND)))
                {
                    commandsSelect.setLong(1, componentId);
                    try (ResultSet commandsSelectRs = commandsSelect.executeQuery();
                        PreparedStatement argumentsSelect = connection.prepareStatement(String.format(
                                "SELECT name, type_id, unit_id, info FROM %s WHERE command_id = ? ORDER BY argument_index ASC",
                                TableName.COMMAND_ARGUMENT)))
                    {
                        while (commandsSelectRs.next())
                        {
                            List<IfDevCommandArgument> arguments = new ArrayList<>();
                            long commandId = commandsSelectRs.getLong("id");
                            argumentsSelect.setLong(1, commandId);
                            try (ResultSet commandArgumentsRs = argumentsSelect.executeQuery())
                            {
                                while (commandArgumentsRs.next())
                                {
                                    long unitId = commandArgumentsRs.getLong("unit_id");
                                    Optional<IfDevMaybeProxy<IfDevUnit>> unit = commandArgumentsRs.wasNull()
                                            ? Optional.<IfDevMaybeProxy<IfDevUnit>>empty()
                                            : Optional.of(SimpleIfDevMaybeProxy.object(
                                            Preconditions.checkNotNull(unitById.get(unitId), "unit not found")));

                                    arguments.add(ImmutableIfDevCommandArgument.newInstance(
                                            ImmutableIfDevName.newInstanceFromMangledName(
                                                    commandArgumentsRs.getString("name")),
                                            SimpleIfDevMaybeProxy.object(ensureTypeLoaded(
                                                    commandArgumentsRs.getLong("type_id"))),
                                            unit, Optional.ofNullable(commandArgumentsRs.getString("info"))));
                                }
                            }
                            commands.add(ImmutableIfDevCommand.newInstance(
                                    ImmutableIfDevName.newInstanceFromMangledName(commandsSelectRs.getString("name")),
                                    commandsSelectRs.getInt("command_id"),
                                    Optional.ofNullable(commandsSelectRs.getString("info")), arguments));
                        }

                    }
                }

                List<IfDevMessage> messages = new ArrayList<>();
                try (PreparedStatement messagesSelect = connection.prepareStatement(String.format(
                        "SELECT m.id AS id, m.name AS name, m.message_id AS message_id, m.info AS info," +
                                " s.message_id AS s_message_id, e.message_id AS e_message_id," +
                                " d.message_id AS d_message_id FROM %s AS m LEFT JOIN %s AS s ON s.message_id = m.id" +
                                " LEFT JOIN %s AS e ON e.message_id = m.id LEFT JOIN %s AS d ON d.message_id = m.id" +
                                " WHERE component_id = ?", TableName.MESSAGE, TableName.STATUS_MESSAGE,
                        TableName.EVENT_MESSAGE, TableName.DYNAMIC_STATUS_MESSAGE)))
                {
                    messagesSelect.setLong(1, componentId);
                    try (ResultSet messagesSelectRs = messagesSelect.executeQuery();
                         PreparedStatement messageParametersSelect = connection.prepareStatement(String.format(
                                 "SELECT name FROM %s WHERE message_id = ? ORDER BY parameter_index ASC",
                                 TableName.MESSAGE_PARAMETER)))
                    {
                        while (messagesSelectRs.next())
                        {
                            messageParametersSelect.setLong(1, messagesSelectRs.getLong("id"));
                            List<IfDevMessageParameter> parameters = new ArrayList<>();
                            try (ResultSet messageParametersRs = messageParametersSelect.executeQuery())
                            {
                                while (messageParametersRs.next())
                                {
                                    parameters.add(ImmutableIfDevMessageParameter.newInstance(
                                            messageParametersRs.getString("name")));
                                }
                            }

                            IfDevName messageName = ImmutableIfDevName
                                    .newInstanceFromMangledName(messagesSelectRs.getString("name"));
                            int messageId = messagesSelectRs.getInt("message_id");
                            Optional<String> messageInfo = Optional.ofNullable(messagesSelectRs.getString("info"));

                            IfDevMessage message = null;

                            messagesSelectRs.getLong("s_message_id");
                            if (!messagesSelectRs.wasNull())
                            {
                                message = ImmutableIfDevStatusMessage.newInstance(messageName, messageId, messageInfo,
                                        parameters);
                            }

                            messagesSelectRs.getLong("e_message_id");
                            if (!messagesSelectRs.wasNull())
                            {
                                Preconditions.checkState(message == null, "invalid message");
                                message = ImmutableIfDevEventMessage.newInstance(messageName, messageId, messageInfo,
                                        parameters);
                            }

                            messagesSelectRs.getLong("d_message_id");
                            if (!messagesSelectRs.wasNull())
                            {
                                Preconditions.checkState(message == null, "invalid message");
                                message = ImmutableIfDevDynamicStatusMessage.newInstance(messageName, messageId,
                                        messageInfo, parameters);
                            }

                            messages.add(Preconditions.checkNotNull(message, "invalid message"));
                        }
                    }
                }

                component = ImmutableIfDevComponent.newInstance(
                        ImmutableIfDevName.newInstanceFromMangledName(componentRs.getString("name")), namespace, baseType,
                        Optional.ofNullable(componentRs.getString("info")), subComponents, commands, messages);

                componentById.put(componentId, component);
                namespace.getComponents().add(component);
                return component;
            }
        }
    }

    private IfDevType ensureTypeLoaded(long typeId) throws SQLException
    {
        IfDevType type = typeById.get(typeId);
        if (type != null)
        {
            return type;
        }

        try (PreparedStatement typeSelect = connection.prepareStatement(String.format(
                "SELECT t.namespace_id AS namespace_id, t.name AS name, t.info AS info, p.kind AS kind," +
                        " p.bit_length AS bit_length, a.base_type_id AS a_base_type_id," +
                        " s.base_type_id AS s_base_type_id, e.base_type_id AS e_base_type_id," +
                        " ar.base_type_id AS ar_base_type_id, ar.min_length AS min_length, ar.max_length AS max_length," +
                        " str.id AS str_type_id FROM %s AS t LEFT JOIN %s AS p ON p.type_id = t.id" +
                        " LEFT JOIN %s AS a ON a.type_id = t.id LEFT JOIN %s AS s ON s.type_id = t.id" +
                        " LEFT JOIN %s AS e ON e.type_id = t.id LEFT JOIN %s AS ar ON ar.type_id = t.id" +
                        " LEFT JOIN %s AS str ON str.type_id = t.id WHERE t.id = ?",
                TableName.TYPE, TableName.PRIMITIVE_TYPE, TableName.ALIAS_TYPE, TableName.SUB_TYPE,
                TableName.ENUM_TYPE, TableName.ARRAY_TYPE, TableName.STRUCT_TYPE)))
        {
            typeSelect.setLong(1, typeId);
            try (ResultSet typeRs = typeSelect.executeQuery())
            {
                Preconditions.checkState(typeRs.next(), "type not found");
                IfDevNamespace namespace = Preconditions.checkNotNull(namespaceById.get(typeRs.getLong("namespace_id")),
                        "namespace not found for type");
                Optional<IfDevName> name = Optional.ofNullable(typeRs.getString("name")).map(
                        ImmutableIfDevName::newInstanceFromMangledName);
                Optional<String> info = Optional.ofNullable(typeRs.getString("info"));
                String primitiveKind = typeRs.getString("kind");

                if (primitiveKind != null)
                {
                    type = ImmutableIfDevPrimitiveType
                            .newInstance(name, namespace, IfDevType.TypeKind.forName(primitiveKind).orElseThrow(
                                    AssertionError::new),
                                    typeRs.getLong("bit_length"), info);
                }

                long aliasBaseTypeId = typeRs.getLong("a_base_type_id");
                if (!typeRs.wasNull())
                {
                    Preconditions.checkState(type == null, "invalid type");
                    type = ImmutableIfDevAliasType.newInstance(name.get(), namespace,
                            SimpleIfDevMaybeProxy.object(ensureTypeLoaded(aliasBaseTypeId)), info);
                }

                long subTypeBaseTypeId = typeRs.getLong("s_base_type_id");
                if (!typeRs.wasNull())
                {
                    Preconditions.checkState(type == null, "invalid type");
                    type = ImmutableIfDevSubType.newInstance(name, namespace,
                            SimpleIfDevMaybeProxy.object(ensureTypeLoaded(subTypeBaseTypeId)), info);
                }

                long enumBaseTypeId = typeRs.getLong("e_base_type_id");
                if (!typeRs.wasNull())
                {
                    Preconditions.checkState(type == null, "invalid type");
                    PreparedStatement enumConstantsStmt = connection.prepareStatement(
                            String.format("SELECT name, info, value FROM %s WHERE enum_type_id = ?",
                                    TableName.ENUM_TYPE_CONSTANT));
                    enumConstantsStmt.setLong(1, enumBaseTypeId);
                    ResultSet constantsRs = enumConstantsStmt.executeQuery();
                    Set<IfDevEnumConstant> constants = new HashSet<>();
                    while (constantsRs.next())
                    {
                        constants.add(ImmutableIfDevEnumConstant
                                .newInstance(ImmutableIfDevName.newInstanceFromMangledName(
                                                Preconditions.checkNotNull(constantsRs.getString(0), "constant name")),
                                        constantsRs.getString(2), Optional.ofNullable(constantsRs.getString(1))));
                    }
                    type = ImmutableIfDevEnumType.newInstance(name, namespace,
                            SimpleIfDevMaybeProxy.object(ensureTypeLoaded(enumBaseTypeId)), info, constants);
                }

                long arrayBaseTypeId = typeRs.getLong("ar_base_type_id");
                if (!typeRs.wasNull())
                {
                    Preconditions.checkState(type == null, "invalid type");
                    type = ImmutableIfDevArrayType.newInstance(name, namespace,
                            SimpleIfDevMaybeProxy.object(ensureTypeLoaded(arrayBaseTypeId)), info,
                            ImmutableIfDevArrayType.ImmutableArraySize.newInstance(typeRs.getLong("min_length"),
                                    typeRs.getLong("max_length")));
                }

                long structTypeId = typeRs.getLong("str_type_id");
                if (!typeRs.wasNull())
                {
                    Preconditions.checkState(type == null, "invalid type");
                    PreparedStatement structFieldsStmt = connection.prepareStatement(String.format(
                            "SELECT name, type_id, unit_id, info FROM %s WHERE struct_type_id = ? ORDER BY field_index ASC",
                            TableName.STRUCT_TYPE_FIELD));
                    structFieldsStmt.setLong(1, structTypeId);
                    ResultSet structFieldsRs = structFieldsStmt.executeQuery();
                    List<IfDevStructField> fields = new ArrayList<>();
                    while (structFieldsRs.next())
                    {
                        long unitId = structFieldsRs.getLong(3);
                        Optional<IfDevUnit> unit = structFieldsRs.wasNull() ? Optional.<IfDevUnit>empty() : Optional.of(
                                unitById.get(unitId));
                        fields.add(ImmutableIfDevStructField.newInstance(ImmutableIfDevName.newInstanceFromMangledName(
                                        structFieldsRs.getString(1)),
                                SimpleIfDevMaybeProxy.object(ensureTypeLoaded(structFieldsRs.getLong(
                                        2))), unit.map(SimpleIfDevMaybeProxy::object), info));
                    }
                    Preconditions.checkState(!fields.isEmpty(), "struct must not be empty");
                    type = ImmutableIfDevStructType.newInstance(name, namespace, info, fields);
                }

                namespace.getTypes().add(Preconditions.checkNotNull(type, "invalid type"));
            }
        }

        return type;
    }
}
