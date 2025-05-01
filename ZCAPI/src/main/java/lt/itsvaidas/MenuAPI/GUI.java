package lt.itsvaidas.MenuAPI;

import lt.itsvaidas.MenuAPI.events.CInventoryClick;
import lt.itsvaidas.MenuAPI.events.CInventoryClose;
import lt.itsvaidas.MenuAPI.holders.GUIInventoryHolder;
import lt.itsvaidas.MessagesAPI.MSG;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent.Builder;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.apache.commons.lang3.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class GUI implements Listener {

	private final Plugin plugin;
	private final Component inventoryName;
	private final InventoryType type;
	private final int size;
	private final String ID;
	private boolean allowMoveItems = false;

	public GUI(Plugin plugin, InventoryType type, String name, boolean allowMoveItems) {
		this(plugin, type, name);
		this.allowMoveItems = allowMoveItems;
	}

	public GUI(Plugin plugin, int size, String name, boolean allowMoveItems) {
		this(plugin, size, name);
		this.allowMoveItems = allowMoveItems;
	}
	
	public GUI(Plugin plugin, InventoryType type, String name) {
		this.plugin = plugin;
		this.type = type;
		this.size = 9;
		this.inventoryName = convertInventoryName(name);
		this.ID = this.getRandomID();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	public GUI(Plugin plugin, int size, String name) {
		this.plugin = plugin;
		this.type = InventoryType.CHEST;
		this.size = size*9;
		this.inventoryName = convertInventoryName(name);
		this.ID = this.getRandomID();
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	private Component convertInventoryName(String inventoryName) {
        String[] words = inventoryName.split(" \\| ");

		@NotNull Builder builder = Component.text();
		
		builder.append(Component.text(words[0]).color(TextColor.fromHexString("#ff0000")).decoration(TextDecoration.BOLD, true));

		if (words.length > 1) {
			builder.append(Component.text(" | ").color(NamedTextColor.DARK_GRAY));
			builder.append(Component.text(words[1]).color(TextColor.fromHexString("#ff6a00")));
		}
		if (words.length > 2) {
			builder.append(Component.text(" | ").color(NamedTextColor.DARK_GRAY));
			builder.append(Component.text(words[2]).color(TextColor.fromHexString("#ffbf00")));
		}

		return builder.build();
	}
	
	private String getRandomID() {
	    return RandomStringUtils.random(10, true, false);
	}
	
	public void open(Player p, String... args) {
		GUIInventoryHolder inventoryHolder = new GUIInventoryHolder(ID, args);
		Inventory inv;
		if (this.type == InventoryType.CHEST)
			inv = Bukkit.createInventory(inventoryHolder, this.size, this.inventoryName);
		else
			inv = Bukkit.createInventory(inventoryHolder, this.type, this.inventoryName);
		Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> create(inv, p, args));
		p.openInventory(inv);
	}
	
	public abstract void create(Inventory inv, Player p, String... args);
	
	public abstract void invInteract(CInventoryClick e);
	
	public void invClose(CInventoryClose e) {}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onInventoryClickEvent(InventoryClickEvent e) {
		if (!(e.getWhoClicked() instanceof Player player)) return;
		
		if (player.getGameMode() != GameMode.SPECTATOR)
			if (e.isCancelled())
				return;
			
		if (!(e.getInventory().getHolder(false) instanceof GUIInventoryHolder inventoryHolder)) return;
		if (!inventoryHolder.ID().equals(this.ID)) return;

		e.setCancelled(!allowMoveItems);

		if (e.getAction() == InventoryAction.HOTBAR_SWAP) {
			e.setCancelled(true);
			return;
		}

		if (e.getRawSlot() < e.getInventory().getSize()) {
			CInventoryClick click = new CInventoryClick(
					this.plugin,
					e.getCurrentItem(),
					e.getSlot(),
					e.getView().getTopInventory(),
					player,
					e.getClick(),
					inventoryHolder.args(),
					!allowMoveItems);
			invInteract(click);
			if (click.isCancelled())
				e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e) {
		if (!(e.getPlayer() instanceof Player player)) return;
		if (!(e.getInventory().getHolder(false) instanceof GUIInventoryHolder inventoryHolder)) return;
		if (!inventoryHolder.ID().equals(this.ID)) return;

		CInventoryClose close = new CInventoryClose(player, e.getReason(), e.getInventory(), inventoryHolder.args());
		invClose(close);

		if (close.isCancalled()) {
			Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, () -> player.openInventory(e.getInventory()), 2L);
		}
	}
}
