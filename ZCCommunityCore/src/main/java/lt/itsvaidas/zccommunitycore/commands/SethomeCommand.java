package lt.itsvaidas.zccommunitycore.commands;

import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import org.bukkit.entity.Player;

@Command(
        name = "sethome",
        description = "Set your home location"
)
public class SethomeCommand {

    @Path(
            description = "Set your home location"
    )
    public void onSethomeCommand(Player sender) {

    }
}
