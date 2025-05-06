package lt.itsvaidas.ZCHub;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import lt.itsvaidas.MenuAPI.GeyserGUI;
import lt.itsvaidas.MenuAPI.events.CInventoryClick;
import lt.itsvaidas.MenuAPI.geyser.elements.Button;
import lt.itsvaidas.MenuAPI.geyser.forms.SimpleForm;
import lt.itsvaidas.MenuAPI.geyser.interfaces.IForm;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.builders.BItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class HubGUI extends GeyserGUI {

    Plugin plugin;

    public HubGUI(Plugin plugin) {
        super(plugin, 5, "KAIMUX.COM");

        this.plugin = plugin;
    }

    @Override
    public void create(Inventory inv, Player p, String... args) {
        for (String slot : Config.getSlots()) {
            String name = Config.getName(slot);
            List<String> lore = Config.getLore(slot);
            Material material = Config.getMaterial(slot);

            inv.setItem(Integer.parseInt(slot),
                    BItem.b(
                            material,
                            name,
                            lore
                    ).build());
        }
    }

    @Override
    public void invInteract(CInventoryClick e) {
        if (e.getItem() == null) return;

        int slot = e.getSlot();
        if (slot == 49) {
            e.getPlayer().kick(MSG.raw("<red>You have left the server."));
            return;
        }
        String server = Config.getServer(String.valueOf(slot));

        sendPlayerToServer(e.getPlayer(), server);
    }

    @Override
    public IForm generateForm(Player player, String... args) {
        Button[] servers = new Button[Config.getSlots().size()];

        int i = 0;
        for (String slot : Config.getSlots()) {
            String name = Config.getName(slot);
            String plainName = MSG.strip(name);
            String server = Config.getServer(slot);
            servers[i++] = Button.of(plainName, (p) -> sendPlayerToServer(p, server));
        }

        return SimpleForm.of(
                "Server Selector",
                () -> true,
                servers
        );
    }

    private void sendPlayerToServer(Player player, String server) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF("Connect");
        out.writeUTF(server);

        player.sendPluginMessage(plugin, "BungeeCord", out.toByteArray());
    }
}
