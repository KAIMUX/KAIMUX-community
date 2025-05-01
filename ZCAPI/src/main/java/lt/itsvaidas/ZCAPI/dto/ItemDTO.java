package lt.itsvaidas.ZCAPI.dto;

import org.bukkit.Material;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDTO {
    private final int slot;
    private final Material material;
    private final int amount;
    private final @Nullable String command;
    private final @Nullable String consoleCommand;
    private final String title;
    private final List<String> lore;

    public ItemDTO(int slot, Material material, int amount, @Nullable String command, @Nullable String consoleCommand, String title, List<String> lore) {
        this.slot = slot;
        this.material = material;
        this.amount = amount;
        this.command = command;
        this.consoleCommand = consoleCommand;
        this.title = title;
        this.lore = lore;
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

    public String title() {
        return title;
    }

    public List<String> lore() {
        return lore;
    }

    public @Nullable String getConsoleCommand() {
        return consoleCommand;
    }
}
