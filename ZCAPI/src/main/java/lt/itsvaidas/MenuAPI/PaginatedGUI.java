package lt.itsvaidas.MenuAPI;

import lt.itsvaidas.MenuAPI.events.CInventoryClick;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.builders.BItem;
import lt.itsvaidas.ZCAPI.builders.BPaginatorItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class PaginatedGUI extends GUI {

	final Map<Player, Integer> currentPage = new HashMap<>();
	final int size;

	public PaginatedGUI(Plugin plugin, int size, String name) {
		super(plugin, size, name);
		this.size = size * 9;
	}

	public abstract List<BItem> getItems(Player p, String... args);

	public abstract @Nullable Map<Integer, BPaginatorItem> getButtons(Player p, String... args);

	public abstract void itemInteract(CInventoryClick e);

	@Override
	public void open(Player p, String... args) {
		currentPage.put(p, 0);
		super.open(p, args);
	}

	public void open(Player p, int page, String... args) {
		currentPage.put(p, page);
		super.open(p, args);
	}

	public void refresh(Player p, String... args) {
		open(p, getPage(p), args);
	}

	public int getPage(Player p) {
		return currentPage.getOrDefault(p, 0);
	}
	
	@Override
	public void create(Inventory inv, Player p, String... args) {
		List<BItem> items = getItems(p, args);
		Map<Integer, BPaginatorItem> buttons = getButtons(p, args);

		int page = getPage(p);
		int from = Math.max(0, page * (this.size - 9));
		int to = Math.min(items.size(), (page + 1) * (this.size - 9));

        if (to > items.size()) {
			to = items.size();
		}

		for (int i = from; i < to; i++) {
			BItem item = items.get(i);
			inv.setItem(i - from, item.build());
		}

		if (page > 0) {
			inv.setItem(this.size - 9, BItem.b(Material.REDSTONE, MSG.raw("<red><bold>⮜")).build());
		}
		if (to < items.size()) {
			inv.setItem(this.size - 8, BItem.b(Material.EMERALD, MSG.raw("<green><bold>➤")).build());
		}

		if (buttons != null)
			for (Map.Entry<Integer, BPaginatorItem> entry : buttons.entrySet()) {
				inv.setItem(entry.getKey() + this.size - 7, entry.getValue().item().build());
			}
	}

	@Override
	public void invInteract(CInventoryClick e) {
		if (e.getItem() == null) return;
		
		Player p = e.getPlayer();
		int slot = e.getSlot();

		if (slot == this.size - 9) {
			open(p, getPage(p) - 1, e.getArgs());
		} else if (slot == this.size - 8) {
			open(p, getPage(p) + 1, e.getArgs());
		} else {
			if (slot >= this.size - 7) {
				var buttons = getButtons(p, e.getArgs());
				if (buttons != null) {
					BPaginatorItem item = buttons.get(slot - this.size + 7);
					if (item != null && item.run() != null) {
						item.run().run();
					}
				}
			} else {
				itemInteract(e);
			}
		}
	}
}
