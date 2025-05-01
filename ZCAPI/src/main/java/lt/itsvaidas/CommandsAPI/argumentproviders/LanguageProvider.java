package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import lt.itsvaidas.MessagesAPI.enums.Language;

import java.util.Arrays;
import java.util.stream.Stream;

public class LanguageProvider extends ArgumentProvider {
    public LanguageProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Arrays.stream(Language.values()).map(Enum::name);
    }
}
