package lt.itsvaidas.ZCAPI.commands;

import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Command(
        name = "logs",
        description = "Enable/Disable logging into chat",
        permission = "zcapi.commands.logs"
)
public class LogsCommand {

    @Path(
        description = "Enable/Disable logging"
    )
    public void onLogCommand(CommandSender sender) {
        if (sender instanceof Player player) {
            if (Main.getLogEnabledPlayers().contains(player.getUniqueId())) {
                Main.getLogEnabledPlayers().remove(player.getUniqueId());
                MSG.Send.success(player, "Logging <red>disabled");
            } else {
                Main.getLogEnabledPlayers().add(player.getUniqueId());
                MSG.Send.success(player, "Logging <green>enabled");
            }
        }
    }
}
