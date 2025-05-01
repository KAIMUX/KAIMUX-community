package lt.itsvaidas.EconomyAPI.listeners;

import lt.itsvaidas.EconomyAPI.EconomyAPI;
import lt.itsvaidas.MessagesAPI.MSG;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServiceRegisterEvent;

import java.util.logging.Level;

public class EconomyServiceRegisterListener implements Listener {

    @EventHandler
    public void onServiceRegister(ServiceRegisterEvent e) {
        if (e.getProvider().getService() == Economy.class) {
            Economy economy = (Economy) e.getProvider().getProvider();
            EconomyAPI.set(economy);
            MSG.log(Level.INFO, "[ZCAPI] [ServiceRegisterEvent] Registered economy service from " + economy.getName());
        }
    }
}
