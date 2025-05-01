package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.stream.Stream;

public class WorldsProvider extends ArgumentProvider {
    public WorldsProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Bukkit.getWorlds().stream().map(World::getName);
    }
}
