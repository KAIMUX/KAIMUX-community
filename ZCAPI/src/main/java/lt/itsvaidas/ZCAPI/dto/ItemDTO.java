package lt.itsvaidas.ZCAPI.dto;

import org.bukkit.Material;

import javax.annotation.Nullable;

public class ItemDTO {
    private final int slot;
    private final Material material;
    private final int amount;
    private final @Nullable String command;
    private final @Nullable String consoleCommand;
    private final boolean hasTitle;
    private final boolean hasLore;

    public ItemDTO(int slot, Material material, int amount, @Nullable String command, @Nullable String consoleCommand, boolean hasTitle, boolean hasLore) {
        this.slot = slot;
        this.material = material;
        this.amount = amount;
        this.command = command;
        this.consoleCommand = consoleCommand;
        this.hasTitle = hasTitle;
        this.hasLore = hasLore;
    }

    public int getSlot() {
        return slot;
    }

    public Material getMaterial() {
        return material;
    }

    public int getAmount() {
        return amount;
    }

    public @Nullable String getCommand() {
        return command;
    }

    public boolean hasTitle() {
        return hasTitle;
    }

    public boolean hasLore() {
        return hasLore;
    }

    public @Nullable String getConsoleCommand() {
        return consoleCommand;
    }
}
