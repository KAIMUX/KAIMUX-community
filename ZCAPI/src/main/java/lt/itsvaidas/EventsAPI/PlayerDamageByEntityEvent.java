package lt.itsvaidas.EventsAPI;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDamageByEntityEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull Player victim;
    private final @NotNull Entity damager;
    private boolean cancelled;

    public PlayerDamageByEntityEvent(@NotNull Player victim, @NotNull Entity damager, EntityDamageByEntityEvent e) {
        this.victim = victim;
        this.damager = damager;
    }

    public @NotNull Player getVictim() {
        return this.victim;
    }

    public @NotNull Entity getDamager() {
        return this.damager;
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
