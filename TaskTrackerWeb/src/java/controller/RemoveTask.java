package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.dao.DAOFactory;
import ttracker.dao.exc.TrackerException;

/**
 * Remove task by id from database and task list
 */
public class RemoveTask extends SomethingAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(RemoveTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        
        /* get id parameter from request */
        int id = Integer.parseInt(request.getParameter(AppProperties.getProperty("remove_param")));
        
        try {
            /* remove task by id from database */
            DAOFactory.getInstance().deleteTask(id);
            setNotifyMessage(session, logger, SUCCESS_MESS, null, "Task succesfully removed!", null);
        } catch (TrackerException ex) {
            setNotifyMessage(session, logger, FAIL_MESS, "Remove action", "Cannot remove task: " + ex.getMessage(), ex);
        }
        

        return "/index.jsp"; //redirected page path
    }

}