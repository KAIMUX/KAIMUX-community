package lt.itsvaidas.ZCAPI.guis;

import lt.itsvaidas.EventsAPI.PlayerLanguageSwitchEvent;
import lt.itsvaidas.MenuAPI.GeyserGUI;
import lt.itsvaidas.MenuAPI.events.CInventoryClick;
import lt.itsvaidas.MenuAPI.events.CInventoryClose;
import lt.itsvaidas.MenuAPI.geyser.elements.Button;
import lt.itsvaidas.MenuAPI.geyser.forms.SimpleForm;
import lt.itsvaidas.MenuAPI.geyser.interfaces.IForm;
import lt.itsvaidas.MessagesAPI.MSG;
import lt.itsvaidas.MessagesAPI.MessagesAPI;
import lt.itsvaidas.MessagesAPI.enums.Language;
import lt.itsvaidas.ZCAPI.GlobalMessages;
import lt.itsvaidas.ZCAPI.builders.BItem;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class LanguageSelectGUI extends GeyserGUI {

    public LanguageSelectGUI(Plugin plugin) {
        super(plugin, 2, "Language selection");
    }

    @Override
    public void create(Inventory inv, Player p, String... args) {
        inv.setItem(0, lithuanianFlag());
        inv.setItem(1, englishFlag());
        inv.setItem(2, russianFlag());
        inv.setItem(3, germanFlag());
        inv.setItem(4, polishFlag());
        inv.setItem(5, franceFlag());
        inv.setItem(6, italyFlag());
        inv.setItem(7, spainFlag());
        inv.setItem(8, turkeyFlag());

        inv.setItem(17, BItem.b(p, Material.PAPER,
                GlobalMessages.LANGUAGE__PUBLIC_CHAT_TRANSLATOR,
                GlobalMessages.LANGUAGE__PUBLIC_CHAT_TRANSLATOR).build());
    }

    @Override
    public void invInteract(CInventoryClick e) {
        if (e.getItem() == null) return;

        Language current = MessagesAPI.getLanguage(e.getPlayer());

        switch (e.getSlot()) {
            case 0 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.LT).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.LT);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 1 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.EN).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.EN);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 2 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.RU).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.RU);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 3 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.DE).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.DE);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 4 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.PL).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.PL);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 5 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.FR).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.FR);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 6 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.IT).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.IT);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 7 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.ES).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.ES);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 8 -> {
                new PlayerLanguageSwitchEvent(e.getPlayer(), current, Language.FR).callEvent();
                MessagesAPI.setLanguage(e.getPlayer(), Language.TR);
                MSG.Send.info(e.getPlayer(), GlobalMessages.LANGUAGE__SWITCHED);
                e.getPlayer().closeInventory();
            }
            case 17 -> {
                MessagesAPI.setPublicChat(e.getPlayer(), !MessagesAPI.isPublicChatEnabled(e.getPlayer()));
                MSG.Send.success(e.getPlayer(), GlobalMessages.LANGUAGE__CHANGED_PUBLIC_CHAT_TRANSLATOR, MessagesAPI.isPublicChatEnabled(e.getPlayer()) ? "<green>enabled<gray>" : "<red>disabled<gray>");
                e.getPlayer().closeInventory();
            }
        }
    }

    @Override
    public void invClose(CInventoryClose e) {
        if (!MessagesAPI.hasSelectedLanguage(e.getPlayer())) {
            e.setCancalled(true);
        }
    }

    public ItemStack lithuanianFlag()
    {
        ItemStack i = new ItemStack(Material.GREEN_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Lietuvių"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    public ItemStack englishFlag()
    {
        ItemStack i = new ItemStack(Material.BLUE_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNLEFT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_DOWNRIGHT));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CROSS));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRAIGHT_CROSS));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>English"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    public ItemStack russianFlag()
    {
        ItemStack i = new ItemStack(Material.BLUE_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Русский"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    public ItemStack germanFlag()
    {
        ItemStack i = new ItemStack(Material.RED_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.BLACK, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.YELLOW, PatternType.STRIPE_BOTTOM));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Deutsch"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    public ItemStack polishFlag()
    {
        ItemStack i = new ItemStack(Material.RED_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.WHITE, PatternType.HALF_HORIZONTAL));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Polski"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    public ItemStack franceFlag()
    {
        ItemStack i = new ItemStack(Material.BLUE_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_VERTICAL_RIGHT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Français"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    public ItemStack italyFlag()
    {
        ItemStack i = new ItemStack(Material.LIME_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.RED, PatternType.HALF_VERTICAL_RIGHT));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_CENTER));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Italiano"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    public ItemStack spainFlag()
    {
        ItemStack i = new ItemStack(Material.ORANGE_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Español"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    private ItemStack turkeyFlag() {
        ItemStack i = new ItemStack(Material.RED_BANNER, 1);
        BannerMeta m = (BannerMeta)i.getItemMeta();
        List<Pattern> patterns = new ArrayList<>();

        patterns.add(new Pattern(DyeColor.WHITE, PatternType.HALF_VERTICAL));
        patterns.add(new Pattern(DyeColor.WHITE, PatternType.STRIPE_MIDDLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CURLY_BORDER));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_TOP));
        patterns.add(new Pattern(DyeColor.RED, PatternType.STRIPE_BOTTOM));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CIRCLE));
        patterns.add(new Pattern(DyeColor.RED, PatternType.CIRCLE));
        m.setPatterns(patterns);
        m.displayName(MSG.raw("<green>Türkçe"));

        i.setItemMeta(m);
        i.addItemFlags(ItemFlag.values());

        return i;
    }

    @Override
    public IForm generateForm(Player player, String... args) {
        return SimpleForm.of(
                "Select language",
                () -> MessagesAPI.hasSelectedLanguage(player),
                Button.of("Lietuvių", p -> changeLanguage(p, Language.LT)),
                Button.of("English", p -> changeLanguage(p, Language.EN)),
                Button.of("Русский", p -> changeLanguage(p, Language.RU)),
                Button.of("Deutsch", p -> changeLanguage(p, Language.DE)),
                Button.of("Polski", p -> changeLanguage(p, Language.PL)),
                Button.of("Français", p -> changeLanguage(p, Language.FR)),
                Button.of("Italiano", p -> changeLanguage(p, Language.IT)),
                Button.of("Español", p -> changeLanguage(p, Language.ES)),
                Button.of("Türkçe", p -> changeLanguage(p, Language.TR))
        );
    }

    private void changeLanguage(Player p, Language language) {
        new PlayerLanguageSwitchEvent(p, MessagesAPI.getLanguage(p), language).callEvent();
        MessagesAPI.setLanguage(p, language);
        MSG.Send.info(p, GlobalMessages.LANGUAGE__SWITCHED);
    }
}
