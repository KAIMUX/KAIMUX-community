package lt.itsvaidas.ZCTab.configs;

import lt.itsvaidas.ZCTab.enums.SortType;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Set;

public class Config {

    private static FileConfiguration config;

    public static void setConfig(FileConfiguration config) {
        Config.config = config;
    }

    public static class Sorting {
        public static Set<String> getGroups() {
            return config.getConfigurationSection("Sorting").getKeys(false);
        }

        public static int getPriority(String group) {
            return config.getInt("Sorting." + group + ".priority");
        }

        public static String getPlaceholder(String group) {
            return config.getString("Sorting." + group + ".placeholder");
        }

        public static SortType getSortType(String group) {
            return SortType.valueOf(config.getString("Sorting." + group + ".type"));
        }
    }

    public static class NameTag {
        public static class Tab {
            public static String prefix() {
                return config.getString("Name tag.tab.prefix");
            }
            public static String suffix() {
                return config.getString("Name tag.tab.suffix");
            }
        }
    }
}
