package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.ZCAPI.CommandsHolder;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandSendEvent;

public class PlayerCommandListener implements Listener {

    @EventHandler
    public void onCommandSend(PlayerCommandSendEvent e) {
        e.getCommands().removeIf(command ->
            CommandsHolder.getCommand(command) != null && !CommandsHolder.getCommand(command).isRegisterd()
        );
    }
}
