package lt.itsvaidas.ZCAPI.commands;

import lt.itsvaidas.CommandsAPI.anotations.Argument;
import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.CommandsAPI.argumentproviders.PlayerProvider;
import lt.itsvaidas.CommandsAPI.enums.ArgumentType;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.MessagesAPI.MessagesAPI;
import lt.itsvaidas.ZCAPI.argumentproviders.StaticMessageKeysProvider;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Command(
        name = "sendt",
        description = "Send a translatable message",
        permission = "zcapi.commands.sendt"
)
public class SendTCommand {

    @Path(
        path = "<Player> <Key> [Arguments]",
        description = "Send a translatable message to a players or player",
        arguments = {
                @Argument(key = "player", provider = PlayerProvider.class),
                @Argument(key = "key", provider = StaticMessageKeysProvider.class),
                @Argument(key = "arguments", type = ArgumentType.SENTENCE)
        }
    )
    public void onSendMessage(CommandSender sender, String player, String key, String arguments) {
        Collection<? extends Player> receivers;
        if (player.equalsIgnoreCase("@a")) {
            receivers = Bukkit.getOnlinePlayers();
        } else {
            Player p = Bukkit.getPlayer(player);
            if (p == null) {
                MSG.Send.error(sender, "Player was not found!");
                return;
            }

            receivers = List.of(p);
        }

        List<String> args = new ArrayList<>();

        StringBuilder currentArgument = new StringBuilder();
        if (arguments != null) {
            for (String argument : arguments.split(" ")) {
                if (argument.startsWith("\"")) {
                    currentArgument = new StringBuilder(argument.substring(1));
                } else if (argument.endsWith("\"")) {
                    currentArgument.append(" ").append(argument, 0, argument.length() - 1);
                    args.add(currentArgument.toString());
                    currentArgument = new StringBuilder();
                } else if (!currentArgument.isEmpty()) {
                    currentArgument.append(" ").append(argument);
                } else {
                    args.add(argument);
                }
            }
        }

        receivers.forEach(p -> {
            String message = MessagesAPI.getString(p, true, "ZCAPI", "Static Messages." + key);
            if (message == null) {
                MSG.Send.error(sender, "Message with provided key was not found!");
                return;
            }

            Object[] argumentsArray = args.toArray(new String[0]);
            MSG.Send.raw(p, message, argumentsArray);
        });
    }
}
