package lt.itsvaidas.EventsAPI;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerInteractAtEntityMainHandEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull PlayerInteractAtEntityEvent e;
    private boolean cancelled;


    public PlayerInteractAtEntityMainHandEvent(@NotNull PlayerInteractAtEntityEvent e) {
        this.e = e;
    }

    public @NotNull Entity getRightClicked() {
        return this.e.getRightClicked();
    }

    public @NotNull Player getPlayer() {
        return this.e.getPlayer();
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
