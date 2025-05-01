package lt.itsvaidas.ZCAPI.listeners;

import lt.itsvaidas.ZCAPI.tools.DATA;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class RemovePDCFromBlockListener implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent e) {
        DATA.removeAll(e.getBlock());
    }
}
