package lt.itsvaidas.MenuAPI.geyser.forms;

import lt.itsvaidas.MenuAPI.geyser.elements.Button;
import lt.itsvaidas.MenuAPI.geyser.interfaces.ICloseForm;
import lt.itsvaidas.MenuAPI.geyser.interfaces.IForm;
import lt.itsvaidas.MessagesAPI.MessagesAPI;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;

public class ModalForm implements IForm {

    private final String title;
    private final Enum<?> message;
    private final Button button1;
    private final Button button2;
    private final ICloseForm onClose;

    public ModalForm(String title, Enum<?> message, Button button1, Button button2, ICloseForm onClose) {
        this.title = title;
        this.message = message;
        this.button1 = button1;
        this.button2 = button2;
        this.onClose = onClose;
    }

    @Override
    public Form build(FloodgateApi api, Player player) {
        var builder = org.geysermc.cumulus.form.ModalForm.builder()
                .title(this.title)
                .content(MessagesAPI.getString(player, this.message))
                .button1(this.button1.getStringText() == null ? MessagesAPI.getString(player, this.button1.getEnumText()) : this.button1.getStringText())
                .button2(this.button2.getStringText() == null ? MessagesAPI.getString(player, this.button2.getEnumText()) : this.button2.getStringText());

        builder.closedOrInvalidResultHandler(() -> {
            boolean close = onClose.onCloseForm();
            if (!close) {
                api.sendForm(player.getUniqueId(), builder);
            }
        });

        builder.validResultHandler(result -> {
            int index = result.clickedButtonId();
            if (index == 0) {
                button1.getAction().onClick(player);
            } else {
                button2.getAction().onClick(player);
            }
        });

        return builder.build();
    }

    public static ModalForm of(String title, Enum<?> message, Button button1, Button button2, ICloseForm onClose) {
        return new ModalForm(title, message, button1, button2, onClose);
    }
}
