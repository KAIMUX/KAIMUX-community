package lt.itsvaidas.EventsAPI;

import lt.itsvaidas.MessagesAPI.enums.Language;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLanguageSwitchEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull Player player;
    private final @NotNull Language oldLanguage;
    private final @NotNull Language newLanguage;
    private boolean cancelled;

    public PlayerLanguageSwitchEvent(@NotNull Player player, @NotNull Language oldLanguage, @NotNull Language newLanguage) {
        this.player = player;
        this.oldLanguage = oldLanguage;
        this.newLanguage = newLanguage;
    }

    public @NotNull Player getPlayer() {
        return player;
    }

    public @NotNull Language getOldLanguage() {
        return oldLanguage;
    }

    public @NotNull Language getNewLanguage() {
        return newLanguage;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
