package lt.itsvaidas.zccommunitycore.commands;

import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.zccommunitycore.TemporaryData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

@Command(
        name = "tpaccept",
        description = "Accept teleport request"
)
public class TpacceptCommand {

    @Path(
            description = "Accept teleport request"
    )
    public void command(Player player) {
        UUID uuidFrom = TemporaryData.TpaRequests.getFrom(player.getUniqueId());
        if (uuidFrom == null) {
            MSG.Send.error(player, "You have no teleport request");
            return;
        }

        Player from = Bukkit.getPlayer(uuidFrom);
        if (from == null) {
            MSG.Send.error(player, "Player is not online");
            return;
        }

        Location toLocation = player.getLocation().clone();
        MSG.Send.success(from, player.getName() + " accepted your teleport request");
        MSG.Send.success(player, "You accepted " + from.getName() + "'s teleport request");
        TemporaryData.TpaRequests.remove(from.getUniqueId());

        from.teleportAsync(toLocation);
    }
}
