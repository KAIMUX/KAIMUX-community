package lt.itsvaidas.zccommunitycore;

import org.jetbrains.annotations.Nullable;

import java.util.*;

public class TemporaryData {

    public static class TpaRequests {
        private static final Set<UUID> teleportDisabled = new HashSet<>();
        private static final Map<UUID, UUID> requests = new HashMap<>();

        public static void add(UUID from, UUID to) {
            requests.put(from, to);
        }

        public static void remove(UUID from) {
            requests.remove(from);
        }

        public static @Nullable UUID get(UUID from) {
            return requests.get(from);
        }

        public static @Nullable UUID getFrom(UUID uuid) {
            return requests.entrySet().stream().filter(e -> e.getValue().equals(uuid)).findFirst().map(Map.Entry::getKey).orElse(null);
        }
    }
}
