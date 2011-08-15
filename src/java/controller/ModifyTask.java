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
 * Modify task by id
 */
public class ModifyTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(ModifyTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        logger.info("MODIFY TASK");
        try {
            int id = Integer.parseInt(request.getParameter(AppProperties.getProperty("modify_id_param")));
            verifyData(request.getParameter(AppProperties.getProperty("modify_name_param")),
                    request.getParameter(AppProperties.getProperty("modify_begin_param")),
                    request.getParameter(AppProperties.getProperty("modify_end_param")));
            DAOFactory.getInstance().modifyTask(id, request.getParameter(AppProperties.getProperty("modify_name_param")),
                    request.getParameter(AppProperties.getProperty("modify_parent_param")),
                    request.getParameter(AppProperties.getProperty("modify_user_param")),
                    request.getParameter(AppProperties.getProperty("modify_begin_param")),
                    request.getParameter(AppProperties.getProperty("modify_end_param")),
                    request.getParameter(AppProperties.getProperty("modify_status_param")),
                    request.getParameter(AppProperties.getProperty("modify_descr_param")));
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

    private void verifyData(String name, String begin, String end) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = formatter.parse(begin);
        Date endDate = formatter.parse(end);
        if (name == null || "".equals(name)) {
            throw new ParseException("Task name cannot be empty", 0);
        } else if (endDate.compareTo(beginDate) == -1) {
            throw new ParseException("Wrong date", 0);
        }
    }
}
