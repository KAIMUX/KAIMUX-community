package lt.itsvaidas.ZCAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import lt.itsvaidas.ZCAPI.Config;

import java.util.stream.Stream;

public class StaticMessageKeysProvider extends ArgumentProvider {
    public StaticMessageKeysProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Config.getStaticMessages().stream();
    }
}
