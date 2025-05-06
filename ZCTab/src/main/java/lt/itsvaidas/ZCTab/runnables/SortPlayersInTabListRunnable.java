package lt.itsvaidas.ZCTab.runnables;

import lt.itsvaidas.ZCTab.configs.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class SortPlayersInTabListRunnable implements Runnable {

    @Override
    public void run() {
        AtomicInteger i = new AtomicInteger(0);
        HashMap<Player, String> sortedPlayers = Bukkit.getOnlinePlayers().stream().sorted((p1, p2) -> {
            for (String group : Config.Sorting.getGroups().stream().sorted(Comparator.comparingInt(Config.Sorting::getPriority)).toList()) {
                switch (Config.Sorting.getSortType(group)) {
                    case A_TO_Z -> {
                        String placeholder = Config.Sorting.getPlaceholder(group);
                        String p1Value = PlaceholderAPI.setPlaceholders(p1, placeholder);
                        String p2Value = PlaceholderAPI.setPlaceholders(p2, placeholder);
                        int compared = p1Value.compareTo(p2Value);
                        if (compared != 0) return compared;
                    }
                    case Z_TO_A -> {
                        String placeholder = Config.Sorting.getPlaceholder(group);
                        String p1Value = PlaceholderAPI.setPlaceholders(p1, placeholder);
                        String p2Value = PlaceholderAPI.setPlaceholders(p2, placeholder);
                        int compared = p2Value.compareTo(p1Value);
                        if (compared != 0) return compared;
                    }
                    case EXIST -> {
                        String placeholder = Config.Sorting.getPlaceholder(group);
                        String p1Value = PlaceholderAPI.setPlaceholders(p1, placeholder);
                        String p2Value = PlaceholderAPI.setPlaceholders(p2, placeholder);
                        if (p1Value.isEmpty() && p2Value.isEmpty()) continue;
                        if (p1Value.equalsIgnoreCase(p2Value)) continue;
                        if (p1Value.isEmpty()) return -1;
                        if (p2Value.isEmpty()) return 1;
                    }
                }
            }
            return 0;
        }).reduce(new HashMap<>(), (map, p) -> {
            map.put(p, String.format("%04d", i.getAndIncrement()));
            return map;
        }, (map1, map2) -> {
            map1.putAll(map2);
            return map1;
        });

        sortedPlayers.forEach((p, name) -> {
            Team team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(name);
            if (team == null) {
                team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(name);
                team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            }
            if (team.hasEntity(p)) return;
            team.removeEntries(team.getEntries());
            team.addPlayer(p);
        });
    }
}
