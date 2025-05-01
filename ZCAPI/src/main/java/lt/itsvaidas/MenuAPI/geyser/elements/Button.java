package lt.itsvaidas.MenuAPI.geyser.elements;

import lt.itsvaidas.MenuAPI.geyser.interfaces.IButtonAction;

public class Button {

    private final String stringText;
    private final Enum<?> enumText;
    private final IButtonAction action;

    public Button(Enum<?> text, IButtonAction action) {
        this.stringText = null;
        this.enumText = text;
        this.action = action;
    }

    public Button(String text, IButtonAction action) {
        this.stringText = text;
        this.enumText = null;
        this.action = action;
    }

    public String getStringText() {
        return stringText;
    }

    public Enum<?> getEnumText() {
        return enumText;
    }

    public IButtonAction getAction() {
        return action;
    }

    public static Button of(Enum<?> text, IButtonAction action) {
        return new Button(text, action);
    }

    public static Button of(String text, IButtonAction action) {
        return new Button(text, action);
    }
}
