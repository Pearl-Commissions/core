package fr.pearl.core.common.command;

import fr.pearl.api.common.command.PearlCommand;
import fr.pearl.api.common.command.PearlSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandExecutor {

    public static void execute(PearlCommand command, PearlSender sender, String label, String[] args) {
        if (command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            if (command.getPermissionMessage() != null) sender.sendMessage(command.getPermissionMessage());
            return;
        }

        if (command.getArgumentMap().isEmpty() || args.length < 1) {
            if (!testPlayer(command, sender)) return;
            command.execute(sender, label, args);
            return;
        }

        String arg0 = args[0].toLowerCase();
        PearlCommand argument = command.getArgumentMap().get(arg0);
        if (argument == null) {
            argument = command.getAliasMap().get(arg0);
        }
        if (argument == null) {
            if (!testPlayer(command, sender)) return;
            command.execute(sender, label, args);
            return;
        }

        execute(argument, sender, label, Arrays.copyOfRange(args, 1, args.length));
    }

    private static boolean testPlayer(PearlCommand command, PearlSender sender) {
        if (command.isPlayersOnly() && !sender.isPlayer()) {
            sender.sendMessage("§cYou must be a player to execute this command.");
            return false;
        }

        return true;
    }

    public static List<String> tabComplete(PearlCommand command, PearlSender sender, String label, String[] args) {
        if (command.isPlayersOnly() && !sender.isPlayer() || command.getPermission() != null && !sender.hasPermission(command.getPermission())) {
            return null;
        }

        String arg0 = args.length == 0 ? "" : args[0];
        PearlCommand argument = arg0.equals("") ? null : command.getArgumentMap().get(arg0.toLowerCase());

        if (argument == null) {
            List<String> names = new ArrayList<>();
            for (String string : command.getArgumentMap().keySet()) {
                if (string.startsWith(arg0)) {
                    names.add(string);
                }
            }
            List<String> tabComplete = command.tabComplete(sender, label, args);
            if (tabComplete == null) {
                return null;
            }
            names.addAll(tabComplete);

            return names;
        }

        return tabComplete(argument, sender, label, Arrays.copyOfRange(args, 1, args.length));
    }
}
