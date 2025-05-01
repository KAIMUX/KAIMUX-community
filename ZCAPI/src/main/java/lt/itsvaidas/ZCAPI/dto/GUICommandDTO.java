package lt.itsvaidas.ZCAPI.dto;

import org.bukkit.event.inventory.InventoryType;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GUICommandDTO extends CommandDTO {

    final InventoryType type;
    final String title;
    final int size;
    final Map<Integer, ItemDTO> items;

    public GUICommandDTO(String ID, String command, List<String> aliases, boolean isRegisterd, InventoryType type, String title, int size, Map<Integer, ItemDTO> items) {
        super(ID, command, isRegisterd, aliases);
        this.type = type;
        this.title = title;
        this.size = size;
        this.items = items;
    }

    public InventoryType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public int getSize() {
        return size;
    }

    public Collection<ItemDTO> getItems() {
        return items.values();
    }

    public ItemDTO getItem(int slot) {
        return items.get(slot);
    }

}
