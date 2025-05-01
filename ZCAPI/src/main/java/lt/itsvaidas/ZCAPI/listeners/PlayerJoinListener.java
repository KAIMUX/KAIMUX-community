package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.ZCAPI.data.PlayerUUIDData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        PlayerUUIDData.setPlayerUUID(e.getPlayer().getName(), e.getPlayer().getUniqueId());
    }
}
