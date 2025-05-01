package lt.itsvaidas.ZCAPI;

import lt.itsvaidas.DataAPI.ConfigFile;
import org.bukkit.plugin.Plugin;

public class MysqlConfig {

    private static ConfigFile pd;

    public static void load(Plugin plugin) {
        pd = ConfigFile.load(plugin, "mysql.yml");
    }

    public static String getUrl() {
        return pd.getString("Connection.url");
    }

    public static String getDatabase() {
        return pd.getString("Connection.database");
    }

    public static String getUsername() {
        return pd.getString("Connection.username");
    }

    public static String getPassword() {
        return pd.getString("Connection.password");
    }
}
