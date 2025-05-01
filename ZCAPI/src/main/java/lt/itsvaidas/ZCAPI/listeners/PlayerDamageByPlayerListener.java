package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.EventsAPI.PlayerDamageByPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageByPlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
        Player victim = null;
        Player damager = null;

        if (e.getEntity() instanceof Player player)
            victim = player;

        if (e.getDamager() instanceof Player player)
            damager = player;
        else if (e.getDamager() instanceof Projectile projectile)
            if (projectile.getShooter() instanceof Player player)
                damager = player;

        if (victim == null || damager == null) return;

        PlayerDamageByPlayerEvent event = new PlayerDamageByPlayerEvent(victim, damager, e);
        if (!event.callEvent())
            e.setCancelled(true);
    }
}
