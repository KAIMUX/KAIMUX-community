package lt.itsvaidas.ZCAPI;

import lt.itsvaidas.ZCAPI.dto.CommandDTO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CommandsHolder {

    private static final Map<String, CommandDTO> commands = new HashMap<>();

    public static void addCommand(CommandDTO command) {
        commands.put(command.getCommand(), command);
        command.getAliases().forEach(alias -> commands.put(alias, command));
    }

    public static Collection<CommandDTO> getCommands() {
        return commands.values();
    }

    public static void clearCommands() {
        commands.clear();
    }

    public static CommandDTO getCommand(String command) {
        return commands.get(command);
    }
}
