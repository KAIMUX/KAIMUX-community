package lt.itsvaidas.ZCAPI.services;

import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.dto.TextCommandDTO;
import org.bukkit.entity.Player;

import java.util.List;

public class TextCommandService {

    public static void runCommand(Player player, TextCommandDTO command) {
        List<String> text = command.getText();
        if (text != null) {
            MSG.Send.raw(player, text);
        }
    }
}
