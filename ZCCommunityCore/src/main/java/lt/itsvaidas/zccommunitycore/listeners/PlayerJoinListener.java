package lt.itsvaidas.zccommunitycore.listeners;

import lt.itsvaidas.ZCAPI.tools.LOCATION;
import lt.itsvaidas.ZCAPI.tools.TOOLS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (!e.getPlayer().hasPlayedBefore()) {
            int x = TOOLS.randomInt(-15000, 15000);
            int z = TOOLS.randomInt(-15000, 15000);

            Location spawn = LOCATION.getTopYLocation(new Location(Bukkit.getWorld("world"), x, 256, z), true);
            e.getPlayer().teleport(spawn);
        }
    }
}
