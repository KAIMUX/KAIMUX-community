package lt.itsvaidas.MessagesAPI;

import lt.itsvaidas.MessagesAPI.interfaces.TranslatableLore;
import lt.itsvaidas.MessagesAPI.interfaces.TranslatableTitle;
import lt.itsvaidas.ZCAPI.Main;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.List;
import java.util.logging.Level;

public class MSG {

    private static final MiniMessage mm = MiniMessage.builder()
                .tags(TagResolver.builder()
                        .resolvers(
                                StandardTags.color(),
                                StandardTags.decorations(),
                                StandardTags.gradient(),
                                StandardTags.rainbow(),
                                StandardTags.reset(),
                                StandardTags.clickEvent(),
                                StandardTags.hoverEvent()
                        )
                        .build())
                .build();

    private static final String SUCCESS_PREFIX = "<green> ✔ <dark_gray>|<gray> ";
    private static final String ERROR_PREFIX = "<red> ✕ <dark_gray>|<gray> ";
    private static final String INFO_PREFIX = "<yellow> ❖ <dark_gray>|<gray> ";

    public static class Send {
        public static void raw(CommandSender sender, Enum<?> message, Object... args) {
            sendWithPrefix(sender, "", message, args);
        }

        public static void success(CommandSender sender, Enum<?> message, Object... args) {
            sendWithPrefix(sender, SUCCESS_PREFIX, message, args);
        }

        public static void error(CommandSender sender, Enum<?> message, Object... args) {
            sendWithPrefix(sender, ERROR_PREFIX, message, args);
        }

        public static void info(CommandSender sender, Enum<?> message, Object... args) {
            sendWithPrefix(sender, INFO_PREFIX, message, args);
        }

        public static void raw(CommandSender sender, String message, Object... args) {
            sender.sendMessage(stringToComponent(message, args));
        }

        public static void success(CommandSender sender, String message, Object... args) {
            sender.sendMessage(stringToComponent(SUCCESS_PREFIX + message, args));
        }

        public static void error(CommandSender sender, String message, Object... args) {
            sender.sendMessage(stringToComponent(ERROR_PREFIX + message, args));
        }

        public static void info(CommandSender sender, String message, Object... args) {
            sender.sendMessage(stringToComponent(INFO_PREFIX + message, args));
        }

        public static void raw(CommandSender sender, List<String> messages, Object... args) {
            messages.stream().map(s -> stringToComponent(s, args)).forEach(sender::sendMessage);
        }

        public static void success(CommandSender sender, List<String> messages, Object... args) {
            messages.stream().map(s -> stringToComponent(SUCCESS_PREFIX + s, args)).forEach(sender::sendMessage);
        }

        public static void error(CommandSender sender, List<String> messages, Object... args) {
            messages.stream().map(s -> stringToComponent(ERROR_PREFIX + s, args)).forEach(sender::sendMessage);
        }

        public static void info(CommandSender sender, List<String> messages, Object... args) {
            messages.stream().map(s -> stringToComponent(INFO_PREFIX + s, args)).forEach(sender::sendMessage);
        }
    }

    public static void command(CommandSender sender, String command, String description) {
        if (command.contains("<") || command.contains("[")) {
            String commandWithoutArgs = command.replaceAll("<.*?>", "").replaceAll("\\[.*?]", "");
            sender.sendMessage(stringToComponent("<gold> ▶ <dark_gray>|<gray> <click:suggest_command:" + commandWithoutArgs + "><hover:show_text:'" + description + "'>" + command + "</hover></click> - " + description));
        } else {
            sender.sendMessage(stringToComponent("<gold> ▶ <dark_gray>|<gray> <click:run_command:" + command + "><hover:show_text:'" + description + "'>" + command + "</hover></click> - " + description));
        }
    }

    public static Component rawLine(CommandSender sender, Enum<?> message, Object... args) {
        return stringToComponent(MessagesAPI.getString(sender, message), args);
    }

    public static List<Component> rawList(CommandSender sender, Enum<?> message, Object... args) {
        return MessagesAPI.getList(sender, message).stream().map(s -> stringToComponent(s, args)).toList();
    }

    public static @Nullable Component raw(String message, Object... args) {
        if (message == null) return null;
        return stringToComponent(message, args);
    }

    public static @Nullable List<Component> raw(List<String> messages, Object... args) {
        if (messages == null) return null;
        return messages.stream().map(s -> stringToComponent(s, args)).toList();
    }

    public static String plain(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }

    public static String strip(String string) {
        return mm.stripTags(string);
    }

    public static String toLegacy(Component component) {
        return LegacyComponentSerializer.legacySection().serialize(component);
    }

    public static void actionBar(Player player, Enum<?> message, Object... args) {
        player.sendActionBar(stringToComponent(MessagesAPI.getString(player, message), args));
    }

    public static void title(Player p, Enum<?> message, long fadeIn, long stay, long fadeOut, Object... args) {
        List<String> text = MessagesAPI.getList(p, message);
        String titleString = text.isEmpty() ? null : text.get(0);
        String subtitleString = text.size() > 1 ? text.get(1) : null;

        Component rawTitle = raw(titleString, args);
        Component rawSubtitle = raw(subtitleString, args);

        Component title = rawTitle == null ? Component.empty() : rawTitle;
        Component subtitle = rawSubtitle == null ? Component.empty() : rawSubtitle;

        p.showTitle(
                Title.title(
                        title,
                        subtitle,
                        Title.Times.times(
                                Duration.ofMillis(fadeIn),
                                Duration.ofMillis(stay),
                                Duration.ofMillis(fadeOut)
                        )
                )
        );
    }

    public static void rawTitle(Player player, String s, String s1, int i, int i1, int i2) {
        player.showTitle(Title.title(stringToComponent(s), stringToComponent(s1), Title.Times.times(Duration.ofMillis(i), Duration.ofMillis(i1), Duration.ofMillis(i2))));
    }

    private static void sendWithPrefix(CommandSender sender, String prefix, Enum<?> message, Object... args) {
        boolean hasTitle = TranslatableTitle.class.isAssignableFrom(message.getClass());
        boolean hasLore = TranslatableLore.class.isAssignableFrom(message.getClass());

        if (hasTitle && hasLore) {
            String title = ((TranslatableTitle) message).getTitle();
            List<String> lore = ((TranslatableLore) message).getLore();

            if (title != null && lore == null) {
                sender.sendMessage(stringToComponent(prefix + MessagesAPI.getString(sender, message), args));
                return;
            } else if (title == null && lore != null) {
                MessagesAPI.getList(sender, message).stream().map(s -> stringToComponent(prefix + s, args))
                    .forEach(sender::sendMessage);
            } else {
                sender.sendMessage(stringToComponent(prefix + MessagesAPI.getString(sender, message), args));
                MessagesAPI.getList(sender, message).stream().map(s -> stringToComponent(prefix + s, args))
                        .forEach(sender::sendMessage);
            }
        } else if (hasLore) {
            MessagesAPI.getList(sender, message).stream().map(s -> stringToComponent(prefix + s, args))
                    .forEach(sender::sendMessage);
        } else {
            sender.sendMessage(stringToComponent(prefix + MessagesAPI.getString(sender, message), args));
        }
    }

    private static Component stringToComponent(@NotNull String string, Object... args) {
        TagResolver.Single[] placeholders = new TagResolver.Single[args.length];
        for (int i = args.length - 1; i >= 0; i--) {
            if (args[i] instanceof Component component) {
                placeholders[i] = Placeholder.component("arg" + i, component);
            } else if (args[i] instanceof String line) {
                placeholders[i] = Placeholder.parsed("arg" + i, line);
            } else if (args[i] instanceof Integer integer) {
                placeholders[i] = Placeholder.parsed("arg" + i, integer.toString());
            } else if (args[i] instanceof Double dbl) {
                placeholders[i] = Placeholder.parsed("arg" + i, dbl.toString());
            } else if (args[i] instanceof TagResolver.Single placeholder) {
                placeholders[i] = placeholder;
            }
        }

        return Component.empty()
                .style(Style.style(TextDecoration.ITALIC.withState(false)))
                .append(mm.deserialize(string, placeholders));
    }

    public static void log(Level level, String message) {
        Bukkit.getLogger().log(level, message);
        Bukkit.getOnlinePlayers().stream()
                .filter(p -> Main.getLogEnabledPlayers().contains(p.getUniqueId()))
                .forEach(p -> p.sendMessage(stringToComponent("<gray>[<red>" + level.getLocalizedName() + "<gray>] <white>" + message)));
    }

    public static void log(Exception e) {
        Bukkit.getLogger().log(Level.SEVERE, e.getMessage(), e);
    }
}
