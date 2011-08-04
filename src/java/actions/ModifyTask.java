package actions;

import controller.Operations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Modify task by id
 */
public class ModifyTask extends TaskAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(ModifyTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        logger.info("MODIFY TASK");
        try {
            int id = Integer.parseInt(request.getParameter("modifyId"));
            Operations.connect(Operations.SETTINGS_FILE);
            Operations.modifyTask(id, request.getParameter("modifyName"),
                    request.getParameter("modifyParent"),
                    request.getParameter("modifyUser"),
                    request.getParameter("modifyBegin"),
                    request.getParameter("modifyEnd"),
                    request.getParameter("modifyStatus"));
            Operations.disconnect();
            request.getSession().setAttribute("taskList", null);
        } catch (Exception ex) {
            logger.error("Modify exception", ex);
        }
        return "/index.jsp"; //redirected page path
    }
}
