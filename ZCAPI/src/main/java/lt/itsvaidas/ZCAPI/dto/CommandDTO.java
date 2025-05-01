package lt.itsvaidas.ZCAPI.dto;

import java.util.List;

public class CommandDTO {

    private final String ID;
    private final String command;
    private final boolean isRegisterd;
    private final List<String> aliases;

    public CommandDTO(String ID, String command, boolean isRegisterd, List<String> aliases) {
        this.ID = ID;
        this.command = command;
        this.isRegisterd = isRegisterd;
        this.aliases = aliases;
    }

    public String getID() {
        return ID;
    }

    public String getCommand() {
        return command;
    }

    public boolean isRegisterd() {
        return isRegisterd;
    }

    public List<String> getAliases() {
        return aliases;
    }

}
