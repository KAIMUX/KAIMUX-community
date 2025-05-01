package lt.itsvaidas.ZCAPI;

import lt.itsvaidas.CommandsAPI.CommandRegister;
import lt.itsvaidas.EconomyAPI.EconomyAPI;
import lt.itsvaidas.EconomyAPI.listeners.EconomyServiceRegisterListener;
import lt.itsvaidas.MenuAPI.EnterValueGUI;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.commands.*;
import lt.itsvaidas.ZCAPI.data.CommandsData;
import lt.itsvaidas.ZCAPI.data.PlayerUUIDData;
import lt.itsvaidas.ZCAPI.dto.CommandDTO;
import lt.itsvaidas.ZCAPI.listeners.*;
import lt.itsvaidas.ZCAPI.services.GUICommandService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

public class Main extends JavaPlugin implements Listener {

	private static final List<UUID> logEnabledPlayers = new ArrayList<>();
	private static Plugin instance;
	
	@Override
	public void onEnable() {
		saveDefaultConfig();
		Config.load(this);
		MysqlConfig.load(this);
		RegisterPlaceholders.register();

		instance = this;

		PlayerUUIDData.load(this);
		CommandsData.load(this);

		setupEconomy();
		registerCommands();
		registerListeners();
	}

	private void registerListeners() {
		Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDamageByPlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerDamageByEntityListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
		Bukkit.getPluginManager().registerEvents(new EntityDamageByPlayerListener(), this);
		Bukkit.getPluginManager().registerEvents(new RemovePDCFromBlockListener(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerCommandListener(), this);
		Bukkit.getPluginManager().registerEvents(new GUICommandService(), this);

		Bukkit.getPluginManager().registerEvents(new EnterValueGUI(), this);

		if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
			Bukkit.getPluginManager().registerEvents(new EconomyServiceRegisterListener(), this);
		}
	}

	private void setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		Economy economy = rsp.getProvider();
		EconomyAPI.set(economy);
		MSG.log(Level.INFO, "[ZCAPI] [PluginLoad] Registered economy service from " + economy.getName());
	}

	private void registerCommands() {
		CommandRegister register = new CommandRegister(this);
		register.register(new LogsCommand());
		register.register(new ZCAPICommand(this));

		for (CommandDTO command : CommandsHolder.getCommands())
			Bukkit.getCommandMap().register("zcapi", new FakeCommand(command));
	}

	public static Plugin getInstance() {
		return instance;
	}

	public static List<UUID> getLogEnabledPlayers() {
		return logEnabledPlayers;
	}
	
	@Override
	public void onDisable() {
		PlayerUUIDData.save();
		RegisterPlaceholders.unregister();
		Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "stop");
	}
}
