package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.stream.Stream;

public class MaterialProvider extends ArgumentProvider {
    public MaterialProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Arrays.stream(Material.values()).map(Enum::name);
    }
}
