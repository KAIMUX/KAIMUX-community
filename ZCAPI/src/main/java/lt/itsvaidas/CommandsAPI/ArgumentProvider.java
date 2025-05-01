package lt.itsvaidas.CommandsAPI;

import io.papermc.paper.command.brigadier.CommandSourceStack;

import java.util.stream.Stream;

public abstract class ArgumentProvider {
    public ArgumentProvider() {}
    protected abstract Stream<String> provide(CommandSourceStack source);
}
