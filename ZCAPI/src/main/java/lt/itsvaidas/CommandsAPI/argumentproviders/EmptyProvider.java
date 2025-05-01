package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;

import java.util.stream.Stream;

public class EmptyProvider extends ArgumentProvider {
    public EmptyProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Stream.empty();
    }
}
