package lt.itsvaidas.ZCTab.commands;

import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCTab.configs.Config;
import lt.itsvaidas.ZCTab.configs.TabConfig;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

@Command(
        name = "tab",
        description = "Tab command",
        permission = "zctab.command.tab"
)
public class TabCommand {

    private final Plugin plugin;

    public TabCommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Path(
            path = "reload",
            description = "Reloads tab configurations"
    )
    public void onReload(CommandSender sender) {
        plugin.reloadConfig();
        Config.setConfig(plugin.getConfig());
        TabConfig.load(plugin);
        MSG.Send.success(sender, "Tab configurations reloaded");
    }
}
