package lt.itsvaidas.MenuAPI.geyser.elements;

import lt.itsvaidas.MenuAPI.geyser.interfaces.ICustomElement;

public class Dropdown implements ICustomElement {

    private final String id;
    private final Enum<?> text;
    private final Enum<?>[] enumOptions;
    private final String[] stringOptions;

    public Dropdown(String id, Enum<?> text, Enum<?>... options) {
        this.id = id;
        this.text = text;
        this.enumOptions = options;
        this.stringOptions = null;
    }

    public Dropdown(String id, Enum<?> text, String... options) {
        this.id = id;
        this.text = text;
        this.enumOptions = null;
        this.stringOptions = options;
    }

    @Override
    public String id() {
        return this.id;
    }

    public Enum<?> getText() {
        return text;
    }

    public Enum<?>[] getEnumOptions() {
        return enumOptions;
    }

    public String[] getStringOptions() {
        return stringOptions;
    }

    public static Dropdown of(String id, Enum<?> text, Enum<?>... options) {
        return new Dropdown(id, text, options);
    }

    public static Dropdown of(String id, Enum<?> text, String... options) {
        return new Dropdown(id, text, options);
    }
}
