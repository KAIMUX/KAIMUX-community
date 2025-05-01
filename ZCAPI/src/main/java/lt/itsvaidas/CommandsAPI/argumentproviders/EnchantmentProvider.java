package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;

import java.util.stream.Stream;

public class EnchantmentProvider extends ArgumentProvider {
    public EnchantmentProvider() {}
    @Override
    protected Stream<String> provide(CommandSourceStack source) {
        return RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).stream().map(e -> e.getKey().value());
    }
}
