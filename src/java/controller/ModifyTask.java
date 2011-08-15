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
 * Modify task by id
 */
public class ModifyTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(ModifyTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        logger.info("MODIFY TASK");
        try {
            int id = Integer.parseInt(request.getParameter(AppProperties.getProperty("modify_id_param")));
            String taskName = request.getParameter(AppProperties.getProperty("modify_name_param"));
            int taskParent = Integer.parseInt(request.getParameter(AppProperties.getProperty("modify_parent_param")));
            Date taskBegin = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("modify_begin_param")));
            Date taskEnd = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("modify_end_param")));
            String taskStatus = request.getParameter(AppProperties.getProperty("modify_status_param"));
            String taskUser = request.getParameter(AppProperties.getProperty("modify_user_param"));
            String taskDescr = request.getParameter(AppProperties.getProperty("modify_descr_param"));

            Task task = new Task(id, taskName, taskParent, taskBegin, taskEnd, taskStatus, taskUser, "", taskDescr);
            
            verifyData(task);
           
            DAOFactory.getInstance().modifyTask(task);
            
            setModifyMessage(request, SUCCESS_MESS, "Task modify sucessfully!", null);
        } catch (NumberFormatException ex) {
            setModifyMessage(request, FAIL_MESS, "Incorrect ID: "
                    + request.getParameter(AppProperties.getProperty("modify_id_param")), ex);
        } catch (ParseException ex) {
            setModifyMessage(request, FAIL_MESS, "Incorrect name or dates.", ex);
        } catch (ConnectionException ex) {
            setModifyMessage(request, FAIL_MESS, "Cannot connect to database.", ex);
        } catch (SQLException ex) {
            setModifyMessage(request, FAIL_MESS, "Cannot exequte sql script.", ex);
        }
        return "/index.jsp"; //redirected page path
    }

    private void setModifyMessage(HttpServletRequest request, int type, String value, Throwable t) {
        if (type == FAIL_MESS) {
            request.getSession().setAttribute("message", "Modify exception. " + value);
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Modify exception", t);
        } else {
            request.getSession().setAttribute("message", value);
            request.getSession().setAttribute("messageType", "successMessage");
        }
    }

    private void verifyData(Task task) throws ParseException {
        if (task.getName() == null || "".equals(task.getName())) {
            throw new ParseException("Task name cannot be empty", 0);
        } else if (task.getEnd().compareTo(task.getBegin()) == -1) {
            throw new ParseException("Wrong date", 0);
        }
    }
}
