package lt.itsvaidas.ZCTab.configs;

import lt.itsvaidas.MessagesAPI.MSG;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

public class TabConfig {
    private static File f;
    private static FileConfiguration pd;

    public static void load(Plugin plugin) {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);

        String season = switch (month) {
            case 11, 0, 1 -> "winter";
            case 2, 3, 4 -> "spring";
            case 5, 6, 7 -> "summer";
            case 8, 9, 10 -> "autumn";
            default -> "Unknown";
        };

        File directory = new File(plugin.getDataFolder(), File.separator);
        f = new File(directory, File.separator + "tab-" + season + ".yml");
        pd = YamlConfiguration.loadConfiguration(f);
        boolean fileExists = f.exists();
        try {
            f.createNewFile();
        } catch (IOException e) {
            MSG.log(e);
        }
        try {
            pd.load(f);
            if (!fileExists) {
                pd.set("Tab Header", List.of(
                        "",
                        "&3&m    <dark_gray><bold>&m[ &r <aqua>☃ <bold><#587ffc>KAI</#7394ff><#7394ff>MUX</#587ffc></bold> <aqua>☃ <dark_gray>&m <bold>]&3&m    &r",
                        "#7394ffNetwork<gray>: #7394ff%ZC_total%<gray>/#7394ff%zcplayerlimit_networkmax%",
                        "",
                        "#7394ff%server_time_yyyy-MM-dd HH:mm%"
                ));
                 pd.set("Tab footer", List.of(
                        "",
                        "#7394ff%server_name%<gray>: #7394ff%ZC_online%<gray>/#7394ff%zcplayerlimit_max%",
                        "<green>%server_tps_1_colored%☘ &0<bold>| <red>%ping%⚡",
                        ""
                 ));
                 save();
            }
        } catch (IOException | InvalidConfigurationException e) {
            MSG.log(e);
        }
    }

    public static void save() {
        try {
            pd.save(f);
        } catch (IOException e) {
            MSG.log(e);
        }
    }

    public static List<String> getHeader() {
        return pd.getStringList("Tab Header");
    }

    public static List<String> getFooter() {
        return pd.getStringList("Tab footer");
    }
}
