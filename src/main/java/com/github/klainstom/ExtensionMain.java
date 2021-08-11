package com.github.klainstom;

import net.minestom.server.MinecraftServer;
import net.minestom.server.extensions.DiscoveredExtension;
import net.minestom.server.extensions.Extension;

import java.util.Arrays;

public class ExtensionMain extends Extension {
    @Override
    public void initialize() {
        DiscoveredExtension extension = this.getOrigin();
        MinecraftServer.LOGGER.info(String.format("%s %s by %s load", extension.getName(), extension.getVersion(), Arrays.toString(extension.getAuthors())));
        MinecraftServer.getCommandManager().register(ScriptCommand.COMMAND);
    }

    @Override
    public void terminate() {
        DiscoveredExtension extension = this.getOrigin();
        MinecraftServer.LOGGER.info(String.format("%s %s by %s unload", extension.getName(), extension.getVersion(), Arrays.toString(extension.getAuthors())));
        MinecraftServer.getCommandManager().unregister(ScriptCommand.COMMAND);
    }
}
