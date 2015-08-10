package ru.cpb9.ifdev.model.exporter;

import com.google.common.base.Preconditions;
import ru.cpb9.ifdev.model.domain.*;
import ru.cpb9.ifdev.model.domain.message.*;
import ru.cpb9.ifdev.model.domain.proxy.IfDevMaybeProxy;
import ru.cpb9.ifdev.model.domain.type.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.sql.*;
import java.util.*;

/**
 * @author Artem Shein
 */
public class IfDevSqlite3Exporter
{
    @NotNull
    private final Map<IfDevType, Long> typeKeyByType = new HashMap<>();
    @NotNull
    private final Map<IfDevNamespace, Long> namespaceKeyByNamespaceMap = new HashMap<>();
    @NotNull
    private final Map<IfDevUnit, Long> unitKeyByUnit = new HashMap<>();
    @NotNull
    private final Map<IfDevComponent, Long> componentKeyByComponentMap = new HashMap<>();
    @NotNull
    private final IfDevSqlite3ExporterConfiguration config;
    @NotNull
    private Connection connection;
    @NotNull
    private InsertTypeVisitor insertTypeVisitor = new InsertTypeVisitor();

    public IfDevSqlite3Exporter(@NotNull IfDevSqlite3ExporterConfiguration config)
    {
        this.config = config;
    }

    public void export(@NotNull IfDevRegistry registry)
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
                .getConnection("jdbc:sqlite:" + config.getOutputFile().getAbsolutePath()))
        {
            this.connection = connection;
            connection.setAutoCommit(false);
            List<IfDevNamespace> namespacesStream = registry.getRootNamespaces();
            namespacesStream.forEach(this::insertNamespaceAndSubNamespaces);
            namespacesStream.forEach(this::insertUnits);
            namespacesStream.forEach(this::insertTypes);
            namespacesStream.forEach(this::insertComponents);
            connection.commit();
        }
        catch (SQLException e)
        {
            throw new ModelExportingException(e);
        }
    }

    private void insertNamespaceAndSubNamespaces(@NotNull IfDevNamespace namespace)
    {
        try
        {
            insertNamespace(namespace);
            try (PreparedStatement linkNamespaces = connection.prepareStatement(
                    String.format("INSERT INTO %s (namespace_id, sub_namespace_id) VALUES (?, ?)",
                            TableName.SUB_NAMESPACE)))
            {
                for (IfDevNamespace subNamespace : namespace.getSubNamespaces())
                {
                    insertNamespaceAndSubNamespaces(subNamespace);
                    linkNamespaces.setLong(1, namespaceKeyByNamespaceMap.get(namespace));
                    linkNamespaces.setLong(2, namespaceKeyByNamespaceMap.get(subNamespace));
                    linkNamespaces.execute();
                }
            }
        }
        catch (SQLException e)
        {
            throw new ModelExportingException(e);
        }
    }

    private void insertComponents(@NotNull IfDevNamespace namespace)
    {
        namespace.getComponents().forEach(this::insertComponent);
        namespace.getSubNamespaces().forEach(this::insertComponents);
    }

    private void insertComponent(@NotNull IfDevComponent component)
    {
        if (componentKeyByComponentMap.containsKey(component))
        {
            return;
        }
        try(PreparedStatement insertComponent = connection.prepareStatement(
                String.format("INSERT INTO %s (namespace_id, name, base_type_id, info) VALUES (?, ?, ?, ?)", TableName.COMPONENT)))
        {
            insertComponent.setLong(1, namespaceKeyByNamespaceMap.get(component.getNamespace()));
            insertComponent.setString(2, component.getName().asString());
            if (component.getBaseType().isPresent())
            {
                insertType(component.getBaseType().get().getObject());
                insertComponent.setLong(3, typeKeyByType.get(component.getBaseType().get().getObject()));
            }
            else
            {
                insertComponent.setNull(3, Types.BIGINT);
            }
            setStringOrNull(insertComponent, 4, component.getInfo());
            insertComponent.execute();

            long componentKey = getGeneratedKey(insertComponent);
            componentKeyByComponentMap.put(component, componentKey);
            for (IfDevMaybeProxy<IfDevComponent> subComponent : component.getSubComponents())
            {
                insertComponent(subComponent.getObject());
                try (PreparedStatement linkComponents = connection.prepareStatement(
                        String.format("INSERT INTO %s (component_id, sub_component_id) VALUES (?, ?)",
                                TableName.SUB_COMPONENT)))
                {
                    linkComponents.setLong(1, componentKey);
                    linkComponents.setLong(2, componentKeyByComponentMap.get(subComponent.getObject()));
                    linkComponents.execute();
                }
            }
            for (IfDevCommand command : component.getCommands())
            {
                long commandKey;
                try (PreparedStatement insertCommand = connection.prepareStatement(
                        String.format("INSERT INTO %s (component_id, name, command_id, info) VALUES (?, ?, ?, ?)",
                                TableName.COMMAND)))
                {
                    insertCommand.setLong(1, componentKey);
                    insertCommand.setString(2, command.getName().asString());
                    insertCommand.setLong(3, command.getId());
                    setStringOrNull(insertCommand, 4, command.getInfo());
                    insertCommand.execute();
                    commandKey = getGeneratedKey(insertCommand);
                }
                long index = 0;
                for (IfDevCommandArgument argument : command.getArguments())
                {
                    try (PreparedStatement insertArgument = connection.prepareStatement(
                            String.format(
                                    "INSERT INTO %s (command_id, argument_index, name, type_id, unit_id, info) VALUES (?, ?, ?, ?, ?, ?)",
                                    TableName.COMMAND_ARGUMENT)))
                    {
                        insertArgument.setLong(1, commandKey);
                        insertArgument.setLong(2, index++);
                        insertArgument.setString(3, argument.getName().asString());
                        insertArgument.setLong(4, typeKeyByType.get(argument.getType().getObject()));
                        setUnit(insertArgument, 5, argument.getUnit());
                        setStringOrNull(insertArgument, 6, argument.getInfo());
                        insertArgument.execute();
                    }
                }
            }
            for (IfDevMessage message : component.getMessages())
            {
                long messageKey;
                try (PreparedStatement insertMessage = connection.prepareStatement(
                        String.format("INSERT INTO %s (message_id, component_id, name, info) VALUES (?, ?, ?, ?)",
                                TableName.MESSAGE)))
                {
                    insertMessage.setLong(1, message.getId());
                    insertMessage.setLong(2, componentKey);
                    insertMessage.setString(3, message.getName().asString());
                    setStringOrNull(insertMessage, 4, message.getInfo());
                    insertMessage.execute();
                    messageKey = getGeneratedKey(insertMessage);
                }
                message.accept(new IfDevInsertMessageVisitor(messageKey, connection));
                long index = 0;
                for (IfDevMessageParameter parameter : message.getParameters())
                {
                    try (PreparedStatement insertParameter = connection.prepareStatement(
                            String.format("INSERT INTO %s (message_id, parameter_index, name) VALUES (?, ?, ?)",
                                    TableName.MESSAGE_PARAMETER)))
                    {
                        insertParameter.setLong(1, messageKey);
                        insertParameter.setLong(2, index++);
                        insertParameter.setString(3, parameter.getValue());
                        insertParameter.execute();
                    }
                }
            }
        }
        catch (SQLException e)
        {
            throw new ModelExportingException(e);
        }
    }

    private void setUnit(@NotNull PreparedStatement statement, int index,
                                @NotNull Optional<IfDevMaybeProxy<IfDevUnit>> unit) throws SQLException
    {
        if (unit.isPresent())
        {
            statement.setLong(index, unitKeyByUnit.get(unit.get().getObject()));
        }
        else
        {
            statement.setNull(index, Types.BIGINT);
        }
    }

    private void insertTypes(@NotNull IfDevNamespace namespace)
    {
        namespace.getTypes().stream().forEach(this::insertType);
        namespace.getSubNamespaces().stream().forEach(this::insertTypes);
    }

    private void insertType(@NotNull IfDevType type)
    {
        if (typeKeyByType.containsKey(type))
        {
            return;
        }
        System.out.println("--");
        try(PreparedStatement insertType = connection.prepareStatement(
                String.format("INSERT INTO %s (namespace_id, name, info) VALUES (?, ?, ?)", TableName.TYPE)))
        {
            insertType.setLong(1, namespaceKeyByNamespaceMap.get(type.getNamespace()));
            setStringOrNull(insertType, 2, type.getOptionalName().map(IfDevName::asString));
            setStringOrNull(insertType, 3, type.getInfo());
            insertType.execute();
            typeKeyByType.put(type, getGeneratedKey(insertType));
            type.accept(insertTypeVisitor);
        }
        catch (SQLException e)
        {
            throw new ModelExportingException(e, "Can't insert type: %s", type);
        }
    }

    private void setStringOrNull(@NotNull PreparedStatement statement, int index, @NotNull Optional<String> stringOptional) throws
            SQLException
    {
        if (stringOptional.isPresent())
        {
            statement.setString(index, stringOptional.get());
        }
        else
        {
            statement.setNull(index, Types.VARCHAR);
        }
    }

    private void insertUnits(@NotNull IfDevNamespace namespace)
    {
        namespace.getUnits().stream().forEach(this::insertUnit);
        namespace.getSubNamespaces().stream().forEach(this::insertUnits);
    }

    private void insertUnit(@NotNull IfDevUnit unit)
    {
        try(PreparedStatement insertUnit = connection.prepareStatement(
                String.format("INSERT INTO %s (namespace_id, name, display) VALUES (?, ?, ?)", TableName.UNIT)))
        {
            insertUnit.setLong(1, namespaceKeyByNamespaceMap.get(unit.getNamespace()));
            insertUnit.setString(2, unit.getName().asString());
            if (unit.getDisplay().isPresent())
            {
                insertUnit.setString(3, unit.getDisplay().get());
            }
            else
            {
                insertUnit.setNull(3, Types.VARCHAR);
            }
            insertUnit.execute();
            unitKeyByUnit.put(unit, getGeneratedKey(insertUnit));
        }
        catch (SQLException e)
        {
            throw new ModelExportingException(e);
        }
    }

    private void insertNamespace(@NotNull IfDevNamespace namespace)
    {
        try(PreparedStatement insertNamespace = connection.prepareStatement(
                String.format("INSERT INTO %s (name) VALUES (?)", TableName.NAMESPACE)))
        {
            insertNamespace.setString(1, namespace.getName().asString());
            insertNamespace.execute();
            long generatedKey = getGeneratedKey(insertNamespace);
            namespaceKeyByNamespaceMap.put(namespace, generatedKey);
        }
        catch (SQLException e)
        {
            throw new ModelExportingException(e);
        }
    }

    private static long getGeneratedKey(PreparedStatement statement) throws SQLException
    {
        return statement.getGeneratedKeys().getLong(1);
    }

    private class InsertTypeVisitor implements IfDevTypeVisitor<Void, SQLException>
    {
        public InsertTypeVisitor()
        {
        }

        @Nullable
        @Override
        public Void visit(@NotNull IfDevPrimitiveType primitiveType) throws SQLException
        {
            try(PreparedStatement insertPrimitiveType = connection.prepareStatement(
                    String.format("INSERT INTO %s (type_id, kind, bit_length) VALUES (?, ?, ?)", TableName.PRIMITIVE_TYPE)))
            {
                insertPrimitiveType.setLong(1, typeKeyByType.get(primitiveType));
                insertPrimitiveType.setString(2, primitiveType.getKind().getName());
                insertPrimitiveType.setLong(3, primitiveType.getBitLength());
                insertPrimitiveType.execute();
            }
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevSubType subType) throws SQLException
        {
            insertType(subType.getBaseType().getObject());
            try(PreparedStatement insertSubType = connection.prepareStatement(
                    String.format("INSERT INTO %s (type_id, base_type_id) VALUES " +
                                    "(?, ?)",
                            TableName.SUB_TYPE)))
            {
                insertSubType.setLong(1, typeKeyByType.get(subType));
                insertSubType.setLong(2, typeKeyByType.get(subType.getBaseType().getObject()));
                insertSubType.execute();
            }
            return null;
        }

        @Nullable
        @Override
        public Void visit(@NotNull IfDevEnumType enumType) throws SQLException
        {
            insertType(enumType.getBaseType().getObject());
            try(PreparedStatement insertEnumType = connection.prepareStatement(
                    String.format("INSERT INTO %s (type_id, base_type_id) VALUES (?, ?)",
                            TableName.ENUM_TYPE)))
            {
                insertEnumType.setLong(1, typeKeyByType.get(enumType));
                insertEnumType.setLong(2, typeKeyByType.get(enumType.getBaseType().getObject()));
                insertEnumType.execute();
                long enumKey = getGeneratedKey(insertEnumType);
                try(PreparedStatement insertEnumConstant = connection.prepareStatement(
                        String.format("INSERT INTO %s (enum_type_id, name, value, info) VALUES (?, ?, ?, ?)",
                                TableName.ENUM_TYPE_CONSTANT)))
                {
                    for (IfDevEnumConstant constant : enumType.getConstants())
                    {
                        insertEnumConstant.setLong(1, enumKey);
                        insertEnumConstant.setString(2, constant.getName().asString());
                        insertEnumConstant.setString(3, constant.getValue());
                        setStringOrNull(insertEnumConstant, 4, constant.getInfo());
                        insertEnumConstant.execute();
                    }
                }
            }
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevArrayType arrayType) throws SQLException
        {
            insertType(arrayType.getBaseType().getObject());
            try(PreparedStatement insertArrayType = connection.prepareStatement(
                    String.format("INSERT INTO %s (type_id, base_type_id, min_length, max_length) VALUES (?, ?, ?, ?)",
                            TableName.ARRAY_TYPE)))
            {
                insertArrayType.setLong(1, typeKeyByType.get(arrayType));
                insertArrayType.setLong(2, typeKeyByType.get(arrayType.getBaseType().getObject()));
                insertArrayType.setLong(3, arrayType.getSize().getMinLength());
                insertArrayType.setLong(4, arrayType.getSize().getMaxLength());
                insertArrayType.execute();
            }
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevStructType structType) throws SQLException
        {
            try(PreparedStatement insertStructType = connection.prepareStatement(
                    String.format("INSERT INTO %s (type_id) VALUES (?)", TableName.STRUCT_TYPE)))
            {
                insertStructType.setLong(1, typeKeyByType.get(structType));
                insertStructType.execute();
                long structKey = getGeneratedKey(insertStructType);
                try(PreparedStatement insertField = connection.prepareStatement(
                        String.format("INSERT INTO %s (struct_type_id, field_index, name, type_id, unit_id, info) VALUES (?, ?, ?, ?, (SELECT id FROM %s WHERE namespace_id = ? AND name = ?), ?)",
                                TableName.STRUCT_TYPE_FIELD, TableName.UNIT)))
                {
                    long index = 0;
                    Preconditions.checkState(!structType.getFields().isEmpty(), "struct must not be empty");
                    for (IfDevStructField field : structType.getFields())
                    {
                        insertField.setLong(1, structKey);
                        insertField.setLong(2, index++);
                        insertField.setString(3, field.getName().asString());
                        insertType(field.getType().getObject());
                        insertField.setLong(4, typeKeyByType.get(field.getType().getObject()));
                        insertField.setLong(5, namespaceKeyByNamespaceMap.get(structType.getNamespace()));
                        setStringOrNull(insertField, 6, field.getUnit().map(IfDevMaybeProxy::getObject).map(IfDevUnit::getName).map(IfDevName::asString));
                        setStringOrNull(insertField, 7, field.getInfo());
                        insertField.execute();
                    }
                }
            }
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevAliasType typeAlias) throws SQLException
        {
            insertType(typeAlias.getType().getObject());
            try(PreparedStatement insertArrayType = connection.prepareStatement(
                    String.format("INSERT INTO %s (type_id, base_type_id) VALUES (?, ?)",
                            TableName.ALIAS_TYPE)))
            {
                insertArrayType.setLong(1, typeKeyByType.get(typeAlias));
                insertArrayType.setLong(2, typeKeyByType.get(typeAlias.getType().getObject()));
                insertArrayType.execute();
            }
            return null;
        }
    }

    private static class IfDevInsertMessageVisitor implements IfDevMessageVisitor<Void, SQLException>
    {
        private final long messageKey;
        @NotNull
        private final Connection connection;

        public IfDevInsertMessageVisitor(
                long messageKey, @NotNull Connection connection)
        {
            this.messageKey = messageKey;
            this.connection = connection;
        }

        @Override
        public Void visit(@NotNull IfDevEventMessage eventMessage) throws SQLException
        {
            try(PreparedStatement insertEventMessage = connection.prepareStatement(
                    String.format("INSERT INTO %s (message_id) VALUES (?)", TableName.EVENT_MESSAGE)))
            {
                insertEventMessage.setLong(1, messageKey);
                insertEventMessage.execute();
            }
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevStatusMessage statusMessage) throws SQLException
        {
            try(PreparedStatement insertEventMessage = connection.prepareStatement(
                    String.format("INSERT INTO %s (message_id) VALUES (?)", TableName.STATUS_MESSAGE)))
            {
                insertEventMessage.setLong(1, messageKey);
                insertEventMessage.execute();
            }
            return null;
        }

        @Override
        public Void visit(@NotNull IfDevDynamicStatusMessage dynamicStatusMessage) throws SQLException
        {
            try(PreparedStatement insertEventMessage = connection.prepareStatement(
                    String.format("INSERT INTO %s (message_id) VALUES (?)", TableName.DYNAMIC_STATUS_MESSAGE)))
            {
                insertEventMessage.setLong(1, messageKey);
                insertEventMessage.execute();
            }
            return null;
        }
    }
}