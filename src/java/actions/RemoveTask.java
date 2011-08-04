package actions;

import controller.Operations;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import model.TaskList;
import org.apache.log4j.Logger;

/**
 * Remove task by id from database and task list
 */
public class RemoveTask extends TaskAction {
    
    /* Logger */
    private static final Logger logger = Logger.getLogger(RemoveTask.class);
    
    public String perform(HttpServletRequest request, HttpServletResponse response) {
        try {
            int id = Integer.parseInt(request.getParameter("rem"));
            Operations.connect(Operations.SETTINGS_FILE);
            Operations.deleteTask(id);
            Operations.disconnect();
            TaskList taskList = (TaskList)request.getSession().getAttribute("taskList");
            taskList.removeByID(id);
            request.getSession().setAttribute("taskList", taskList);
        } catch (Exception ex) {
            logger.error("Remove exception", ex);
        }
        return "/index.jsp"; //redirected page path
    }
}