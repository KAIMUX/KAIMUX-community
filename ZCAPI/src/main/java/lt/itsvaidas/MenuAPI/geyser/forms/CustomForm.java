package lt.itsvaidas.MenuAPI.geyser.forms;

import lt.itsvaidas.MenuAPI.geyser.elements.Input;
import lt.itsvaidas.MenuAPI.geyser.elements.Dropdown;
import lt.itsvaidas.MenuAPI.geyser.interfaces.ICloseForm;
import lt.itsvaidas.MenuAPI.geyser.interfaces.ICustomElement;
import lt.itsvaidas.MenuAPI.geyser.interfaces.IForm;
import lt.itsvaidas.MenuAPI.geyser.interfaces.ISubmitForm;
import org.bukkit.entity.Player;
import org.geysermc.cumulus.form.Form;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class CustomForm implements IForm {

    private final String title;
    private final ISubmitForm onSubmit;
    private final ICloseForm onClose;
    private final ICustomElement[] elements;

    public CustomForm(String title, ISubmitForm onSubmit, ICloseForm onClose, ICustomElement... elements) {
        this.title = title;
        this.onSubmit = onSubmit;
        this.onClose = onClose;
        this.elements = elements;
    }

    @Override
    public Form build(FloodgateApi api, Player player) {
        var builder = org.geysermc.cumulus.form.CustomForm.builder()
                .title(this.title);

        builder.closedOrInvalidResultHandler(() -> {
            boolean close = onClose.onCloseForm();
            if (!close) {
                api.sendForm(player.getUniqueId(), builder);
            }
        });

        for (ICustomElement element : elements) {
            switch (element) {
                case Dropdown dropdown -> builder.dropdown(dropdown.getText(), dropdown.getStringOptions());
                case Input input -> builder.input(input.getText(), input.getPlaceholder());
                default -> throw new IllegalStateException("Unexpected value: " + element);
            }
        }

        builder.validResultHandler(result -> {
            Map<String, Object> values = new HashMap<>();

            for (int i = 0; i < elements.length; i++) {
                ICustomElement element = elements[i];
                switch (element) {
                    case Dropdown dropdown -> values.put(dropdown.id(), result.asDropdown(i));
                    case Input input -> values.put(input.id(), result.asInput(i));
                    default -> throw new IllegalStateException("Unexpected value: " + element);
                }
            }

            onSubmit.submit(values);
        });

        return builder.build();
    }

    public static CustomForm of(String title, ISubmitForm onSubmit, ICloseForm onClose, ICustomElement... elements) {
        return new CustomForm(title, onSubmit, onClose, elements);
    }
}
