package com.github.klainstom.bashscripts;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.minestom.server.command.CommandSender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

class ProcessChatOutput {
    private final CommandSender sender;

    private BufferedReader outputReader;
    private BufferedReader errorReader;

    private final Thread threadOut;
    private final Thread threadErr;
    private boolean running = true;

    ProcessChatOutput(CommandSender sender, String scriptName) {
        this.sender = sender;

        Component OUT_PREFIX = Component.text(String.format("[%s] ", scriptName), NamedTextColor.YELLOW);
        Component ERR_PREFIX = Component.text(String.format("[%s] ", scriptName), NamedTextColor.RED);

        try {
            Process process = Runtime.getRuntime().exec(String.format("sh ./scripts/%s", scriptName));
            outputReader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
        } catch (IOException e) {
            sender.sendMessage(Component.text("Something went wrong.", NamedTextColor.RED));
            e.printStackTrace();
        }
        threadOut = new Thread(new OutToChat(outputReader, OUT_PREFIX));
        threadErr = new Thread(new OutToChat(errorReader, ERR_PREFIX));
        threadOut.start();
        threadErr.start();
    }

    private void stop() {
        running = false;
        if (threadOut != null) {
            try {
                threadOut.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (threadErr != null) {
            try {
                threadErr.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class OutToChat implements Runnable{
        private final BufferedReader reader;
        private final Component prefix;

        private OutToChat(BufferedReader reader, Component prefix) {
            this.reader = reader;
            this.prefix = prefix;
        }

        @Override
        public void run() {
            try {
                String out;
                while (running && (out = reader.readLine()) != null) {
                    sender.sendMessage(prefix.append(Component.text(out)));
                }
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}