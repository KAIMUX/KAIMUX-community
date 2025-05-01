package lt.itsvaidas.ZCAPI.commands;

import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.EventsAPI.ServerWipeEvent;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.CommandsHolder;
import lt.itsvaidas.ZCAPI.Config;
import lt.itsvaidas.ZCAPI.data.CommandsData;
import lt.itsvaidas.ZCAPI.dto.CommandDTO;
import lt.itsvaidas.ZCAPI.dto.GUICommandDTO;
import lt.itsvaidas.ZCAPI.dto.ItemDTO;
import lt.itsvaidas.ZCAPI.dto.TextCommandDTO;
import lt.itsvaidas.ZCAPI.tools.TOOLS;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Map;

@Command(
        name = "zcapi",
        description = "Main command for ZCAPI",
        permission = "zcapi.commands.zcapi"
)
public class ZCAPICommand {

    private final Plugin plugin;

    public ZCAPICommand(Plugin plugin) {
        this.plugin = plugin;
    }

    @Path(
            path = "commands reload",
            description = "Reloads all custom commands"
    )
    public void onReloadCommand(CommandSender sender) {
        CommandsHolder.clearCommands();
        CommandsData.load(plugin);
        MSG.Send.success(sender, "All commands reloaded");
    }

    @Path(
            path = "commands add text <Name>",
            description = "Add a new text command"
    )
    public void onAddGuiCommand(CommandSender sender, String command) {
        String id = TOOLS.randomString();
        TextCommandDTO textCommand = new TextCommandDTO(id, command, false, List.of(), List.of());
        CommandsData.Commands.addCommand(textCommand);
        MSG.Send.success(sender, "GUI command added with ID: " + id);
    }

    @Path(
            path = "commands add gui <Name>",
            description = "Add a new GUI command"
    )
    public void onAddTextCommand(CommandSender sender, String command) {
        String id = TOOLS.randomString();
        GUICommandDTO guiCommand = new GUICommandDTO(id, command, List.of(), false, InventoryType.CHEST, command + " Title", 9, Map.of(
                0, new ItemDTO(0, Material.STONE, 1, "help", "me $player", "test title", List.of("test", "lore"))
        ));
        CommandsData.GUI.addCommand(guiCommand);
        MSG.Send.success(sender, "Text command added with ID: " + id);
    }

    @Path(
            path = "commands list",
            description = "List all custom commands"
    )
    public void onListCommands(CommandSender sender) {
        MSG.Send.success(sender, "All commands:");
        for (CommandDTO command : CommandsHolder.getCommands())
            MSG.command(sender, command.getCommand(), command.getID());
    }

    @Path(
            path = "wipe [confirm]",
            description = "Initiate server wipe"
    )
    public void onWipe(CommandSender sender, String confirm) {
        if (confirm == null || !confirm.equalsIgnoreCase("confirm")) {
            MSG.Send.info(sender, "<red>ATENTION! <gray>This will initiate a server WIPE!");
            MSG.Send.info(sender, "<red>ATENTION! <gray>This action is NOT reversable!");
            MSG.Send.info(sender, "Make sure you really want to wipe all data from the server!");
            MSG.Send.info(sender, "To initiate a wipe type: /zcapi wipe confirm");
        } else {
            MSG.Send.success(sender, "Initiating wipe...");
            ServerWipeEvent event = new ServerWipeEvent();
            event.callEvent();
            MSG.Send.success(sender, "Server wipe has been completed. All data wiped!");
            MSG.Send.success(sender, "You can now finish up the wipe by:");
            MSG.Send.success(sender, "- Deleting/Reseting the world(s)");
            MSG.Send.success(sender, "- Transfering players to their premium UUID");
            MSG.Send.success(sender, "- Other required actions.");
        }
    }
}
