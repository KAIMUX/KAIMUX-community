package lt.itsvaidas.ZCAPI.commands;

import lt.itsvaidas.ZCAPI.dto.CommandDTO;
import lt.itsvaidas.ZCAPI.dto.GUICommandDTO;
import lt.itsvaidas.ZCAPI.dto.TextCommandDTO;
import lt.itsvaidas.ZCAPI.services.GUICommandService;
import lt.itsvaidas.ZCAPI.services.TextCommandService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FakeCommand extends Command {

	final CommandDTO command;

	public FakeCommand(CommandDTO command) {
		super(command.getCommand(), "", "/"+command.getCommand(), command.getAliases());
		this.command = command;
	}

	@Override
	public boolean execute(CommandSender sender, String arg1, String[] arg2) {
		if (sender instanceof Player player) {
			if (command instanceof TextCommandDTO textCommandDTO) {
				TextCommandService.runCommand(player, textCommandDTO);
			} else if (command instanceof GUICommandDTO guiCommandDTO) {
				GUICommandService.runCommand(player, guiCommandDTO);
			}
		}
		return true;
	}

}
