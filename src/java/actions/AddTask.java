package actions;

import controller.Operations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Add record about new task to database and task list
 */
public class AddTask extends TaskAction {
    
    /* Logger */
    private static final Logger logger = Logger.getLogger(AddTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        logger.info("ADD NEW TASK");
        try {
            Operations.connect(Operations.SETTINGS_FILE);
            Operations.addNewTask(request.getParameter("taskName"),
                    request.getParameter("taskParent"),
                    request.getParameter("taskUser"),
                    request.getParameter("taskBegin"),
                    request.getParameter("taskEnd"),
                    request.getParameter("taskStatus"));
            request.getSession().setAttribute("taskList", null);
            Operations.disconnect();
        } catch (Exception ex) {
            logger.error("Add exception", ex);
        }
        return "/index.jsp"; //redirected page path
    }
}
