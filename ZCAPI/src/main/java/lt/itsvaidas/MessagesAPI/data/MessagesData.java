package lt.itsvaidas.MessagesAPI.data;

import lt.itsvaidas.MessagesAPI.enums.Language;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessagesData {

    private static File directory;
    private static final Map<Language, Map<String, Object>> oldMessages = new HashMap<>();
    private static final Map<Language, Map<String, Object>> messages = new HashMap<>();

    private static final Map<String, String> mapping = new HashMap<String, String>() {{
        put("&0", "<black>");
        put("&1", "<dark_blue>");
        put("&2", "<dark_green>");
        put("&3", "<dark_aqua>");
        put("&4", "<dark_red>");
        put("&5", "<dark_purple>");
        put("&6", "<gold>");
        put("&7", "<gray>");
        put("&8", "<dark_gray>");
        put("&9", "<blue>");
        put("&a", "<green>");
        put("&b", "<aqua>");
        put("&c", "<red>");
        put("&d", "<light_purple>");
        put("&e", "<yellow>");
        put("&f", "<white>");
        put("&k", "<obfuscated>");
        put("&l", "<bold>");
        put("&m", "<strikethrough>");
        put("&n", "<underline>");
        put("&o", "<italic>");
        put("&r", "<reset>");
    }};

    public static void load(Plugin plugin, String path) {
        directory = new File(plugin.getDataFolder()+"/"+path+"/Languages", File.separator);
        directory.mkdirs();

        for (Language language : Language.values())
            messages.put(language, new HashMap<>());

        oldMessages.clear();

        for (Language language : Language.values()) {
            File file = new File(directory, language.name()+".yml");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            oldMessages.put(language, new HashMap<>());
            for (String key : yaml.getKeys(true)) {
                if (yaml.isString(key)) {
                    String value = convertFromLegacy(yaml.getString(key));
                    oldMessages.get(language).put(key, value);
                } else if (yaml.isList(key)) {
                    List<String> value = convertFromLegacy(yaml.getStringList(key));
                    oldMessages.get(language).put(key, value);
                }
            }
        }
    }

    public static void reload(Plugin plugin, String path) {
        directory = new File(plugin.getDataFolder()+"/"+path+"/Languages", File.separator);
        directory.mkdirs();

        messages.clear();

        for (Language language : Language.values()) {
            File file = new File(directory, language.name()+".yml");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            messages.put(language, new HashMap<>());
            for (String key : yaml.getKeys(true)) {
                if (yaml.isString(key)) {
                    String value = convertFromLegacy(yaml.getString(key));
                    messages.get(language).put(key, value);
                } else if (yaml.isList(key)) {
                    List<String> value = convertFromLegacy(yaml.getStringList(key));
                    messages.get(language).put(key, value);
                }
            }
        }
    }

    private static String convertFromLegacy(String line) {
        for (Map.Entry<String, String> entry : mapping.entrySet()) {
            line = line.replace(entry.getKey(), entry.getValue());
        }
        return line;
    }

    private static List<String> convertFromLegacy(List<String> list) {
        list.replaceAll(MessagesData::convertFromLegacy);
        return list;
    }

    public static void save() {
        for (Language language : Language.values()) {
            File file = new File(directory, language.name()+".yml");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            for (Map.Entry<String, Object> entry : messages.get(language).entrySet()) {
                Object value = entry.getValue();
                if (value instanceof String s) {
                    yaml.set(entry.getKey(), s);
                } else if (value instanceof List<?> list) {
                    yaml.set(entry.getKey(), list);
                }
            }
            try {
                yaml.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearUnwanted(String server) {
        for (Language language : Language.values()) {
            File file = new File(directory, language.name()+".yml");
            YamlConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            for (String key : yaml.getKeys(true)) {
                if (!key.startsWith(server))
                    continue;
                if ((yaml.isString(key) || yaml.isList(key)) && !messages.get(language).containsKey(key)) {
                    yaml.set(key, null);
                }
                if (yaml.isConfigurationSection(key) && yaml.getConfigurationSection(key).getKeys(true).isEmpty()) {
                    // Go back each parent and check if it's empty
                    yaml.set(key, null);
                    String parent = key;
                    while (parent.contains(".")) {
                        parent = parent.substring(0, parent.lastIndexOf("."));
                        if (yaml.getConfigurationSection(parent).getKeys(true).isEmpty()) {
                            yaml.set(parent, null);
                        }
                    }
                }
            }
            try {
                yaml.save(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        oldMessages.clear();

        for (Language language : Language.values()) {
            oldMessages.put(language, new HashMap<>());
        }
    }

    public static void register(String key, String string) {
        for (Language language : Language.values()) {
            messages.get(language).put(key, oldMessages.get(language).getOrDefault(key, string));
        }
    }

    public static void register(String key, List<String> list) {
        for (Language language : Language.values()) {
            messages.get(language).put(key, oldMessages.get(language).getOrDefault(key, list));
        }
    }

    public static String getString(Language language, String key) {
        return messages.get(language).get(key).toString();
    }

    @SuppressWarnings("unchecked")
    public static List<String> getList(Language language, String key) {
        return new ArrayList<>((List<String>) messages.get(language).get(key));
    }
}
