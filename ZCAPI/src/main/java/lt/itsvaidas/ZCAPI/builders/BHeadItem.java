package lt.itsvaidas.ZCAPI.builders;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import lt.itsvaidas.MessagesAPI.MSG;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class BHeadItem extends BItem {

    private @NotNull final String playerHeadTexture;
    private @NotNull final String playerHeadSignature;

    public BHeadItem(@Nullable Component name, @Nullable List<Component> lore, @NotNull String texture, @NotNull String signature, String... args) {
        super(Material.PLAYER_HEAD, name, lore);
        this.playerHeadTexture = texture;
        this.playerHeadSignature = signature;
    }

    @Override
    public void additionalMeta(ItemMeta meta) {
        if (meta instanceof SkullMeta skull) {
            PlayerProfile profile = Bukkit.createProfile(UUID.randomUUID(), "ZCAPI");
            profile.setProperty(new ProfileProperty(
                    "textures",
                    playerHeadTexture,
                    playerHeadSignature
            ));
            skull.setPlayerProfile(profile);
        }
    }


    public static BHeadItem b(@Nullable String name, @NotNull String texture, @NotNull String signature) {
        return new BHeadItem(MSG.raw(name), null, texture, signature);
    }

    public static BHeadItem b(@Nullable String name, @Nullable List<String> lore, @NotNull String texture, @NotNull String signature) {
        return new BHeadItem(MSG.raw(name), MSG.raw(lore), texture, signature);
    }

    public static BHeadItem b(@Nullable Component name, @NotNull String texture, @NotNull String signature) {
        return new BHeadItem(name, null, texture, signature);
    }

    public static BHeadItem b(@Nullable Component name, @Nullable List<Component> lore, @NotNull String texture, @NotNull String signature) {
        return new BHeadItem(name, lore, texture, signature);
    }
}
