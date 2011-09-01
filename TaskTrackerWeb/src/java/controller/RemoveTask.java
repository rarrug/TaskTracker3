package controller;

import java.rmi.RemoteException;
import javax.ejb.RemoveException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.dao.SQLConsts;
import ttracker.ejb.task.TaskHome;

/**
 * Remove task by id from database and task list
 */
public class RemoveTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(RemoveTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {
            /* create tasks context */
            Context initial = new InitialContext();
            Object objRef = initial.lookup(SQLConsts.JNDI_TASK);
            TaskHome taskHome = (TaskHome) javax.rmi.PortableRemoteObject.narrow(
                    objRef, TaskHome.class);

            /* get id parameter from request */
            int id = Integer.parseInt(request.getParameter(AppProperties.getProperty("remove_param")));
            
            /* remove task by id from database */
            taskHome.remove(new Integer(id));
            setNotifyMessage(session, SUCCESS_MESS, "Task succesfully removed", null);

        } catch (RemoveException ex) {
            setNotifyMessage(session, FAIL_MESS, "Cannot remove object", null);
        } catch (RemoteException ex) {
            setNotifyMessage(session, FAIL_MESS, "Removing remote exception", null);
        } catch (NamingException ex) {
            setNotifyMessage(session, FAIL_MESS, "Cannot find JNDI name", ex);
        }
        return "/index.jsp"; //redirected page path
    }

    /**
     * Write notify message to log and to session
     * @param session Session
     * @param type Succesfull or fail message const
     * @param value Message value
     * @param t Exception object if notify is fail or null if succesfull
     */
    private void setNotifyMessage(HttpSession session, int type, String value, Throwable t) {
        if (type == FAIL_MESS) {
            session.setAttribute("message", "Remove exception. " + value);
            session.setAttribute("messageType", "failMessage");
            logger.error("Show exception", t);
        } else {
            session.setAttribute("message", value);
            session.setAttribute("messageType", "successMessage");
        }
    }
}