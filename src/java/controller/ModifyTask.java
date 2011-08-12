package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AppProperties;
import model.DAOFactory;
import org.apache.log4j.Logger;

/**
 * Modify task by id
 */
public class ModifyTask  implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(ModifyTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        logger.info("MODIFY TASK");
        try {
            int id = Integer.parseInt(request.getParameter(AppProperties.getProperty("modify_id_param")));

            if (checkDateRange(request) == -1) {
                throw new Exception("Wrong date! Task not modified");
            } else {
                DAOFactory.getInstance().modifyTask(id, request.getParameter(AppProperties.getProperty("modify_name_param")),
                        request.getParameter(AppProperties.getProperty("modify_parent_param")),
                        request.getParameter(AppProperties.getProperty("modify_user_param")),
                        request.getParameter(AppProperties.getProperty("modify_begin_param")),
                        request.getParameter(AppProperties.getProperty("modify_end_param")),
                        request.getParameter(AppProperties.getProperty("modify_status_param")),
                        request.getParameter(AppProperties.getProperty("modify_descr_param")));
                request.getSession().setAttribute("message", "Task modify sucessfully!");
                request.getSession().setAttribute("messageType", "successMessage");
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("message", "Modify exception! " + ex.getMessage());
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Modify exception", ex);
        }
        return "/index.jsp"; //redirected page path
    }

    private int checkDateRange(HttpServletRequest request) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(request.getParameter(AppProperties.getProperty("modify_begin_param")));
            Date endDate = formatter.parse(request.getParameter(AppProperties.getProperty("modify_end_param")));
            return endDate.compareTo(beginDate);
        } catch (ParseException ex) {
            logger.error("Modify exception (date check range)", ex);
            return -1;
        }
    }

}
