package lt.itsvaidas.zccommunitycore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

public class ArmorStandOnPlayerQuitListener implements Listener {

    private final Plugin plugin;

    public ArmorStandOnPlayerQuitListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        e.getPlayer().getWorld().spawn(e.getPlayer().getLocation(), ArmorStand.class, armorStand -> {
            armorStand.setInvulnerable(true);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (armorStand.isValid()) {
                    armorStand.remove();
                }
            }, 300L);
        });
    }
}
