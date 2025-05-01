package lt.itsvaidas.ZCAPI.builders;

import lt.itsvaidas.MessagesAPI.MSG;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BItem {

    private @Nullable Plugin plugin;
    private @NotNull Material material;
    private @Nullable Component name;
    private @Nullable List<Component> lore;
    private int amount = 1;
    private @NotNull final Map<String, String> pdc = new HashMap<>();
    private ItemFlag[] flags;
    private int maxStackSize = -1;
    private UUID playerHead;

    public BItem(@NotNull Material material, @Nullable Component name, @Nullable List<Component> lore) {
        this.material = material;
        this.name = name;
        this.lore = lore;
    }

    public BItem material(@NotNull Material material) {
        this.material = material;
        return this;
    }

    public BItem name(@Nullable Component name) {
        this.name = name;
        return this;
    }

    public BItem lore(@Nullable List<Component> lore) {
        this.lore = lore;
        return this;
    }

    public BItem key(Plugin plugin, @NotNull String key, @NotNull String value) {
        this.plugin = plugin;
        pdc.put(key, value);
        return this;
    }

    public BItem maxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;
        return this;
    }

    public BItem amount(int amount) {
        this.amount = amount;
        return this;
    }

    public BItem flags(ItemFlag... flags) {
        this.flags = flags;
        return this;
    }

    public BItem playerHead(UUID uuid) {
        this.playerHead = uuid;
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);

        item.editMeta(meta -> {
            if (name != null)
                meta.displayName(name);

            if (lore != null)
                meta.lore(lore);

            if (flags != null)
                meta.addItemFlags(flags);

            if (maxStackSize != -1)
                meta.setMaxStackSize(maxStackSize);

            if (playerHead != null && meta instanceof SkullMeta skull) {
                skull.setOwningPlayer(Bukkit.getOfflinePlayer(playerHead));
            }

            this.additionalMeta(meta);

            for (Map.Entry<String, String> entry : pdc.entrySet())
                meta.getPersistentDataContainer()
                        .set(new NamespacedKey(this.plugin, entry.getKey()), PersistentDataType.STRING, entry.getValue());
        });

        return item;
    }

    public void additionalMeta(ItemMeta meta) {}

    public static BItem b(@NotNull Material material) {
        return new BItem(material, null, null);
    }

    public static BItem b(@NotNull Material material, @Nullable String name) {
        return new BItem(material, MSG.raw(name), null);
    }

    public static BItem b(@NotNull Material material, @Nullable String name, @Nullable List<String> lore) {
        return new BItem(material, MSG.raw(name), MSG.raw(lore));
    }

    public static BItem b(@NotNull Material material, @Nullable Component name) {
        return new BItem(material, name, null);
    }

    public static BItem b(@NotNull Material material, @Nullable Component name, @Nullable List<Component> lore) {
        return new BItem(material, name, lore);
    }

    public static BItem b(@NotNull Player p, @NotNull Material material, @Nullable Component name, Enum<?> lore, Object... args) {
        return new BItem(material, name, MSG.rawList(p, lore, args));
    }

    public static BItem b(ItemStack item) {
        return new BItem(item.getType(), item.displayName(), item.lore()).amount(item.getAmount());
    }

    public static BItem b(@NotNull Player p, @NotNull Material material, @Nullable Enum<?> name, Object... args) {
        return new BItem(material, MSG.rawLine(p, name, args), null);
    }

    public static BItem b(@NotNull Player p, @NotNull Material material, @Nullable Enum<?> name, @Nullable List<String> lore) {
        return new BItem(material, MSG.rawLine(p, name), MSG.raw(lore));
    }

    public static BItem b(@NotNull Player p, @NotNull Material material, @Nullable Enum<?> name, @Nullable Enum<?> lore, Object... args) {
        return new BItem(material, MSG.rawLine(p, name, args), MSG.rawList(p, lore, args));
    }

    public static BItem b(@NotNull Player p, @NotNull Material material, @Nullable Enum<?> name, @Nullable List<Component> lore, Object... args) {
        return new BItem(material, MSG.rawLine(p, name, args), lore);
    }

    public static BItem b(@NotNull Player p, @NotNull Material material, @Nullable String name, @Nullable Enum<?> lore, Object... args) {
        return new BItem(material, MSG.raw(name), MSG.rawList(p, lore, args));
    }

    public static BItem b(@NotNull Player p, @NotNull ItemStack item, @Nullable Enum<?> name, @Nullable Enum<?> lore, Object... args) {
        return new BItem(item.getType(), MSG.rawLine(p, name, args), MSG.rawList(p, lore, args)).amount(item.getAmount());
    }
}

