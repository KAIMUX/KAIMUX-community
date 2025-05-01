package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.stream.Stream;

public class EntityTypeProvider extends ArgumentProvider {
    public EntityTypeProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Arrays.stream(EntityType.values()).map(Enum::name);
    }
}
