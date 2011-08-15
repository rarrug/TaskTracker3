package controller;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.AppProperties;
import model.DAOFactory;
import model.Task;
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
            String taskName = request.getParameter(AppProperties.getProperty("add_name_param"));
            int taskParent = Integer.parseInt(request.getParameter(AppProperties.getProperty("add_parent_param")));
            Date taskBegin = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("add_begin_param")));
            Date taskEnd = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("add_end_param")));
            String taskStatus = request.getParameter(AppProperties.getProperty("add_status_param"));
            String taskUser = request.getParameter(AppProperties.getProperty("add_user_param"));
            String taskDescr = request.getParameter(AppProperties.getProperty("add_descr_param"));

            Task task = new Task(0, taskName, taskParent, taskBegin, taskEnd, taskStatus, taskUser, "", taskDescr);
            
            verifyData(task);
            DAOFactory.getInstance().addNewTask(task);
            
            setAddMessage(request, SUCCESS_MESS, "Task addedify sucessfully!", null);
        } catch (NumberFormatException ex) {
            setAddMessage(request, FAIL_MESS, "Incorrect data: "
                    + ex.getMessage(), ex);
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

    private void verifyData(Task task) throws ParseException {
        if (task.getName() == null || "".equals(task.getName())) {
            throw new ParseException("Task name cannot be empty", 0);
        } else if (task.getEnd().compareTo(task.getBegin()) == -1) {
            throw new ParseException("Wrong dates", 0);
        }
    }
}
