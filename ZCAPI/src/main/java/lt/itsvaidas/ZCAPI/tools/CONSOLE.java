package lt.itsvaidas.ZCAPI.tools;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CONSOLE {

    public static void sendCommand(@NotNull String command) {
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
    }

    public static void sendCommand(@NotNull Player p, @NotNull String command) {
        Bukkit.dispatchCommand(p, command);
    }
    
}
