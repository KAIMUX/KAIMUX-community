package lt.itsvaidas.ZCAPI.dto;

import java.util.List;

public class TextCommandDTO extends CommandDTO {

    public TextCommandDTO(String ID, String command, boolean isRegisterd, List<String> aliases) {
        super(ID, command, isRegisterd, aliases);
    }
}
