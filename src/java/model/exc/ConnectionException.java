package model.exc;

public class ConnectionException extends Exception {
    
    private String message;

    public ConnectionException(String message) {
        this.message = message;
    }
    
    @Override
    public String toString() {
        return "ConnectionException: " + message;
    }

}
