package lt.itsvaidas.MenuAPI.geyser.elements;

import lt.itsvaidas.MenuAPI.geyser.interfaces.ICustomElement;

public class Dropdown implements ICustomElement {

    private final String id;
    private final String text;
    private final String[] stringOptions;

    public Dropdown(String id, String text, String... options) {
        this.id = id;
        this.text = text;
        this.stringOptions = options;
    }

    @Override
    public String id() {
        return this.id;
    }

    public String getText() {
        return text;
    }

    public String[] getStringOptions() {
        return stringOptions;
    }

    public static Dropdown of(String id, String text, String... options) {
        return new Dropdown(id, text, options);
    }
}
