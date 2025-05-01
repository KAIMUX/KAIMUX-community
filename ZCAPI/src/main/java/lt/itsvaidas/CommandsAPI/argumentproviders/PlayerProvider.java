package lt.itsvaidas.CommandsAPI.argumentproviders;

import io.papermc.paper.command.brigadier.CommandSourceStack;
import lt.itsvaidas.CommandsAPI.ArgumentProvider;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.stream.Stream;

public class PlayerProvider extends ArgumentProvider {
    public PlayerProvider() {}
    @Override
    public Stream<String> provide(CommandSourceStack source) {
        return Bukkit.getOnlinePlayers().stream().map(Player::getName);
    }
}
