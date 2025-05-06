package lt.itsvaidas.zccommunitycore;

import lt.itsvaidas.CommandsAPI.CommandRegister;
import lt.itsvaidas.zccommunitycore.commands.SethomeCommand;
import lt.itsvaidas.zccommunitycore.commands.TpaCommand;
import lt.itsvaidas.zccommunitycore.commands.TpacceptCommand;
import lt.itsvaidas.zccommunitycore.listeners.ArmorStandOnPlayerQuitListener;
import lt.itsvaidas.zccommunitycore.listeners.PlayerDamageByPlayerListener;
import lt.itsvaidas.zccommunitycore.listeners.PlayerJoinListener;
import lt.itsvaidas.zccommunitycore.listeners.SpawnerEventsListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        registerListeners();
        registerCommands();
    }

    private void registerCommands() {
        CommandRegister cr = new CommandRegister(this);
        cr.register(new TpaCommand(this));
        cr.register(new TpacceptCommand());
        cr.register(new SethomeCommand());
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerDamageByPlayerListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
        pm.registerEvents(new ArmorStandOnPlayerQuitListener(this), this);
        pm.registerEvents(new SpawnerEventsListener(this), this);
    }

    @Override
    public void onDisable() {

    }
}
