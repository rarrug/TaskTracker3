package ttracker.dao.exc;


public class TrackerException extends Exception {
    
    private String message;
    
    public TrackerException() {        
    }
    
    public TrackerException(String message) {
        this.message = message;
    }
    
    public String getMessage() {
        return message;
    }
}
