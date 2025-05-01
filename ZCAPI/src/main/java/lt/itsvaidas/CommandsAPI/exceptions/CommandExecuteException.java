package lt.itsvaidas.CommandsAPI.exceptions;

public class CommandExecuteException extends Exception {
    private final Enum<?> message;

    public CommandExecuteException(Enum<?> message) {
        this.message = message;
    }

    public Enum<?> getEnumMessage() {
        return message;
    }
}
