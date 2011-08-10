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
 * Add record about new task to database and task list
 */
public class AddTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(AddTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        logger.info("ADD NEW TASK");
        try {
            if (checkDateRange(request) == -1) {
                throw new Exception("Wrong date! Task not added");
            } else {
                DAOFactory.getInstance().addNewTask(request.getParameter(AppProperties.getProperty("add_name_param")),
                        request.getParameter(AppProperties.getProperty("add_parent_param")),
                        request.getParameter(AppProperties.getProperty("add_user_param")),
                        request.getParameter(AppProperties.getProperty("add_begin_param")),
                        request.getParameter(AppProperties.getProperty("add_end_param")),
                        request.getParameter(AppProperties.getProperty("add_status_param")),
                        request.getParameter(AppProperties.getProperty("add_descr_param")));
                request.getSession().setAttribute("message", "Task added sucessfully!");
                request.getSession().setAttribute("messageType", "successMessage");
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("message", "Add exception! " + ex.getMessage());
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Add exception", ex);
        }
        return "/index.jsp"; //redirected page path
    }

    private int checkDateRange(HttpServletRequest request) {
        try {

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date beginDate = formatter.parse(request.getParameter(AppProperties.getProperty("add_begin_param")));
            Date endDate = formatter.parse(request.getParameter(AppProperties.getProperty("add_end_param")));
            return endDate.compareTo(beginDate);
        } catch (ParseException ex) {
            logger.error("Add exception (date check range)", ex);
            return -1;
        }
    }
}
