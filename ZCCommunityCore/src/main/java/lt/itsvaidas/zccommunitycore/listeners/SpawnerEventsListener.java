package lt.itsvaidas.zccommunitycore.listeners;

import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.builders.BItem;
import lt.itsvaidas.ZCAPI.tools.DATA;
import lt.itsvaidas.ZCAPI.tools.TOOLS;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

public class SpawnerEventsListener implements Listener {

    private final Plugin plugin;
    private final Tag<Material> pickaxes = Tag.ITEMS_PICKAXES;

    public SpawnerEventsListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockBreak(BlockBreakEvent e) {
        e.getPlayer().sendMessage("1");
        if (!(e.getBlock().getState() instanceof CreatureSpawner spawner)) return;
        ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
        e.getPlayer().sendMessage("2 " + pickaxes.isTagged(item.getType()) + " " + item.getEnchantments().containsKey(Enchantment.SILK_TOUCH));
        if (pickaxes.isTagged(item.getType()) && item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) {

            e.getPlayer().sendMessage("3");
            int chance = TOOLS.randomInt(10, 15);
            int current = TOOLS.randomInt(0, 100);
            e.getPlayer().sendMessage("4 " + chance + " " + current);
            if (current < chance) {
                e.getPlayer().sendMessage("5");
                EntityType type = spawner.getSpawnedType();
                if (type == null) type = EntityType.PIG;
                e.getPlayer().sendMessage("6");

                ItemStack spawnerItem = BItem.b(Material.SPAWNER,  MSG.raw("<arg0> Spawner", Component.translatable(type))).key(plugin, "spawner", type.toString()).build();
                e.getBlock().getWorld().dropItem(e.getBlock().getLocation(), spawnerItem);
                e.getPlayer().sendMessage("7");
            }
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType().equals(Material.SPAWNER)) {
            CreatureSpawner spawner = (CreatureSpawner) e.getBlockPlaced().getState();
            String type = DATA.getValue(plugin, e.getItemInHand(), "spawner", PersistentDataType.STRING, "PIG");
            spawner.setSpawnedType(EntityType.valueOf(type));
            spawner.update();
        }
    }
}
