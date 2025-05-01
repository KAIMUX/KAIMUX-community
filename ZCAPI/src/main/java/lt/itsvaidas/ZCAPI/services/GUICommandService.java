package lt.itsvaidas.ZCAPI.services;

import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.CommandsHolder;
import lt.itsvaidas.ZCAPI.builders.BItem;
import lt.itsvaidas.ZCAPI.dto.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class GUICommandService implements Listener {

    public static void runCommand(Player player, GUICommandDTO command) {
        CustomGUIHolder guiHolder = new CustomGUIHolder(command);
        Inventory inv = command.getType() == InventoryType.CHEST ? Bukkit.createInventory(guiHolder, command.getSize(), MSG.raw(command.getTitle())) : Bukkit.createInventory(guiHolder, command.getType(), MSG.raw(command.getTitle()));
        player.openInventory(inv);

        for (ItemDTO item : command.getItems()) {
            inv.setItem(item.getSlot(),
                    BItem.b(
                        item.getMaterial(),
                        item.title(),
                        item.lore()
                    ).build()
            );
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player player)) return;

        if (player.getGameMode() != GameMode.SPECTATOR)
            if (e.isCancelled())
                return;

        if (e.getInventory().getHolder(false) instanceof CustomGUIHolder guiHolder) {
            if (e.getCurrentItem() == null) return;
            e.setCancelled(true);

            if (e.getRawSlot() < e.getInventory().getSize()) {
                ItemDTO item = guiHolder.command().getItem(e.getRawSlot());
                if (item != null) {
                    if (item.getCommand() != null) {
                        CommandDTO command = CommandsHolder.getCommand(item.getCommand().toLowerCase());
                        if (command != null) {
                            if (command instanceof TextCommandDTO textCommandDTO) {
                                TextCommandService.runCommand(player, textCommandDTO);
                            } else if (command instanceof GUICommandDTO guiCommandDTO) {
                                GUICommandService.runCommand(player, guiCommandDTO);
                            }
                        } else {
                            Bukkit.dispatchCommand(player, item.getCommand());
                        }
                    }
                    if (item.getConsoleCommand() != null) {
                        String command = item.getConsoleCommand().replace("$player", player.getName());
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
            }
        }
    }
}
