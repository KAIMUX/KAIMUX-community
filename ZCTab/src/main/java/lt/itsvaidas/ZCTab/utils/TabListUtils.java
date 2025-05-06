package lt.itsvaidas.ZCTab.utils;

import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCTab.configs.Config;
import lt.itsvaidas.ZCTab.configs.TabConfig;
import me.clip.placeholderapi.PlaceholderAPI;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.List;

public class TabListUtils {

    public static void updateTablist(Player player) {
        List<String> header = TabConfig.getHeader();
        List<String> footer = TabConfig.getFooter();

        Component headerComponent = header.stream().map(line -> PlaceholderAPI.setPlaceholders(player, line)).map(MSG::raw).reduce(Component.empty(), (cm, cc) -> cm.append(Component.newline()).append(cc));
        Component footerComponent = footer.stream().map(line -> PlaceholderAPI.setPlaceholders(player, line)).map(MSG::raw).reduce(Component.empty(), (cm, cc) -> cm.append(Component.newline()).append(cc));

        player.sendPlayerListHeaderAndFooter(headerComponent, footerComponent);

        String tabPrefix = Config.NameTag.Tab.prefix();
        String tabSuffix = Config.NameTag.Tab.suffix();
        String finalPrefix = PlaceholderAPI.setPlaceholders(player, tabPrefix) + player.getName();
        String finalSuffix = PlaceholderAPI.setPlaceholders(player, tabSuffix);

        player.playerListName(MSG.raw(finalPrefix + " " + finalSuffix));
    }
}
