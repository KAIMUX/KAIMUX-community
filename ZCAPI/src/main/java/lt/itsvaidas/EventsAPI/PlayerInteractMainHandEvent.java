package lt.itsvaidas.EventsAPI;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerInteractMainHandEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();

    private final @NotNull PlayerInteractEvent e;
    private boolean cancelled;


    public PlayerInteractMainHandEvent(@NotNull PlayerInteractEvent e) {
        this.e = e;
    }

    public @NotNull Action getAction() {
        return this.e.getAction();
    }

    public @Nullable Block getClickedBlock() {
        return this.e.getClickedBlock();
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

    public void setUseInteractedBlock(Result result) {
        this.e.setUseInteractedBlock(result);
    }

    public void setUseItemInHand(Result result) {
        this.e.setUseItemInHand(result);
    }

    public @Nullable ItemStack getItem() {
        return this.e.getItem();
    }

    public @NotNull BlockFace getBlockFace() {
        return this.e.getBlockFace();
    }
}
