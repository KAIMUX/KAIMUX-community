package lt.itsvaidas.MenuAPI.events;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public class CInventoryClose {
	
	private final Player player;
	private final InventoryCloseEvent.Reason reason;
	private final Inventory inventory;
	private final String[] args;
	private boolean isCancalled = false;
	
	public CInventoryClose(@NotNull Player player, @NotNull InventoryCloseEvent.Reason reason, @NotNull Inventory inventory, String[] args) {
		this.player = player;
		this.reason = reason;
		this.inventory = inventory;
		this.args = args;
	}
	
	public Player getPlayer() {
		return player;
	}

	public InventoryCloseEvent.Reason getReason() {
		return reason;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public String[] getArgs() {
		return args;
	}
	
	public boolean isCancalled() {
		return isCancalled;
	}
	
	public void setCancalled(boolean isCancalled) {
		this.isCancalled = isCancalled;
	}
}
