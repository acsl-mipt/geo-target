package ru.cpb9.ifdev.model.provider;

import org.jetbrains.annotations.NotNull;
import org.kohsuke.args4j.Argument;

import java.io.File;

/**
 * @author Artem Shein
 */
public class IfDevSqlProviderConfiguration
{
    @NotNull
    @Argument(required = true, metaVar = "CONNECTION_URL", usage = "DB connection URL")
    private String connectionUrl;

    @NotNull
    public String getConnectionUrl()
    {
        return connectionUrl;
    }

    public void setConnectionUrl(@NotNull String connectionUrl)
    {
        this.connectionUrl = connectionUrl;
    }
}
