package lt.itsvaidas.MenuAPI.holders;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public record GUIInventoryHolder(String ID, String[] args) implements InventoryHolder {

	@Override
	public @NotNull Inventory getInventory() {
		return null;
	}


}
