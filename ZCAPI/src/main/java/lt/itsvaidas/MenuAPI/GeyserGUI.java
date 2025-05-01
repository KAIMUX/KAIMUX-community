package lt.itsvaidas.MenuAPI;

import lt.itsvaidas.MenuAPI.geyser.interfaces.IForm;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.geysermc.floodgate.api.FloodgateApi;

public abstract class GeyserGUI extends GUI {

    private final Plugin plugin;
    private final FloodgateApi floodgateApi = FloodgateApi.getInstance();

    public GeyserGUI(Plugin plugin, int size, String name) {
        super(plugin, size, name);

        this.plugin = plugin;
    }

    @Override
    public void open(Player p, String... args) {
        if (floodgateApi.isFloodgatePlayer(p.getUniqueId())) {
            p.closeInventory();
            Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
                floodgateApi.sendForm(p.getUniqueId(), generateForm(p, args).build(floodgateApi, p));
            });
        } else {
            super.open(p, args);
        }
    }

    public abstract IForm generateForm(Player player, String... args);
}
