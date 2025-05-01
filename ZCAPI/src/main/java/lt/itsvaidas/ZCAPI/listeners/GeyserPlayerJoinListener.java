package lt.itsvaidas.ZCAPI.listeners;


import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.GlobalMessages;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.geysermc.floodgate.api.FloodgateApi;

public class GeyserPlayerJoinListener implements Listener {

    private final FloodgateApi floodgateApi = FloodgateApi.getInstance();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (floodgateApi.isFloodgatePlayer(e.getPlayer().getUniqueId())) {
            if (!floodgateApi.getPlayer(e.getPlayer().getUniqueId()).isLinked()) {
                MSG.Send.info(e.getPlayer(), GlobalMessages.INFO__GEYSER_NOTICE_GLOBAL_LINK);
            }
        }
    }
}
