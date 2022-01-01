package fr.pearl.core.common.command;

import fr.pearl.api.common.command.PearlCommand;
import fr.pearl.api.common.command.PearlSender;

import java.util.Arrays;

public class CommandExecutor {

    public static void execute(PearlCommand command, PearlSender sender, String label, String[] args) {
        if (command.isPlayersOnly() && !sender.isPlayer()) {
            sender.sendMessage("§cYou must be a player to execute this command.");
            return;
        }

        if (command.getPermission() != null && sender.hasPermission(command.getPermission())) {
            if (command.getPermissionMessage() != null) sender.sendMessage(command.getPermissionMessage());
            return;
        }

        if (command.getArgumentMap().isEmpty() || args.length < 1) {
            command.execute(sender, label, args);
            return;
        }

        String arg0 = args[0].toLowerCase();
        PearlCommand argument = command.getArgumentMap().get(arg0);
        if (argument == null) {
            command.execute(sender, label, args);
            return;
        }

        argument.execute(sender, label, Arrays.copyOfRange(args, 1, args.length));
    }
}
