package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.EventsAPI.PlayerInteractAtEntityMainHandEvent;
import lt.itsvaidas.EventsAPI.PlayerInteractMainHandEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;

public class PlayerInteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if (e.getHand() == EquipmentSlot.HAND) {
            PlayerInteractMainHandEvent event = new PlayerInteractMainHandEvent(e);
            if (!event.callEvent())
                e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (e.getHand() == EquipmentSlot.HAND) {
            PlayerInteractAtEntityMainHandEvent event = new PlayerInteractAtEntityMainHandEvent(e);
            if (!event.callEvent())
                e.setCancelled(true);
        }
    }
}
