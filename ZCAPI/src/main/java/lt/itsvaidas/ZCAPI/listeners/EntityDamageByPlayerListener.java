package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.EventsAPI.EntityDamageByPlayerEvent;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityDamageByPlayerListener implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByPlayer(EntityDamageByEntityEvent e) {
        Entity victim = e.getEntity();
        Player damager = null;

        if (e.getDamager() instanceof Player player)
            damager = player;
        else if (e.getDamager() instanceof Projectile projectile)
            if (projectile.getShooter() instanceof Player player)
                damager = player;

        if (damager == null) return;

        EntityDamageByPlayerEvent event = new EntityDamageByPlayerEvent(victim, damager, e);
        if (!event.callEvent())
            e.setCancelled(true);
    }
}
