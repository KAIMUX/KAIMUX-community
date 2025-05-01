package lt.itsvaidas.DataAPI;

import lt.itsvaidas.ZCAPI.tools.LOCATION;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class DataFile {

    private final File file;

    private final FileConfiguration data;

    public DataFile(@NotNull Plugin plugin, @NotNull String name, long saveInterval) {
        this(plugin, name, saveInterval, "data");
    }

    public DataFile(@NotNull Plugin plugin, @NotNull String name, long saveInterval, @NotNull String folder) {
        FileConfiguration tmpData = null;

        File directory = new File(plugin.getDataFolder(), folder + File.separator);
        directory.mkdirs();

        this.file = new File(directory, name);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            tmpData = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.data = tmpData;

        if (saveInterval > 0)
            Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this::save, saveInterval * 20 * 60, saveInterval * 20 * 60);
    }

    public void reload() {
        try {
            data.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            data.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSet(@NotNull String node) {
        return data.isSet(node);
    }

    public void set(@NotNull String node, @Nullable Object value) {
        switch (value) {
            case Location l -> data.set(node, LOCATION.toString(l));
            case int[] i -> data.set(node, String.join(" ", Arrays.stream(i).mapToObj(String::valueOf).toArray(String[]::new)));
            case null -> data.set(node, null);
            default -> data.set(node, value);
        }
    }

    public @Nullable String getString(@NotNull String node) {
        return getString(node, null);
    }

    public int getInt(@NotNull String node) {
        return getInt(node, 0);
    }

    public double getDouble(@NotNull String node) {
        return getDouble(node, 0.0);
    }

    public @NotNull List<String> getList(@NotNull String node) {
        return getList(node, new ArrayList<>());
    }

    public long getLong(String node) {
        return getLong(node, 0);
    }

    public @Nullable Location getLocation(String node) {
        return getLocation(node, null);
    }

    public boolean getBoolean(String node) {
        return getBoolean(node, false);
    }

    public int[] getIntArray(String node) {
        return getIntArray(node, new int[0]);
    }

    public @Nullable String getString(@NotNull String node, @Nullable String def) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return data.getString(node);
        } else {
            return def;
        }
    }

    public int getInt(@NotNull String node, int def) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return data.getInt(node);
        } else {
            return def;
        }
    }

    public double getDouble(@NotNull String node, double def) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return data.getDouble(node);
        } else {
            return def;
        }
    }

    public @Nullable List<String> getList(@NotNull String node, @Nullable List<String> list) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return data.getStringList(node);
        } else {
            return list;
        }
    }

    public @NotNull Set<String> getChildren(@NotNull String node) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return data.getConfigurationSection(node).getKeys(false);
        } else {
            return Set.of();
        }
    }

    public long getLong(String node, long def) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return data.getLong(node);
        } else {
            return def;
        }
    }

    public ItemStack getItemStack(String node) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        return data.getItemStack(node);
    }

    public @Nullable Location getLocation(@NotNull String node, @Nullable Location def) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return LOCATION.fromString(data.getString(node));
        } else {
            return def;
        }
    }

    public int[] getIntArray(String node, int[] def) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return Arrays.stream(data.getString(node).split(" ")).mapToInt(Integer::parseInt).toArray();
        } else {
            return def;
        }
    }

    public boolean getBoolean(String node, boolean def) {
        if (data == null)
            throw new IllegalStateException("Config file is not loaded");
        if (data.isSet(node)) {
            return data.getBoolean(node);
        } else {
            return def;
        }
    }

    public static DataFile load(@NotNull Plugin plugin, @NotNull String name, long saveInterval) {
        return new DataFile(plugin, name, saveInterval);
    }

    public static DataFile load(@NotNull Plugin plugin, @NotNull String name, long saveInterval, String folder) {
        return new DataFile(plugin, name, saveInterval, folder);
    }

    public @NotNull <T> T get(Class<T> clazz, String node) {
        return (T) data.get(node);
    }

    public Object get(String node) {
        return data.get(node);
    }
}
