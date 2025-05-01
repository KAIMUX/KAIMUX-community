package lt.itsvaidas.EconomyAPI;

import lt.itsvaidas.EconomyAPI.interfaces.IRegisterService;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class EconomyAPI {

    private static Economy economy;

    public static void set(Economy economy) {
        EconomyAPI.economy = economy;
    }

    public static boolean isRegistered() {
        return economy != null;
    }

    /**
     * Register services if Vault is present
     */
    public static void register(IRegisterService registerService) {
        if (Bukkit.getPluginManager().getPlugin("Vault") != null) {
            registerService.register();
        }
    }

    /**
     * Add money to player's balance
     */
    public static void deposit(OfflinePlayer player, double amount) {
        if (economy == null) {
            throw new IllegalStateException("Economy service is not registered");
        }
        economy.depositPlayer(player, amount);
    }

    /**
     * Remove money from player's balance
     */
    public static void withdraw(OfflinePlayer player, double amount) {
        if (economy == null) {
            throw new IllegalStateException("Economy service is not registered");
        }
        economy.withdrawPlayer(player, amount);
    }

    /**
     * Get player's balance
     */
    public static double get(OfflinePlayer player) {
        if (economy == null) {
            throw new IllegalStateException("Economy service is not registered");
        }
        return economy.getBalance(player);
    }

    public static boolean has(OfflinePlayer player, double amount) {
        if (economy == null) {
            throw new IllegalStateException("Economy service is not registered");
        }
        if (Double.isNaN(amount) || Double.isInfinite(amount)) {
            return false;
        }
        return economy.has(player, amount);
    }
}
