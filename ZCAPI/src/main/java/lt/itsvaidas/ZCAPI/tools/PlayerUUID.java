package lt.itsvaidas.ZCAPI.tools;

import lt.itsvaidas.ZCAPI.data.PlayerUUIDData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerUUID {

    public static @Nullable UUID getUUID(String username) {
        Player player = Bukkit.getOnlinePlayers().stream().filter(p -> p.getName().equalsIgnoreCase(username)).findFirst().orElse(null);
        return player == null ? PlayerUUIDData.getPlayerUUID(username) : player.getUniqueId();
    }

    public static @Nullable String getUsername(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        return player == null ? PlayerUUIDData.getPlayerUsername(uuid) : player.getName();
    }
}
