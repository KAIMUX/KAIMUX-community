package lt.itsvaidas.MenuAPI;

import lt.itsvaidas.MenuAPI.geyser.elements.Input;
import lt.itsvaidas.MenuAPI.geyser.forms.CustomForm;
import lt.itsvaidas.MenuAPI.interfaces.IEnteredValueAction;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.MessagesAPI.MessagesAPI;
import lt.itsvaidas.ZCAPI.Main;
import lt.itsvaidas.ZCAPI.builders.BItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.view.AnvilView;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.HashMap;
import java.util.Map;

public class EnterValueGUI implements Listener {

    private static final FloodgateApi floodgateApi = FloodgateApi.getInstance();
    private static final Map<Player, IEnteredValueAction> actions = new HashMap<>();

    public static void open(Player player, Enum<?> title, Component placeholder, IEnteredValueAction action) {
        if (floodgateApi.isFloodgatePlayer(player.getUniqueId())) {
            player.closeInventory();

            String inputTitle = MessagesAPI.getString(player, title);
            String inputPlaceholder = MSG.plain(placeholder);
            CustomForm form = CustomForm.of(
                MessagesAPI.getString(player, title),
                (values) -> {
                    String value = (String) values.get("value");
                    Component response = action.onEnteredValue(value);

                    if (response != null) {
                        open(player, title, response, action);
                    }
                },
                () -> true,
                Input.of("value", inputTitle, inputPlaceholder)
            );

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () ->
            floodgateApi.sendForm(player.getUniqueId(), form.build(floodgateApi, player)), 20L);
        } else {
            AnvilView view = MenuType.ANVIL.create(player, MSG.rawLine(player, title));
            view.setItem(0, BItem.b(Material.PAPER, placeholder).build());
            view.setRepairItemCountCost(0);
            view.setRepairCost(0);
            view.setMaximumRepairCost(0);
            player.openInventory(view);
            actions.put(player, action);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClickEvent(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;
        if (!actions.containsKey(player)) return;

        e.setCancelled(true);
        if (e.getView() instanceof AnvilView anvil && e.getRawSlot() == 2 && e.getCurrentItem() != null) {
            anvil.setRepairCost(0);
            anvil.setRepairItemCountCost(0);

            if (anvil.getRenameText() == null || anvil.getRenameText().isEmpty()) {
                return;
            }

            Component response = actions.get(player).onEnteredValue(anvil.getRenameText());
            if (response != null) {
                e.getCurrentItem().editMeta(meta -> meta.displayName(response.color(NamedTextColor.RED)));
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent e) {
        if (!(e.getPlayer() instanceof Player player)) return;
        if (!actions.containsKey(player)) return;
        e.getInventory().clear();
        actions.remove(player);
    }
}
