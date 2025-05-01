package lt.itsvaidas.ZCAPI.tools;

import org.bukkit.Chunk;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public class DATA {

    public static <J, T> @Nullable T getValue(Plugin plugin, Entity entity, String key, PersistentDataType<J, T> type) {
        return entity.getPersistentDataContainer().get(new NamespacedKey(plugin, key), type);
    }

    public static <J, T> T getValue(Plugin plugin, Entity entity, String key, PersistentDataType<J, T> type, T defaultValue) {
        return entity.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, key), type, defaultValue);
    }

    public static <J, T> void setValue(Plugin plugin, Entity entity, String key, PersistentDataType<J, T> type, T value) {
        entity.getPersistentDataContainer().set(new NamespacedKey(plugin, key), type, value);
    }

    public static boolean hasValue(Plugin plugin, Entity entity, String key, PersistentDataType<?, ?> type) {
        return entity.getPersistentDataContainer().has(new NamespacedKey(plugin, key), type);
    }

    public static void removeValue(Plugin plugin, Entity entity, String key) {
        entity.getPersistentDataContainer().remove(new NamespacedKey(plugin, key));
    }

    public static <J, T> @Nullable T getValue(Plugin plugin, ItemStack item, String key, PersistentDataType<J, T> type) {
        return item.getItemMeta().getPersistentDataContainer().get(new NamespacedKey(plugin, key), type);
    }

    public static <J, T> T getValue(Plugin plugin, ItemStack item, String key, PersistentDataType<J, T> type, T defaultValue) {
        return item.hasItemMeta() ? item.getItemMeta().getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, key), type, defaultValue) : defaultValue;
    }

    public static <J, T> void setValue(Plugin plugin, ItemStack item, String key, PersistentDataType<J, T> type, T value) {
        item.editMeta(meta -> meta.getPersistentDataContainer().set(new NamespacedKey(plugin, key), type, value));
    }

    public static boolean hasValue(Plugin plugin, ItemStack item, String key, PersistentDataType<?, ?> type) {
        return item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(new NamespacedKey(plugin, key), type);
    }

    public static <J, T> void removeValue(Plugin plugin, ItemStack item, String key) {
        item.editMeta(meta -> meta.getPersistentDataContainer().remove(new NamespacedKey(plugin, key)));
    }
    
    public static <J, T> void setValue(Plugin plugin, Block block, String key, PersistentDataType<J, T> type, T value) {
        Chunk c = block.getChunk();
        String blockKey = (block.getX() & 15) + "_" + block.getY() + "_" + (block.getZ() & 15);
        PersistentDataContainer data = c.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, blockKey), PersistentDataType.TAG_CONTAINER, c.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer());
        data.set(new NamespacedKey(plugin, key), type, value);
        c.getPersistentDataContainer().set(new NamespacedKey(plugin, blockKey), PersistentDataType.TAG_CONTAINER, data);
    }

    public static <J, T> @Nullable T getValue(Plugin plugin, Block block, String key, PersistentDataType<J, T> type) {
        Chunk c = block.getChunk();
        String blockKey = (block.getX() & 15) + "_" + block.getY() + "_" + (block.getZ() & 15);
        PersistentDataContainer data = c.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, blockKey), PersistentDataType.TAG_CONTAINER, c.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer());
        return data.get(new NamespacedKey(plugin, key), type);
    }

    public static <J, T> T getValue(Plugin plugin, Block block, String key, PersistentDataType<J, T> type, T defaultValue) {
        Chunk c = block.getChunk();
        String blockKey = (block.getX() & 15) + "_" + block.getY() + "_" + (block.getZ() & 15);
        PersistentDataContainer data = c.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, blockKey), PersistentDataType.TAG_CONTAINER, c.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer());
        return data.getOrDefault(new NamespacedKey(plugin, key), type, defaultValue);
    }

    public static boolean hasValue(Plugin plugin, Block block, String key, PersistentDataType<?, ?> type) {
        Chunk c = block.getChunk();
        String blockKey = (block.getX() & 15) + "_" + block.getY() + "_" + (block.getZ() & 15);
        PersistentDataContainer data = c.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, blockKey), PersistentDataType.TAG_CONTAINER, c.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer());
        return data.has(new NamespacedKey(plugin, key), type);
    }

    public static void removeValue(Plugin plugin, Block block, String key) {
        Chunk c = block.getChunk();
        String blockKey = (block.getX() & 15) + "_" + block.getY() + "_" + (block.getZ() & 15);
        PersistentDataContainer data = c.getPersistentDataContainer().getOrDefault(new NamespacedKey(plugin, blockKey), PersistentDataType.TAG_CONTAINER, c.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer());
        data.remove(new NamespacedKey(plugin, key));
        c.getPersistentDataContainer().set(new NamespacedKey(plugin, blockKey), PersistentDataType.TAG_CONTAINER, data);
    }

    public static void removeAll(Block block) {
        Chunk c = block.getChunk();
        String blockKey = (block.getX() & 15) + "_" + block.getY() + "_" + (block.getZ() & 15);

        Set<NamespacedKey> keys = c.getPersistentDataContainer().getKeys();
        for (NamespacedKey k : keys) {
            if (k.getKey().equals(blockKey)) {
                c.getPersistentDataContainer().remove(k);
            }
        }
    }

    public static Set<Block> getBlocks(Plugin plugin, Chunk c, String key) {
        Set<NamespacedKey> keys = c.getPersistentDataContainer().getKeys();
        Set<Block> blocks = new HashSet<>();

        for (NamespacedKey k : keys) {
            PersistentDataContainer data = c.getPersistentDataContainer().getOrDefault(k, PersistentDataType.TAG_CONTAINER, c.getPersistentDataContainer().getAdapterContext().newPersistentDataContainer());
            Set<NamespacedKey> dataKeys = data.getKeys();
            for (NamespacedKey dk : dataKeys) {
                if (dk.getKey().equals(key)) {
                    String[] blockKey = k.getKey().split("_");
                    blocks.add(c.getBlock(Integer.parseInt(blockKey[0]), Integer.parseInt(blockKey[1]), Integer.parseInt(blockKey[2])));
                    break;
                }
            }
        }

        return blocks;
    }
}
