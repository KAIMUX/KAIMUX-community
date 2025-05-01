package lt.itsvaidas.ZCAPI.data;

import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.MessagesAPI.MessagesAPI;
import lt.itsvaidas.ZCAPI.CommandsHolder;
import lt.itsvaidas.ZCAPI.dto.GUICommandDTO;
import lt.itsvaidas.ZCAPI.dto.ItemDTO;
import lt.itsvaidas.ZCAPI.dto.TextCommandDTO;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandsData {

	public static void load(Plugin plugin) {
		Commands.load(plugin);
		GUI.load(plugin);
	}

	public static class Commands {

		private static Plugin plugin;

	    public static void load(Plugin plugin) {
			Commands.plugin = plugin;
			File directory = new File(plugin.getDataFolder() + "/commands/text", File.separator);

	    	directory.mkdirs();
	    	for (File f : directory.listFiles()) {
				if (f.getName().contains(".yml")) {
					FileConfiguration pd = YamlConfiguration.loadConfiguration(f);
					for (String ID : pd.getConfigurationSection("").getKeys(false)) {
						String command = pd.getString(ID + ".command");
						List<String> aliases = pd.isSet(ID + ".aliases") ? pd.getStringList(ID + ".aliases") : List.of();
						boolean register = pd.getBoolean(ID + ".register");
                        MessagesAPI.register(true, "ZCAPI", "Commands.Text." + ID, List.of("Pakeisti per ZCMessages"));
						CommandsHolder.addCommand(new TextCommandDTO(ID, command, register, aliases));
					}
				}
			}
	    }

		public static void addCommand(TextCommandDTO textCommand) {
			File f = new File(Commands.plugin.getDataFolder() + "/commands/text", File.separator + textCommand.getID() + ".yml");
			FileConfiguration pd = YamlConfiguration.loadConfiguration(f);
			pd.set(textCommand.getID() + ".command", textCommand.getCommand());
			pd.set(textCommand.getID() + ".register", textCommand.isRegisterd());
			try {
				pd.save(f);
			} catch (IOException e) {
				MSG.log(e);
			}
		}
	}

	public static class GUI {

		private static Plugin plugin;

	    public static void load(Plugin plugin) {
			GUI.plugin = plugin;
			File directory = new File(plugin.getDataFolder() + "/commands/gui", File.separator);

	    	directory.mkdirs();
	    	for (File f : directory.listFiles()) {
				if (f.getName().contains(".yml")) {
					FileConfiguration pd = YamlConfiguration.loadConfiguration(f);
					for (String ID : pd.getConfigurationSection("").getKeys(false)) {
						String command = pd.getString(ID + ".command");
						List<String> aliases = pd.isSet(ID + ".aliases") ? pd.getStringList(ID + ".aliases") : List.of();
						boolean register = pd.getBoolean(ID + ".register");
						InventoryType type = InventoryType.valueOf(pd.getString(ID + ".type"));
						String title = pd.getString(ID + ".title");
						int size = pd.getInt(ID + ".size");

						Map<Integer, ItemDTO> items = new HashMap<>();
						for (String slotString : pd.getConfigurationSection(ID + ".items").getKeys(false)) {
							int slot = Integer.parseInt(slotString);
							ItemDTO item = new ItemDTO(
								slot,
								Material.valueOf(pd.getString(ID + ".items." + slot + ".material", "STONE")),
								pd.getInt(ID + ".items." + slot + ".amount", -1),
								pd.getString(ID + ".items." + slot + ".command"),
								pd.getString(ID + ".items." + slot + ".consoleCommand"),
								pd.getBoolean(ID + ".items." + slot + ".has title"),
								pd.getBoolean(ID + ".items." + slot + ".has lore")
							);

							if (item.hasTitle())
                                MessagesAPI.register(true, "ZCAPI", "Commands.GUI." + ID + ".items." + slot + ".title", "Pakeisti per ZCMessages");
							if (item.hasLore())
                                MessagesAPI.register(true, "ZCAPI", "Commands.GUI." + ID + ".items." + slot + ".lore", List.of(
                                    "Pakeisti per ZCMessages",
                                    "Pakeisti per ZCMessages"
                                ));

							items.put(slot, item);
						}

						CommandsHolder.addCommand(new GUICommandDTO(ID, command, aliases, register, type, title, size, items));
					}
				}
			}
	    }

		public static void addCommand(GUICommandDTO guiCommand) {
			File f = new File(GUI.plugin.getDataFolder() + "/commands/gui", File.separator + guiCommand.getID() + ".yml");
			FileConfiguration pd = YamlConfiguration.loadConfiguration(f);
			pd.set(guiCommand.getID() + ".command", guiCommand.getCommand());
			pd.set(guiCommand.getID() + ".register", guiCommand.isRegisterd());
			pd.set(guiCommand.getID() + ".type", guiCommand.getType().name());
			pd.set(guiCommand.getID() + ".title", guiCommand.getTitle());
			pd.set(guiCommand.getID() + ".size", guiCommand.getSize());
			for (ItemDTO item : guiCommand.getItems()) {
				pd.set(guiCommand.getID() + ".items." + item.getSlot() + ".material", item.getMaterial().name());
				pd.set(guiCommand.getID() + ".items." + item.getSlot() + ".amount", item.getAmount());
				pd.set(guiCommand.getID() + ".items." + item.getSlot() + ".command", item.getCommand());
				pd.set(guiCommand.getID() + ".items." + item.getSlot() + ".consoleCommand", item.getConsoleCommand());
				pd.set(guiCommand.getID() + ".items." + item.getSlot() + ".has title", item.hasTitle());
				pd.set(guiCommand.getID() + ".items." + item.getSlot() + ".has lore", item.hasLore());
			}
			try {
				pd.save(f);
			} catch (IOException e) {
				MSG.log(e);
			}
		}
	}
}
