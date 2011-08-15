package controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AppProperties;
import model.DAOFactory;
import model.exc.ConnectionException;
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
            verifyData(request.getParameter(AppProperties.getProperty("add_name_param")),
                    request.getParameter(AppProperties.getProperty("add_begin_param")),
                    request.getParameter(AppProperties.getProperty("add_end_param")));

            DAOFactory.getInstance().addNewTask(request.getParameter(AppProperties.getProperty("add_name_param")),
                    request.getParameter(AppProperties.getProperty("add_parent_param")),
                    request.getParameter(AppProperties.getProperty("add_user_param")),
                    request.getParameter(AppProperties.getProperty("add_begin_param")),
                    request.getParameter(AppProperties.getProperty("add_end_param")),
                    request.getParameter(AppProperties.getProperty("add_status_param")),
                    request.getParameter(AppProperties.getProperty("add_descr_param")));
            setAddMessage(request, SUCCESS_MESS, "Task modaddedify sucessfully!", null);
        } catch (NumberFormatException ex) {
            setAddMessage(request, FAIL_MESS, "Incorrect ID: "
                    + request.getParameter(AppProperties.getProperty("modify_id_param")), ex);
        } catch (ParseException ex) {
            setAddMessage(request, FAIL_MESS, ex.getMessage(), ex);
        } catch (ConnectionException ex) {
            setAddMessage(request, FAIL_MESS, "Cannot connect to database.", ex);
        } catch (SQLException ex) {
            setAddMessage(request, FAIL_MESS, "Cannot exequte sql script.", ex);
        }
        return "/index.jsp"; //redirected page path
    }

    private void setAddMessage(HttpServletRequest request, int type, String value, Throwable t) {
        if (type == FAIL_MESS) {
            request.getSession().setAttribute("message", "Add exception. " + value);
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Add exception", t);
        } else {
            request.getSession().setAttribute("message", value);
            request.getSession().setAttribute("messageType", "successMessage");
        }
    }

    private void verifyData(String name, String begin, String end) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);
        if (name == null || "".equals(name)) {
            throw new ParseException("Task name cannot be empty", 0);
        } else if (endDate.compareTo(beginDate) == -1) {
            throw new ParseException("Wrong dates", 0);
        }
    }
}
