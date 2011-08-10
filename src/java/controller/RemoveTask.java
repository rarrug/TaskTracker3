package controller;

import model.DBModel;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AppProperties;
import model.DAOFactory;
import model.TaskList;
import org.apache.log4j.Logger;

/**
 * Remove task by id from database and task list
 */
public class RemoveTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(RemoveTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter(AppProperties.getProperty("remove_param")));
            DAOFactory.getInstance().deleteTask(id);
            request.getSession().setAttribute("message", "Task removed sucessfully!");
            request.getSession().setAttribute("messageType", "successMessage");
        } catch (Exception ex) {
            request.getSession().setAttribute("message", "Remove exception! " + ex.getMessage());
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Remove exception", ex);
        }
        return "/index.jsp"; //redirected page path
    }
}