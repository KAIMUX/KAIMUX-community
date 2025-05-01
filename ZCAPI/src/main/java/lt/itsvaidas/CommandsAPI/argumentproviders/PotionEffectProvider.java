package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import org.bukkit.Registry;

import java.util.stream.Stream;

public class PotionEffectProvider extends ArgumentProvider {
    public PotionEffectProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Registry.MOB_EFFECT.stream().map(s -> s.getKey().value());
    }
}
