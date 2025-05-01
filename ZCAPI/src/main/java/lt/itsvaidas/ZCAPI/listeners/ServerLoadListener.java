package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.MessagesAPI.data.MessagesData;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public class ServerLoadListener implements Listener {

    private final String server;

    public ServerLoadListener(String server) {
        this.server = server;
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent e) {
        MessagesData.save();
        MessagesData.clearUnwanted(server);
    }
}
