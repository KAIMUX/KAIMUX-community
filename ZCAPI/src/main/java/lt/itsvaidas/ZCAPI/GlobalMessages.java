package lt.itsvaidas.ZCAPI;

import lt.itsvaidas.MessagesAPI.anotations.Translatable;
import lt.itsvaidas.MessagesAPI.interfaces.TranslatableLore;
import lt.itsvaidas.MessagesAPI.interfaces.TranslatableTitle;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Translatable(plugin = "ZCAPI", global = true)
public enum GlobalMessages implements TranslatableLore, TranslatableTitle {

    LANGUAGE__SWITCHED(List.of(
        "Language set to <gold>English",
        "Please write only in English",
        "The more correctly you write, the better others will understand you!"
    )),
    LANGUAGE__PUBLIC_CHAT_TRANSLATOR("<green>Public chat translator", List.of(
        "Status: <arg0>",
        "Translates public chat messages",
        "from other languages to your",
        "language.",
        "",
        "Click to toggle."
    )),
    TELEPORT__INSTANT("You will be teleported once the area is loaded!"),
    TELEPORT__LOADING("Loading area..."),
    TELEPORT__TELEPORTING_CAN_MOVE("You will be teleported in 3s. You can move!"),
    TELEPORT__TELEPORTING_CANT_MOVE("You will be teleported in 3s. Please do not move!"),
    TELEPORT__TELEPORTING_CANCELLED("Teleportation cancelled!"),
    TELEPORT__COUNTDOWN("<dark_gray><bold>---> <red><arg0> <dark_gray><bold><---"),
    LANGUAGE__CHANGED_PUBLIC_CHAT_TRANSLATOR("Public chat translator was <arg0>!"),
    INCORRECT_PARAMETER("Your argument at <arg0> was incorrect"),

    ERROR__PLAYER_ONLY("Only players can use this command"),
    ERROR__PLAYER_NOT_ONLINE("Player is not online"),
    ERROR__PLAYER_NOT_FOUND("Player not found"),
    ERROR__ENCHANTMENT_NOT_FOUND("Enchantment not found"),
    ERROR__WORLD_NOT_FOUND("World not found"),
    ERROR__NOT_ENOUGH_MONEY("You need <arg0>ζ for this action"),
    ERROR__UNKNOWN_COMMAND("<red> ✕ <dark_gray>|<gray> Unknown command. Type <click:run_command:/help><hover:show_text:'<gray>Click to run'><gray>/help</hover></click> for help."),
    ERROR__NO_PERMISSION("You don't have permission to do this"),
    INFO__GEYSER_NOTICE_GLOBAL_LINK(List.of(
        "Link your account!",
        "",
        "We have noticed that you have not linked your Bedrock account to your Java account!",
        "You can do that very easily using Geyser Global Link!",
        "With this method, you will be able to play on all Java/Bedrock compatible servers with either a Bedrock or Java client, and you will retain all your items and progress in both game versions.",
        "",
        "Simply go to: https://link.geysermc.org/",
        "Or search for 'Geyser global linking' on Google."
    )),
    GUI__BACK("<red>Back"),
    GUI__PREVIOUS_PAGE("<red><bold>⮜"),
    GUI__NEXT_PAGE("<green><bold>➤"),
    TRUE("<green>True"),
    FALSE("<red>False"),
    YES("<green>Yes"),
    NO("<red>No"),
    TIMEOUT("You will be able to use this command in <red><arg0>"),
    ACCEPT("<green>Accept"),
    CONFIRM("<green>Confirm"),
    CANCEL("<red>Cancel"),
    REJECT("<red>Reject"),
    ENABLED("<green>Enabled"),
    DISABLED("<red>Disabled"),
    ;

    private String line;
    private List<String> lore;

    GlobalMessages(String line, List<String> lore) {
        this.line = line;
        this.lore = lore;
    }

    GlobalMessages(String line) {
        this.line = line;
    }

    GlobalMessages(List<String> lore) {
        this.lore = lore;
    }

    @Override
    public @Nullable List<String> getLore() {
        return lore;
    }

    @Override
    public @Nullable String getTitle() {
        return line;
    }
}
