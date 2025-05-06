package lt.itsvaidas.ZCHub;

import lt.itsvaidas.DataAPI.ConfigFile;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Set;

public class Config {

    private static ConfigFile pd;

    public static void load(Plugin pl) {
        pd = ConfigFile.load(pl, "config.yml");
	}

    public static Set<String> getSlots() {
        return pd.getChildren("Hub Inventory");
    }

    public static String getName(String slot) {
        return pd.getString("Hub Inventory."+slot+".Name");
    }

    public static List<String> getLore(String slot) {
        return pd.getList("Hub Inventory."+slot+".Lore", List.of());
    }

    public static Material getMaterial(String slot) {
        return Material.valueOf(pd.getString("Hub Inventory."+slot+".Material"));
    }

    public static String getServer(String slot) {
        return pd.getString("Hub Inventory."+slot+".Server");
    }
}
