package lt.itsvaidas.MenuAPI.holders;

import lt.itsvaidas.MenuAPI.interfaces.IEnteredValueAction;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record GUIAnvilHolder(Player player, IEnteredValueAction action)  implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}
