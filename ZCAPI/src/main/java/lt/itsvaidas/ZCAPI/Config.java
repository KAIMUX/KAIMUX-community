package lt.itsvaidas.ZCAPI;

import lt.itsvaidas.DataAPI.ConfigFile;
import org.bukkit.plugin.Plugin;

import java.util.Set;

public class Config {

    private static ConfigFile config;

    public static void load(Plugin plugin) {
        config = ConfigFile.load(plugin, "config.yml");
    }

    public static String getServer() {
        return config.getString("Server");
    }

    public static String getLanguagePath() {
        return config.getString("Language path");
    }

    public static Set<String> getStaticMessages() {
        return config.getChildren("Static messages");
    }

    public static String getMessage(String key) {
        return config.getString("Static messages." + key);
    }
}
