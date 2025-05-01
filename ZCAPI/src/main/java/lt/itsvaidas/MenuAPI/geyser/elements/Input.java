package lt.itsvaidas.MenuAPI.geyser.elements;

import lt.itsvaidas.MenuAPI.geyser.interfaces.ICustomElement;

public class Input implements ICustomElement {

    private final String id;
    private final String text;
    private final String placeholder;

    public Input(String id, String text, String placeholder) {
        this.id = id;
        this.text = text;
        this.placeholder = placeholder;
    }

    public String id() {
        return this.id;
    }

    public String getText() {
        return text;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public static Input of(String id, String text, String placeholder) {
        return new Input(id, text, placeholder);
    }
}
