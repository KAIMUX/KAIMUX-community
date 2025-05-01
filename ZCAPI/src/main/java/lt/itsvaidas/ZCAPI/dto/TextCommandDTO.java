package lt.itsvaidas.ZCAPI.dto;

import java.util.List;

public class TextCommandDTO extends CommandDTO {

    private final List<String> text;

    public TextCommandDTO(String ID, String command, boolean isRegisterd, List<String> aliases, List<String> text) {
        super(ID, command, isRegisterd, aliases);

        this.text = text;
    }

    public List<String> getText() {
        return text;
    }
}
