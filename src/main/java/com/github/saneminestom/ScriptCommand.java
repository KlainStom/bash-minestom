package com.github.saneminestom;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.builder.Command;
import net.minestom.server.command.builder.arguments.ArgumentType;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ScriptCommand extends Command {
    public static final ScriptCommand COMMAND = new ScriptCommand();
    private ScriptCommand() { // list all scripts available and permitted
        super("script");
        File scriptsDir = new File("scripts");
        setDefaultExecutor((sender, context) -> {
            Set<String> availableScripts = new HashSet<>();
            if (scriptsDir.isDirectory()) {
                availableScripts = new HashSet<>(List.of(Objects.requireNonNull(scriptsDir.list())));
            }
            // TODO: 07.08.21 filter scripts by permission
            Component chatMessage;
            if (availableScripts.isEmpty()) chatMessage = Component.text("No scripts available!", NamedTextColor.RED);
            else {
                chatMessage = Component.text("You have permission for those scripts:", NamedTextColor.DARK_GREEN);
                for (String scriptFile : availableScripts) {
                    chatMessage = chatMessage.append(Component.text(String.format("  %s\n", scriptFile), NamedTextColor.GOLD));
                }
            }
            sender.sendMessage(chatMessage);
        });

        var scriptArgument = ArgumentType.String("script");
        addSyntax((sender, context) -> {
            new ProcessChatOutput(sender, context.get(scriptArgument));
        }, scriptArgument);
    }
}
