package lt.itsvaidas.ZCAPI.dto;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record CustomGUIHolder(GUICommandDTO command) implements InventoryHolder {

    @Override
    public @NotNull Inventory getInventory() {
        return null;
    }
}