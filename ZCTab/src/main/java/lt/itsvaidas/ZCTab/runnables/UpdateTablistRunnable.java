package lt.itsvaidas.ZCTab.runnables;

import lt.itsvaidas.ZCTab.utils.TabListUtils;
import org.bukkit.Bukkit;

public class UpdateTablistRunnable implements Runnable {

    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(TabListUtils::updateTablist);
    }
}
