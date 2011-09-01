package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Save task list to file 
 */
public class SaveTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(SaveTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(AppProperties.getProperty("exp_file")));
            bw.write(AppProperties.getProperty("exp_task_header"));
        } catch (IOException ex) {
            request.getSession().setAttribute("message", "Save exception! " + ex.getMessage());
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Save error", ex);
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
            } catch (IOException ex) {
                request.getSession().setAttribute("message", "Save exception! " + ex.getMessage());
                request.getSession().setAttribute("messageType", "failMessage");
                logger.error("Save error", ex);
            }
        }
        return null;
    }
}
