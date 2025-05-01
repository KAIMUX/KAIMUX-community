package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import org.bukkit.WeatherType;

import java.util.Arrays;
import java.util.stream.Stream;

public class WeatherProvider extends ArgumentProvider {
    public WeatherProvider() {}

    @Override
    protected Stream<String> provide(CommandSourceStack source) {
        return Arrays.stream(WeatherType.values()).map(Enum::name);
    }

}
