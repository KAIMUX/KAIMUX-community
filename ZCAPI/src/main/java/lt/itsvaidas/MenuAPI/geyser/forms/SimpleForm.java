package lt.itsvaidas.MenuAPI.geyser.forms;

import lt.itsvaidas.MenuAPI.geyser.elements.Button;
import lt.itsvaidas.MenuAPI.geyser.interfaces.ICloseForm;
import lt.itsvaidas.MenuAPI.geyser.interfaces.IForm;
import lt.itsvaidas.MessagesAPI.MSG;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;

public class SimpleForm implements IForm {

    private final String title;
    private final ICloseForm onClose;
    private final Button[] buttons;

    public SimpleForm(String title, ICloseForm onClose, Button... buttons) {
        this.title = title;
        this.onClose = onClose;
        this.buttons = buttons;
    }

    @Override
    public Form build(FloodgateApi api, Player player) {
        var builder = org.geysermc.cumulus.form.SimpleForm.builder()
                .title(this.title);

        for (Button button : buttons) {
            Component text = button.getStringText() == null ? MSG.rawLine(player, button.getEnumText()) : MSG.raw(button.getStringText());
            String plainText = MSG.toLegacy(text);
            builder.button(plainText);
        }

        builder.closedOrInvalidResultHandler(() -> {
            boolean close = onClose.onCloseForm();
            if (!close) {
                api.sendForm(player.getUniqueId(), builder);
            }
        });

        builder.validResultHandler(result -> {
            int index = result.clickedButtonId();
            buttons[index].getAction().onClick(player);
        });

        return builder.build();
    }

    public static SimpleForm of(String title, ICloseForm onClose, Button... buttons) {
        return new SimpleForm(title, onClose, buttons);
    }
}
