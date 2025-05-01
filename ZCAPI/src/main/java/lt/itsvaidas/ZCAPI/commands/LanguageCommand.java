package lt.itsvaidas.ZCAPI.commands;

import lt.itsvaidas.CommandsAPI.anotations.Argument;
import lt.itsvaidas.CommandsAPI.anotations.Command;
import lt.itsvaidas.CommandsAPI.anotations.Path;
import lt.itsvaidas.CommandsAPI.argumentproviders.LanguageProvider;
import lt.itsvaidas.CommandsAPI.argumentproviders.PlayerProvider;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.MessagesAPI.MessagesAPI;
import lt.itsvaidas.MessagesAPI.enums.Language;
import lt.itsvaidas.ZCAPI.guis.LanguageSelectGUI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

@Command(
        name = "language",
        description = "Change server language",
        aliases = {"lang"}
)
public class LanguageCommand {

    private final LanguageSelectGUI gui;

    public LanguageCommand(Plugin plugin) {
        this.gui = new LanguageSelectGUI(plugin);
    }

    @Path(
        description = "Change server language"
    )
    public void onChangeLanguage(CommandSender sender) {
        if (sender instanceof Player player) {
            gui.open(player);
        }
    }

    @Path(
            path = "set <Player> <Language>",
            description = "Set player language",
            permission = "zcapi.commands.language.set",
            arguments = {
                    @Argument(key = "player", provider = PlayerProvider.class),
                    @Argument(key = "language", provider = LanguageProvider.class)
            }
    )
    public void onSetLanguage(CommandSender sender, Player player, Language language) {
        MessagesAPI.setLanguage(player, language);
        MSG.Send.success(sender, "Player language set to " + language.name());
    }
}
