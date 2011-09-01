package controller;

import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.ejb.emp.EmpRecord;
import ttracker.dao.SQLConsts;
import ttracker.ejb.task.TaskHome;
import ttracker.ejb.task.TaskRecord;

/**
 * Modify task by id
 */
public class ModifyTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(ModifyTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        logger.info("MODIFY TASK");
        try {
            /* create tasks context */
            Context initial = new InitialContext();
            Object objRef = initial.lookup(SQLConsts.JNDI_TASK);
            TaskHome taskHome = (TaskHome) javax.rmi.PortableRemoteObject.narrow(
                    objRef, TaskHome.class);

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
            taskHome.modify(task);
            setNotifyMessage(session, SUCCESS_MESS, "Task sucessfully modified!", null);

        } catch (RemoteException ex) {
            setNotifyMessage(session, FAIL_MESS, "Modifing remote exception", ex);
        } catch (NamingException ex) {
            setNotifyMessage(session, FAIL_MESS, "Cannot find JNDI name", ex);
        } catch (NumberFormatException ex) {
            setNotifyMessage(session, FAIL_MESS, "Incorrect ID: "
                    + request.getParameter(AppProperties.getProperty("modify_id_param")), ex);
        } catch (ParseException ex) {
            setNotifyMessage(session, FAIL_MESS, "Incorrect name or dates", ex);
        }
        return "/index.jsp"; //redirected page path
    }

    /**
     * Write notify message to log and to session
     * @param session Session
     * @param type Succesfull or fail message const
     * @param value Message value
     * @param t Exception object if notify message is fail or null if it is succesfull
     */
    private void setNotifyMessage(HttpSession session, int type, String value, Throwable t) {
        if (type == FAIL_MESS) {
            session.setAttribute("message", "Modify exception. " + value);
            session.setAttribute("messageType", "failMessage");
            logger.error("Modify exception", t);
        } else {
            session.setAttribute("message", value);
            session.setAttribute("messageType", "successMessage");
        }
    }

    /**
     * Check input data
     * @param task Task object with parameters
     * @throws ParseException Data not valid
     */
    private void verifyData(TaskRecord task) throws ParseException {
        if (task.getName() == null || "".equals(task.getName())) {
            throw new ParseException("Task name cannot be empty", 0);
        } else if (task.getBegin().compareTo(task.getEnd()) == -1) {
            throw new ParseException("Wrong date", 0);
        }
    }
}
