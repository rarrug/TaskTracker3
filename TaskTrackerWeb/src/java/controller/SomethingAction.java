
package controller;

import java.text.ParseException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.ejb.task.TaskRecord;

public abstract class SomethingAction implements IAction {
    
    public abstract String perform(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * Write notify message to log and to session
     * @param session Session
     * @param type Succesfull or fail message const
     * @param value Message value
     * @param exc Exception object if notify message is fail or null if it is succesfull
     */
    public void setNotifyMessage(HttpSession session, Logger logger, int type, 
            String from, String value, Exception exc) {
        if (type == FAIL_MESS) {
            session.setAttribute("message", "&lt;" + from + "&gt;" + value);
            session.setAttribute("messageType", "failMessage");
            logger.error(from, exc);
        } else {
            session.setAttribute("message", value);
            session.setAttribute("messageType", "successMessage");
        }
    }
    
    /**
     * Check input data
     * @param task Task object with parameters
     * @throws ParseException Data not valid
     */
    public void verifyData(TaskRecord task) throws ParseException {
        if (task.getName() == null || "".equals(task.getName())) {
            throw new ParseException("Task name cannot be empty", 0);
        } else if (task.getEnd().compareTo(task.getBegin()) == -1) {
            throw new ParseException("Wrong dates", 0);
        }
    }
    
}
