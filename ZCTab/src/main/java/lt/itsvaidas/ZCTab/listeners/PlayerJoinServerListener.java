package lt.itsvaidas.ZCTab.listeners;

import lt.itsvaidas.ZCTab.utils.TabListUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class PlayerJoinServerListener implements Listener {

    private final Plugin plugin;

    public PlayerJoinServerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        Bukkit.getOnlinePlayers().forEach(TabListUtils::updateTablist);
    }
}
