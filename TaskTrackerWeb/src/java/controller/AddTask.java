package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.dao.DAOFactory;
import ttracker.ejb.emp.EmpRecord;
import ttracker.dao.exc.TrackerException;
import ttracker.ejb.task.TaskRecord;

/**
 * Add record about new task to database and task list
 */
public class AddTask extends SomethingAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(AddTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        logger.info("ADD NEW TASK");
        try {

            /* get parameters from request */
            String taskName = request.getParameter(AppProperties.getProperty("add_name_param"));
            Integer taskParent = new Integer(request.getParameter(AppProperties.getProperty("add_parent_param")));
            Date taskBegin = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("add_begin_param")));
            Date taskEnd = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("add_end_param")));
            String taskStatus = request.getParameter(AppProperties.getProperty("add_status_param"));
            String taskUser = request.getParameter(AppProperties.getProperty("add_user_param"));
            String taskDescr = request.getParameter(AppProperties.getProperty("add_descr_param"));

            TaskRecord task = new TaskRecord(new Integer(0), taskName, taskParent,
                    taskBegin, taskEnd, taskStatus, new EmpRecord(0, taskUser, null, null), taskDescr);

            /* verify and write to database */
            verifyData(task);
            DAOFactory.getInstance().addTask(task);
            setNotifyMessage(session, logger, SUCCESS_MESS, null, "Task added sucessfully!", null);

        } catch (ParseException ex) {
            setNotifyMessage(session, logger, FAIL_MESS, "Add exception", "Incorrect input data. Reason: " + ex.getMessage(), ex);
        } catch (TrackerException ex) {
            setNotifyMessage(session, logger, FAIL_MESS, "Add exception", ex.getMessage(), ex);
        }
        return "/index.jsp"; //redirected page path
    }

}
