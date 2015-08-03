package ru.cpb9.ifdev.idea.plugin;

import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import com.google.common.io.Resources;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import com.intellij.psi.ClassFileViewProvider;
import com.intellij.psi.PsiManager;
import ru.cpb9.ifdev.parser.psi.IfDevFile;
import ru.cpb9.ifdev.model.exporter.ModelExportingException;
import ru.cpb9.ifdev.model.exporter.IfDevSqlite3Exporter;
import ru.cpb9.ifdev.model.exporter.IfDevSqlite3ExporterConfiguration;
import ru.cpb9.ifdev.model.domain.IfDevReferenceable;
import ru.cpb9.ifdev.model.domain.IfDevRegistry;
import ru.cpb9.ifdev.model.domain.impl.SimpleIfDevRegistry;
import ru.cpb9.ifdev.model.domain.impl.proxy.SimpleIfDevDomainModelResolver;
import ru.cpb9.ifdev.model.domain.proxy.IfDevResolvingResult;
import ru.cpb9.ifdev.modeling.TransformationResult;
import ru.cpb9.ifdev.parser.IfDevFileType;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.regex.Pattern;

/**
 * @author Artem Shein
 */
public class IfDevGenerateSqliteForIfDevSourcesAction extends AnAction
{

    public static final String GROUP_DISPLAY_ID = "IfDev SQLite Generation";

    @Override
    public void actionPerformed(AnActionEvent anActionEvent)
    {
        Project project = anActionEvent.getProject();
        if (project == null)
        {
            return;
        }
        PsiManager psiManager = PsiManager.getInstance(project);
        IfDevRegistry registry = SimpleIfDevRegistry.newInstance();
        TransformationResult<IfDevRegistry> result = new IfDevTransformationResult(registry);
        ProjectRootManager.getInstance(project).getFileIndex().iterateContent(virtualFile -> {
            if (virtualFile.getFileType().equals(
                    IfDevFileType.INSTANCE))
            {
                IfDevFile file = new IfDevFile(new ClassFileViewProvider(psiManager, virtualFile));
                new IfDevFileProcessor(registry, result).process(file);
            }
            return true;
        });
        result.getMessages().forEach(IfDevFileProcessor::notifyUser);
        if (!result.hasError() && result.getResult().isPresent())
        {
            Preconditions.checkState(result.getResult().get() == registry);
            IfDevResolvingResult<IfDevReferenceable> resolvingResult = SimpleIfDevDomainModelResolver.newInstance().resolve(
                    registry);
            if (resolvingResult.hasError())
            {
                resolvingResult.getMessages()
                        .forEach(IfDevFileProcessor::notifyUser);
            }
        }
        FileSaverDialog fileChooserDialog = FileChooserFactory.getInstance().createSaveFileDialog(
                new FileSaverDescriptor("Save file to", "", "sqlite"), (Project) null);
        VirtualFileWrapper fileWrapper = fileChooserDialog.save(project.getBaseDir(), "ifdev.sqlite");
        if (fileWrapper == null)
        {
            return;
        }
        VirtualFile sqliteVirtualFile = fileWrapper.getVirtualFile(true);
        if (sqliteVirtualFile == null)
        {
            return;
        }
        File sqliteFile = VfsUtil.virtualToIoFile(sqliteVirtualFile);
        if (sqliteFile.exists())
        {
            if (!sqliteFile.delete())
            {
                Notifications.Bus.notify(new Notification(GROUP_DISPLAY_ID, "Can't delete file",
                        String.format("File '%s' can't be deleted", sqliteFile.getAbsolutePath()),
                        NotificationType.ERROR));
            }
        }
        IfDevSqlite3ExporterConfiguration config = new IfDevSqlite3ExporterConfiguration();
        config.setOutputFile(sqliteFile);
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
            connection.setAutoCommit(false);
            for (String sql : Resources.toString(Resources.getResource(this.getClass(), "/edu/phystech/acsl/ifdev/ifdev.sql"), Charsets.UTF_8).split(
                    Pattern.quote(";")))
            {
                connection.prepareStatement(sql).execute();
            }
            connection.commit();
        }
        catch (SQLException | IOException e)
        {
            throw new ModelExportingException(e);
        }
        new IfDevSqlite3Exporter(config).export(registry);
        Notifications.Bus.notify(
                new Notification(GROUP_DISPLAY_ID, "IfDev SQLite3 Generation", "Generated successfully",
                        NotificationType.INFORMATION));
    }
}
