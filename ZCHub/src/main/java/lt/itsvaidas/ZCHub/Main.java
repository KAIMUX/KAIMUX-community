package lt.itsvaidas.ZCHub;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static lt.itsvaidas.ZCHub.Main.ZCHubAPI.openGUI;

public class Main extends JavaPlugin {

	private static HubGUI hubGUI;

	@Override
	public void onEnable() {
		Config.load(this);

		if (!Bukkit.getMessenger().isOutgoingChannelRegistered(this, "BungeeCord")) {
			Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
		}

		hubGUI = new HubGUI(this);
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		if (sender instanceof Player player) {
			openGUI(player);
		}
		return true;
	}

	public static class ZCHubAPI {
		public static void openGUI(Player player) {
			hubGUI.open(player);
		}
	}
}
