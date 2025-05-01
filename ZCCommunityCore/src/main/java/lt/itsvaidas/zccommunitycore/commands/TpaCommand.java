package lt.itsvaidas.zccommunitycore.commands;

import lt.itsvaidas.CommandsAPI.anotations.Argument;
import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.CommandsAPI.argumentproviders.PlayerProvider;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.zccommunitycore.TemporaryData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

@Command(
		name = "tpa",
		description = "Request teleport to player"
)
public class TpaCommand {

	private final Plugin plugin;

	public TpaCommand(Plugin plugin) {
		this.plugin = plugin;
	}

	@Path(
			path = "<Player>",
			description = "Request teleport to player",
			arguments = {
					@Argument(key = "player", provider = PlayerProvider.class)
			}
	)
	public void command(Player player, Player target) {
		if (target.getUniqueId().equals(player.getUniqueId())) {
			MSG.Send.error(player, "You can't teleport to yourself");
			return;
		}

		UUID lastRequest = TemporaryData.TpaRequests.get(player.getUniqueId());
		if (lastRequest != null && lastRequest.equals(target.getUniqueId())) {
			MSG.Send.error(player, "You already have a teleport request to this player");
			return;
		}

		TemporaryData.TpaRequests.add(player.getUniqueId(), target.getUniqueId());
		Bukkit.getScheduler().runTaskLater(plugin, () -> {
			UUID request = TemporaryData.TpaRequests.get(player.getUniqueId());
			if (request != null && request.equals(target.getUniqueId())) {
				TemporaryData.TpaRequests.remove(player.getUniqueId());
			}
		}, 2400);
		MSG.Send.success(player, "Teleport request sent to " + target.getName());
		MSG.Send.info(target, player.getName() + " has requested to teleport to you. Type /tpaccept to accept it.");
	}
}
