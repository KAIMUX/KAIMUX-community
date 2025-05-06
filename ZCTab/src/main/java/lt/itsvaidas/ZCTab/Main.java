package lt.itsvaidas.ZCTab;

import lt.itsvaidas.CommandsAPI.CommandRegister;
import lt.itsvaidas.ZCTab.commands.TabCommand;
import lt.itsvaidas.ZCTab.configs.Config;
import lt.itsvaidas.ZCTab.configs.TabConfig;
import lt.itsvaidas.ZCTab.listeners.PlayerJoinServerListener;
import lt.itsvaidas.ZCTab.runnables.SortPlayersInTabListRunnable;
import lt.itsvaidas.ZCTab.runnables.UpdateTablistRunnable;
import lt.itsvaidas.ZCTab.utils.TabListUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

	@Override
	public void onEnable() {
		saveDefaultConfig();

		Config.setConfig(getConfig());
		TabConfig.load(this);

		registerListeners();
		registerRunnables();
		registerCommands();
		loadTablist();
	}

	private void registerCommands() {
		CommandRegister cr = new CommandRegister(this);
		cr.register(new TabCommand(this));
	}

	private void registerRunnables() {
		Bukkit.getScheduler().runTaskTimer(this, new UpdateTablistRunnable(), 600L, 600L);
		Bukkit.getScheduler().runTaskTimer(this, new SortPlayersInTabListRunnable(), 100L, 100L);
	}

	private void loadTablist() {
		Bukkit.getOnlinePlayers().forEach(TabListUtils::updateTablist);
	}

	private void registerListeners() {
		PluginManager pm = Bukkit.getPluginManager();

		pm.registerEvents(new PlayerJoinServerListener(this), this);
	}
}
