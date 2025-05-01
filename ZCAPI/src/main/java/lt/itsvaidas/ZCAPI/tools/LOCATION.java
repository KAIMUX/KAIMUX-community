package lt.itsvaidas.ZCAPI.tools;

import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.ZCAPI.GlobalMessages;
import lt.itsvaidas.ZCAPI.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class LOCATION {

    private static final Map<String, Long> using = new HashMap<>();
    
	public static Location getTopYLocation(Location location, boolean fromTop) {
		Location l = location.clone();
		int x = l.getBlockX();
		int z = l.getBlockZ();
		World w = l.getWorld();
		int baseY = l.getBlockY();
		if (fromTop)
			baseY = 447;
		
		for (int y = baseY; y >= -64; y--)
			if (!w.getBlockAt(x, y, z).getType().isAir()) {
				l.setY(y + 1);
				return l;
			}
		
		Location loc = w.getHighestBlockAt(x, z).getLocation();
		loc.setY(loc.getY() + 4);
		return loc;
	}

    public static void teleport(Player p, Location l, boolean teleportInstant, boolean teleportOnGround, boolean teleportFromTop) {
    	String uuid = p.getUniqueId().toString();
    	
    	if (using.getOrDefault(uuid, 0L) > System.currentTimeMillis())
    		return;
    	using.put(uuid, System.currentTimeMillis()+1000L*5L);
    	
    	if (teleportInstant || p.hasPermission("survivalcore.tpinst")) {
    		MSG.Send.success(p, GlobalMessages.TELEPORT__INSTANT);
    		if (teleportOnGround)
    			p.teleportAsync(getTopYLocation(l, teleportFromTop), PlayerTeleportEvent.TeleportCause.COMMAND);
    		else
    			p.teleportAsync(l, PlayerTeleportEvent.TeleportCause.COMMAND);
    		return;
    	}

    	Location teleportTo;
    	if (teleportOnGround)
    		teleportTo = getTopYLocation(l, teleportFromTop);
    	else
    		teleportTo = l;
    	
		MSG.Send.success(p, GlobalMessages.TELEPORT__LOADING);
    	if (!p.hasPermission("survivalcore.tp.move") || p.getWorld().getName().equalsIgnoreCase("spawn"))
    		MSG.Send.success(p, GlobalMessages.TELEPORT__TELEPORTING_CANT_MOVE);
    	else
    		MSG.Send.success(p, GlobalMessages.TELEPORT__TELEPORTING_CAN_MOVE);
    	
    	new BukkitRunnable() {
    		int sk = 3;
    		final Location original = p.getLocation().clone();

			@Override
			public void run() {
				if (!p.isOnline()) {
			    	using.remove(uuid);
					this.cancel();
					return;
				}

                if (
                        (
                                (
                                        p.getLocation().getBlockX() != original.getBlockX()
                                                || p.getLocation().getBlockY() != original.getBlockY()
                                                || p.getLocation().getBlockZ() != original.getBlockZ()
                                )
                                        &&
                                        (
                                                !p.hasPermission("survivalcore.tp.move")
                                                        || p.getWorld().getName().equalsIgnoreCase("spawn")
                                        )
                        )
                                || !p.getLocation().getWorld().getName().equals(original.getWorld().getName())
                ) {
                    MSG.Send.success(p, GlobalMessages.TELEPORT__TELEPORTING_CANCELLED);
                    using.remove(uuid);
                    this.cancel();
                    return;
                }

                if (sk <= 0 || p.hasPermission("survivalcore.tpinst")) {
					MSG.actionBar(p, GlobalMessages.TELEPORT__COUNTDOWN, "0");
					p.teleportAsync(teleportTo, PlayerTeleportEvent.TeleportCause.COMMAND);
			    	using.remove(uuid);
					this.cancel();
					return;
				}

				if (sk > 0 && !p.hasPermission("survivalcore.tpinst")) {
					MSG.actionBar(p, GlobalMessages.TELEPORT__COUNTDOWN, String.valueOf(sk));
				}
				sk--;
			}
    		
    	}.runTaskTimer(Main.getInstance(), 20L, 20L);
    }
	
	public static boolean inLoc(Location l, Location l1, Location l2) {
		if (l1.getWorld() != l2.getWorld() || l1.getWorld() != l.getWorld())
			return false;
		int x1 = Math.min(l1.getBlockX(), l2.getBlockX());
		int x2 = Math.max(l1.getBlockX(), l2.getBlockX());

		int y1 = Math.min(l1.getBlockY(), l2.getBlockY());
		int y2 = Math.max(l1.getBlockY(), l2.getBlockY());

		int z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
		int z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		
		return x1 <= l.getBlockX() && l.getBlockX() <= x2
				&& y1 <= l.getBlockY() && l.getBlockY() <= y2
				&& z1 <= l.getBlockZ() && l.getBlockZ() <= z2;
	}

	public static Location fromString(@NotNull String line) {
		String[] split = line.split(";");
		return new Location(
				Bukkit.getWorld(split[0]),
				Double.parseDouble(split[1]),
				Double.parseDouble(split[2]),
				Double.parseDouble(split[3]),
				Float.parseFloat(split[4]),
				Float.parseFloat(split[5])
		);
	}

	public static Location fromIntString(@NotNull String line) {
		String[] split = line.split(";");
		return new Location(
				Bukkit.getWorld(split[0]),
				Double.parseDouble(split[1]),
				Double.parseDouble(split[2]),
				Double.parseDouble(split[3])
		);
	}

	public static String toString(@NotNull Location l) {
		return l.getWorld().getName()+";"+l.getX()+";"+l.getY()+";"+l.getZ()+";"+l.getYaw()+";"+l.getPitch();
	}

	public static String toIntString(@NotNull Location l) {
		return l.getWorld().getName()+";"+l.getBlockX()+";"+l.getBlockY()+";"+l.getBlockZ();
	}

	public static boolean isPlayerOnGround(Player player) {
		Location loc = player.getLocation();
		for (float x1 = -0.3f; x1 != 0.6f; x1 += 0.3f) {
			for (float z1 = -0.3f; z1 != 0.6f; z1 += 0.3f) {
				Block b = loc.add(x1, -0.501, z1).getBlock();
				loc.subtract(x1, -0.501, z1);
				if (!b.getType().isAir()) return true;
			}
		}
		return false;
	}

	public static Iterable<Block> getBlocks(@NotNull World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		return () -> new Iterator<>() {
            private final int minX = Math.min(x1, x2);
            private final int maxX = Math.max(x1, x2);
            private final int minY = Math.min(y1, y2);
            private final int maxY = Math.max(y1, y2);
            private final int minZ = Math.min(z1, z2);
            private final int maxZ = Math.max(z1, z2);

            private int x = minX, y = minY, z = minZ;

            @Override
            public boolean hasNext() {
                return x <= maxX && y <= maxY && z <= maxZ;
            }

            @Override
            public Block next() {
                Block block = world.getBlockAt(x, y, z);

                if (++z > maxZ) {
                    z = minZ;
                    if (++y > maxY) {
                        y = minY;
                        x++;
                    }
                }

                return block;
            }
        };
	}
}
