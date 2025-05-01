package lt.itsvaidas.ZCAPI.abstracts;

import lt.itsvaidas.MessagesAPI.MSG;
import net.kyori.adventure.bossbar.BossBar;
import org.bukkit.entity.Player;

public class BossBarMessage {

    private final BossBar bossBar;

    public BossBarMessage(String message, BossBar.Color color, BossBar.Overlay overlay) {
        this.bossBar = BossBar.bossBar(MSG.raw(message), 1f, color, overlay);
    }

    public BossBarMessage progress(float progress) {
        bossBar.progress(progress);
        return this;
    }

    public BossBarMessage title(String title) {
        bossBar.name(MSG.raw(title));
        return this;
    }

    public void remove() {
        bossBar.viewers().forEach(p -> {
            if (p instanceof Player) {
                bossBar.removeViewer((Player) p);
            }
        });
    }

    public void add(Player player) {
        bossBar.addViewer(player);
    }

    public void remove(Player player) {
        bossBar.removeViewer(player);
    }
}
