package lt.itsvaidas.DataAPI;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ConfigFile {

    private final File file;

    private final FileConfiguration config;
    private final FileConfiguration fallback;

    public ConfigFile(@NotNull Plugin plugin, @NotNull String folder, @NotNull String name) {
        FileConfiguration tmpConfig = null;
        FileConfiguration tmpFallback = null;

        File directory = new File(plugin.getDataFolder(), folder + File.separator);
        directory.mkdirs();

        this.file = new File(directory, name);

        try {
            if (!file.exists()) {
                file.createNewFile();
            }

            tmpConfig = YamlConfiguration.loadConfiguration(file);

            InputStream localResource = plugin.getResource(folder.isEmpty() ? name : folder + "/" + name);
            if (localResource != null) {
                tmpFallback = YamlConfiguration.loadConfiguration(new InputStreamReader(localResource));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        config = tmpConfig;
        fallback = tmpFallback;
    }

    public void reload() {
        try {
            config.load(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isSet(String node) {
        return config.isSet(node);
    }

    public @Nullable Long getLong(@NotNull String node) {
        return getLong(node, null);
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

    public @Nullable List<String> getList(@NotNull String node) {
        return getList(node, null);
    }

    public boolean getBoolean(String node) {
        return getBoolean(node, false);
    }

    public @Nullable String getString(@NotNull String node, @Nullable String def) {
        if (config == null || fallback == null)
            throw new IllegalStateException("Config file is not loaded");
        if (config.isSet(node)) {
            return config.getString(node);
        } else if (fallback.isSet(node)) {
            return fallback.getString(node);
        } else {
            return def;
        }
    }

    public int getInt(@NotNull String node, int def) {
        if (config == null || fallback == null)
            throw new IllegalStateException("Config file is not loaded");
        if (config.isSet(node)) {
            return config.getInt(node);
        } else if (fallback.isSet(node)) {
            return fallback.getInt(node);
        } else {
            return def;
        }
    }

    public double getDouble(@NotNull String node, double def) {
        if (config == null || fallback == null)
            throw new IllegalStateException("Config file is not loaded");
        if (config.isSet(node)) {
            return config.getDouble(node);
        } else if (fallback.isSet(node)) {
            return fallback.getDouble(node);
        } else {
            return def;
        }
    }

    public @Nullable List<String> getList(@NotNull String node, @Nullable List<String> list) {
        if (config == null || fallback == null)
            throw new IllegalStateException("Config file is not loaded");
        if (config.isSet(node)) {
            return config.getStringList(node);
        } else if (fallback.isSet(node)) {
            return fallback.getStringList(node);
        } else {
            return list;
        }
    }

    public @NotNull Set<String> getChildren(@NotNull String node) {
        if (config == null || fallback == null)
            throw new IllegalStateException("Config file is not loaded");
        Set<String> all = new HashSet<>();
        if (config.isSet(node)) {
            all.addAll(config.getConfigurationSection(node).getKeys(false));
        } else if (fallback.isSet(node)) {
            all.addAll(fallback.getConfigurationSection(node).getKeys(false));
        }
        return all;
    }

    public boolean getBoolean(@NotNull String node, boolean def) {
        if (config == null || fallback == null)
            throw new IllegalStateException("Config file is not loaded");
        if (config.isSet(node)) {
            return config.getBoolean(node);
        } else if (fallback.isSet(node)) {
            return fallback.getBoolean(node);
        } else {
            return def;
        }
    }

    public Long getLong(String node, Long def) {
        if (config == null || fallback == null)
            throw new IllegalStateException("Config file is not loaded");
        if (config.isSet(node)) {
            return config.getLong(node);
        } else if (fallback.isSet(node)) {
            return fallback.getLong(node);
        } else {
            return def;
        }
    }

    public static ConfigFile load(@NotNull Plugin plugin, @NotNull String folder, @NotNull String name) {
        return new ConfigFile(plugin, folder, name);
    }

    public static ConfigFile load(@NotNull Plugin plugin, @NotNull String name) {
        return ConfigFile.load(plugin, "", name);
    }
}
