package lt.itsvaidas.MessagesAPI;

import lt.itsvaidas.MessagesAPI.anotations.Translatable;
import lt.itsvaidas.MessagesAPI.data.MessagesData;
import lt.itsvaidas.MessagesAPI.enums.Language;
import lt.itsvaidas.MessagesAPI.interfaces.TranslatableLore;
import lt.itsvaidas.MessagesAPI.interfaces.TranslatableTitle;
import lt.itsvaidas.ZCAPI.tools.DATA;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class MessagesAPI {

    private static Map<UUID, Language> languageCache = new HashMap<>();
    private static Plugin plugin;
    private static String server;

    public static void init(Plugin plugin, String server) {
        MessagesAPI.plugin = plugin;
        MessagesAPI.server = server;
    }

    public static void register(boolean isGlobal, String plugin, String key, String message) {
        String fullKey = (isGlobal ? "Global" : server) + "." + plugin + "." + key;
        MessagesData.register(fullKey, message);
    }

    public static void register(boolean isGlobal, String plugin, String key, List<String> messages) {
        String fullKey = (isGlobal ? "Global" : server) + "." + plugin + "." + key;
        MessagesData.register(fullKey, messages);
    }

    public static void register(Class<? extends Enum<?>> enumClass) {
        boolean hasTitle = TranslatableTitle.class.isAssignableFrom(enumClass);
        boolean hasLore = TranslatableLore.class.isAssignableFrom(enumClass);

        Translatable translatable = enumClass.getAnnotation(Translatable.class);
        boolean isGlobal = translatable.global();
        String plugin = translatable.plugin();

        String enumName = enumClass.getSimpleName();

        for (Enum<?> e : enumClass.getEnumConstants()) {
            String key = enumName + "." + e.name().replace("__", ".").toLowerCase();
            if (hasTitle && hasLore) {
                String title = ((TranslatableTitle) e).getTitle();
                List<String> lore = ((TranslatableLore) e).getLore();

                if (title != null && lore == null) {
                    register(isGlobal, plugin, key, title);
                } else if (title == null && lore != null) {
                    register(isGlobal, plugin, key, lore);
                } else {
                    register(isGlobal, plugin, key + ".title", title);
                    register(isGlobal, plugin, key + ".lore", lore);
                }
            } else if (hasTitle) {
                register(isGlobal, plugin, key, ((TranslatableTitle) e).getTitle());
            } else if (hasLore) {
                register(isGlobal, plugin, key, ((TranslatableLore) e).getLore());
            } else {
                register(isGlobal, plugin, key, e.name());
            }
        }
    }

    public static String getString(CommandSender sender, Enum<?> enumType) {
        boolean hasTitle = TranslatableTitle.class.isAssignableFrom(enumType.getClass());
        boolean hasLore = TranslatableLore.class.isAssignableFrom(enumType.getClass());

        Translatable translatable = enumType.getClass().getAnnotation(Translatable.class);
        boolean isGlobal = translatable.global();
        String plugin = translatable.plugin();

        String enumName = enumType.getClass().getSimpleName();

        String key = enumName + "." + enumType.name().replace("__", ".").toLowerCase();

        if (hasTitle && hasLore) {
                String title = ((TranslatableTitle) enumType).getTitle();
                List<String> lore = ((TranslatableLore) enumType).getLore();
            
            if (title != null && lore == null) {
                return getString(sender, isGlobal, plugin, key);
            } else {
                return getString(sender, isGlobal, plugin, key + ".title");
            }
        } else {
            return getString(sender, isGlobal, plugin, key);
        }
    }

    public static List<String> getList(CommandSender sender, Enum<?> enumType) {
        boolean hasTitle = TranslatableTitle.class.isAssignableFrom(enumType.getClass());
        boolean hasLore = TranslatableLore.class.isAssignableFrom(enumType.getClass());

        Translatable translatable = enumType.getClass().getAnnotation(Translatable.class);
        boolean isGlobal = translatable.global();
        String plugin = translatable.plugin();

        String enumName = enumType.getClass().getSimpleName();

        String key = enumName + "." + enumType.name().replace("__", ".").toLowerCase();

        if (hasTitle && hasLore) {
                String title = ((TranslatableTitle) enumType).getTitle();
                List<String> lore = ((TranslatableLore) enumType).getLore();
            
            if (title == null && lore != null) {
                return getList(sender, isGlobal, plugin, key);
            } else {
                return getList(sender, isGlobal, plugin, key + ".lore");
            }
        } else {
            return getList(sender, isGlobal, plugin, key);
        }
    }

    public static String getString(CommandSender sender, boolean isGlobal, String plugin, String key) {
        Language language = getLanguage(sender);
        String fullKey = (isGlobal ? "Global" : server) + "." + plugin + "." + key;
        return MessagesData.getString(language, fullKey);
    }

    public static List<String> getList(CommandSender sender, boolean isGlobal, String plugin, String key) {
        Language language = getLanguage(sender);
        String fullKey = (isGlobal ? "Global" : server) + "." + plugin + "." + key;
        return MessagesData.getList(language, fullKey);
    }

    public static boolean hasSelectedLanguage(Player player) {
        return DATA.hasValue(plugin, player, "language", PersistentDataType.STRING);
    }

    public static Language getLanguage(CommandSender sender) {
        if (sender instanceof Player player) {
            return Language.valueOf(DATA.getValue(plugin, player, "language", PersistentDataType.STRING, Language.EN.name()));
        }
        return Language.EN;
    }

    public static Language getLanguage(UUID uuid) {
        if (Bukkit.getOfflinePlayer(uuid).isOnline()) {
            return getLanguage(Bukkit.getPlayer(uuid));
        } else {
            return languageCache.getOrDefault(uuid, Language.EN);
        }
    }

    public static void setLanguage(Player player, Language language) {
        DATA.setValue(plugin, player, "language", PersistentDataType.STRING, language.name());
        languageCache.put(player.getUniqueId(), language);
    }

    public static boolean isPublicChatEnabled(Player player) {
        return DATA.getValue(plugin, player, "public_chat", PersistentDataType.BOOLEAN, true);
    }

    public static void setPublicChat(Player player, boolean value) {
        DATA.setValue(plugin, player, "public_chat", PersistentDataType.BOOLEAN, value);
    }
}
