package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.EventsAPI.PlayerDamageByEntityEvent;
import lt.itsvaidas.EventsAPI.PlayerDamageByPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PlayerDamageByEntityListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onPlayerDamageByPlayer(EntityDamageByEntityEvent e) {
        Player victim = null;
        Entity damager = null;

        if (e.getEntity() instanceof Player player)
            victim = player;

        if (e.getDamager() instanceof Player player)
            damager = player;
        else if (e.getDamager() instanceof Projectile projectile)
            if (!(projectile.getShooter() instanceof Player) && projectile.getShooter() instanceof Entity entity)
                damager = entity;

        if (victim == null || damager == null) return;

        PlayerDamageByEntityEvent event = new PlayerDamageByEntityEvent(victim, damager, e);
        if (!event.callEvent())
            e.setCancelled(true);
    }
}
