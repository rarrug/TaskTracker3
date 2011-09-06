package controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.dao.DAOFactory;
import ttracker.dao.exc.TrackerException;
import ttracker.ejb.emp.EmpRecord;
import ttracker.ejb.task.TaskRecord;

/**
 * Modify task by id
 */
public class ModifyTask extends SomethingAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(ModifyTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        logger.info("MODIFY TASK");
        try {

            /* get parameters from request */
            Integer id = new Integer(request.getParameter(AppProperties.getProperty("modify_id_param")));
            String taskName = request.getParameter(AppProperties.getProperty("modify_name_param"));
            Integer taskParent = new Integer(request.getParameter(AppProperties.getProperty("modify_parent_param")));
            Date taskBegin = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("modify_begin_param")));
            Date taskEnd = new SimpleDateFormat("yyyy-MM-dd").parse(
                    request.getParameter(AppProperties.getProperty("modify_end_param")));
            String taskStatus = request.getParameter(AppProperties.getProperty("modify_status_param"));
            String taskUser = request.getParameter(AppProperties.getProperty("modify_user_param"));
            String taskDescr = request.getParameter(AppProperties.getProperty("modify_descr_param"));

            TaskRecord task = new TaskRecord(id, taskName, taskParent, taskBegin,
                    taskEnd, taskStatus, new EmpRecord(0, taskUser, null, null), taskDescr);

            /* verify and modify data in database */
            verifyData(task);
            DAOFactory.getInstance().modifyTask(task);
            setNotifyMessage(session, logger, SUCCESS_MESS, null, "Task modified", null);

        } catch (ParseException ex) {
            setNotifyMessage(session, logger, FAIL_MESS, "Modify exception", "Incorrect input data. Reason: " + ex.getMessage(), ex);
        } catch (TrackerException ex) {
            setNotifyMessage(session, logger, FAIL_MESS, "Modify exception", ex.getMessage(), ex);
        }
        return "/index.jsp"; //redirected page path
    }
}
