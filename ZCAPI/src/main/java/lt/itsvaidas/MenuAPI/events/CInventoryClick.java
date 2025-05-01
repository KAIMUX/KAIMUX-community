package lt.itsvaidas.MenuAPI.events;

import lt.itsvaidas.ZCAPI.tools.DATA;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.intellij.lang.annotations.Subst;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CInventoryClick {

	private final Plugin plugin;
	private final int slot;
	private final Inventory inv;
	private final Player p;
	private final ClickType type;
	private final String[] args;
	private final @Nullable ItemStack item;
	private boolean cancelled;

	public CInventoryClick(Plugin plugin, @Nullable ItemStack item, int slot, Inventory inv, Player p, ClickType type, String[] args, boolean cancelled) {
		this.plugin = plugin;
		this.slot = slot;
		this.inv = inv;
		this.p = p;
		this.type = type;
		this.args = args;
		this.item = item;
		this.cancelled = cancelled;
	}
	
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}
	
	public boolean isCancelled() {
		return cancelled;
	}
	
	public @Nullable ItemStack getItem() {
		return item;
	}
	
	public int getSlot() {
		return slot;
	}
	
	public Inventory getInventory() {
		return inv;
	}
	
	public ItemStack[] getItems() {
		return inv.getContents();
	}
	
	public void setItems(ItemStack[] items) {
		inv.setContents(items);
	}
	
	public Player getPlayer() {
		return p;
	}
	
	public ClickType getClickType() {
		return type;
	}
	
	public String[] getArgs() {
		return args;
	}

	@Subst("")
    public @Nullable String getValue(@NotNull String key) {
		if (this.item != null) {
			return DATA.getValue(this.plugin, this.item, key, PersistentDataType.STRING);
		}
		return null;
	}
}
