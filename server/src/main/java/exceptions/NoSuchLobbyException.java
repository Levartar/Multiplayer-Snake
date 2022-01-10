package exceptions;

public class NoSuchLobbyException extends Exception {
    public NoSuchLobbyException() {
    }

    public NoSuchLobbyException(String message) {
        super(message);
    }
}
