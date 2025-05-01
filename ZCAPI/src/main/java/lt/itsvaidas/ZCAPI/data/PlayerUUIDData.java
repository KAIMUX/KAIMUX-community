package lt.itsvaidas.ZCAPI.data;

import lt.itsvaidas.DataAPI.DataFile;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class PlayerUUIDData {

    private static DataFile pd;

    public static void load(Plugin plugin) {
        pd = new DataFile(plugin, "players.yml", 10);
    }

    public static void save() {
        pd.save();
    }

    public static void setPlayerUUID(String username, UUID uuid) {
        if (pd.isSet("Usernames." + username + ".UUID")) {
            String oldUUID = pd.getString("Usernames." + username + ".UUID");
            if (!oldUUID.equals(uuid.toString())) {
                pd.set("UUIDs." + oldUUID + ".Username", null);
            }
        }
        if (pd.isSet("UUIDs." + uuid.toString() + ".Username")) {
            String oldUsername = pd.getString("UUIDs." + uuid.toString() + ".Username");
            if (!oldUsername.equals(username)) {
                pd.set("Usernames." + oldUsername + ".UUID", null);
            }
        }
        pd.set("Usernames." + username + ".UUID", uuid.toString());
        pd.set("UUIDs." + uuid.toString() + ".Username", username);
    }

    public static @Nullable UUID getPlayerUUID(String username) {
        if (pd.isSet("Usernames." + username + ".UUID"))
            return UUID.fromString(pd.getString("Usernames." + username + ".UUID"));
        return null;
    }

    public static @Nullable String getPlayerUsername(UUID uuid) {
        if (pd.isSet("UUIDs." + uuid.toString() + ".Username"))
            return pd.getString("UUIDs." + uuid.toString() + ".Username");
        return null;
    }
}
