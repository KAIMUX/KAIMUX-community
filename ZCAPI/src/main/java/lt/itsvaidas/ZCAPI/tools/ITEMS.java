package lt.itsvaidas.ZCAPI.tools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

public class ITEMS {
	
	public static void removeItemFromInventory(@NotNull Inventory inv, Material type, int amount) {
		ItemStack[] items = inv.getContents();
		for (ItemStack item : items)
			if (item != null)
				if (item.getType().equals(type))
					if (item.getAmount() >= amount) {
						item.setAmount(item.getAmount() - amount);
						return;
					} else {
						amount -= item.getAmount();
						item.setAmount(0);
					}
	}
	
	public static void removeItemFromInventory(@NotNull Player p, Material type, int amount) {
		ItemStack[] items = p.getInventory().getContents();
		for (ItemStack item : items)
			if (item != null)
				if (item.getType().equals(type))
					if (item.getAmount() >= amount) {
						item.setAmount(item.getAmount() - amount);
						return;
					} else {
						amount -= item.getAmount();
						item.setAmount(0);
					}
	}

	public static boolean hasItemInInventory(@NotNull Inventory inv, Material type, int amount) {
		ItemStack[] items = inv.getContents();
		int sk = 0;
		for (ItemStack item : items)
			if (item != null)
				if (item.getType().equals(type))
					sk += item.getAmount();
		return sk >= amount;
	}

	public static boolean hasItemInInventory(@NotNull Player p, Material type, int amount) {
		ItemStack[] items = p.getInventory().getContents();
		int sk = 0;
		for (ItemStack item : items)
			if (item != null)
				if (item.getType().equals(type))
					sk += item.getAmount();
		return sk >= amount;
	}

	public static int countItemInInventory(@NotNull Inventory inv, Material type) {
		ItemStack[] items = inv.getContents();
		int sk = 0;
		for (ItemStack item : items)
			if (item != null)
				if (item.getType().equals(type))
					sk += item.getAmount();
		return sk;
	}

	public static int countItemInInventory(@NotNull Player p, Material type) {
		ItemStack[] items = p.getInventory().getContents();
		int sk = 0;
		for (ItemStack item : items)
			if (item != null)
				if (item.getType().equals(type))
					sk += item.getAmount();
		return sk;
	}

	public static boolean giveItemToPlayer(@NotNull Player p, ItemStack item, boolean dropIfDoesntHaveSpace) {
		int i = p.getInventory().firstEmpty();
		if (i != -1) {
			p.getInventory().addItem(item);
			return true;
		} else {
			if (dropIfDoesntHaveSpace)
				p.getWorld().dropItem(p.getLocation(), item);
			return false;
		}
	}

	public static @NotNull String toBase64(ItemStack item) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			ItemStack is = item;
			if (item == null) is = new ItemStack(Material.AIR);
			dataOutput.writeObject(is);
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}        
	}

	public static ItemStack fromBase64Item(String data) {
		ItemStack retur = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			retur = (ItemStack) dataInput.readObject();
			dataInput.close();
		} catch (Exception e) {
		}
		return retur;
	}

	public static @NotNull String toBase64(Inventory inventory) {
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
			ItemStack[] is = inventory.getContents();
			dataOutput.writeInt(is.length);
            for (ItemStack itemStack : is) {
                dataOutput.writeObject(itemStack);
            }
			dataOutput.close();
			return Base64Coder.encodeLines(outputStream.toByteArray());
		} catch (Exception e) {
			throw new IllegalStateException("Unable to save item stacks.", e);
		}
	}

	public static ItemStack[] fromBase64Inventory(String data) {
		ItemStack[] items = null;
		try {
			ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
			BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
			items = new ItemStack[dataInput.readInt()];
			for (int i = 0; i < items.length; i++) {
				items[i] = (ItemStack) dataInput.readObject();
			}
			dataInput.close();
		} catch (Exception e) {
		}
		return items;
	}
    
}
