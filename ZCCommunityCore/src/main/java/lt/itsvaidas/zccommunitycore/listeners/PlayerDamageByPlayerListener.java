package lt.itsvaidas.zccommunitycore.listeners;

import lt.itsvaidas.EventsAPI.PlayerDamageByPlayerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerDamageByPlayerListener implements Listener {

    @EventHandler
    public void onPlayerDamageByPlayer(PlayerDamageByPlayerEvent e) {
        if (e.getVictim().getFirstPlayed() > System.currentTimeMillis() - 3600000L) {
            e.setCancelled(true);
        }
    }
}
