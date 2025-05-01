package lt.itsvaidas.zccommunitycore;

import lt.itsvaidas.CommandsAPI.CommandRegister;
import lt.itsvaidas.zccommunitycore.commands.TpaCommand;
import lt.itsvaidas.zccommunitycore.commands.TpacceptCommand;
import lt.itsvaidas.zccommunitycore.listeners.PlayerDamageByPlayerListener;
import lt.itsvaidas.zccommunitycore.listeners.PlayerJoinListener;
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
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerDamageByPlayerListener(), this);
        pm.registerEvents(new PlayerJoinListener(), this);
    }

    @Override
    public void onDisable() {

    }
}
