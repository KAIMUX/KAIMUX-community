package lt.itsvaidas.EventsAPI;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerDamageByPlayerEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull Player victim;
    private final @NotNull Player damager;
    private final EntityDamageByEntityEvent e;
    private boolean cancelled;

    public PlayerDamageByPlayerEvent(@NotNull Player victim, @NotNull Player damager, EntityDamageByEntityEvent e) {
        this.victim = victim;
        this.damager = damager;
        this.e = e;
    }

    public @NotNull Player getVictim() {
        return this.victim;
    }

    public @NotNull Player getDamager() {
        return this.damager;
    }

    public @NotNull Entity getActualDamager() {
        return e.getDamager();
    }

    public void setDamage(double damage) {
        e.setDamage(damage);
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
