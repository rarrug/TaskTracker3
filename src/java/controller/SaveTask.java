package controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Format;
import java.text.SimpleDateFormat;
import javax.servlet.http.HttpSession;
import model.AppProperties;
import model.DAOFactory;
import model.Task;
import org.apache.log4j.Logger;

public class SaveTask implements IAction {

    /* Logger */
    private static final Logger logger = Logger.getLogger(SaveTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(AppProperties.getProperty("exp_file")));
            bw.write(AppProperties.getProperty("exp_task_header"));

            for (Task task : DAOFactory.getInstance().getTaskList(false)) {
                Format formatter = new SimpleDateFormat(AppProperties.getProperty("exp_date_format"));
                String status = task.getStatus();
                if (status.equals(AppProperties.getProperty("exp_task_open_en"))) {
                    status = AppProperties.getProperty("exp_task_open_ru");
                } else if (status.equals(AppProperties.getProperty("exp_task_process_en"))) {
                    status = AppProperties.getProperty("exp_task_process_ru");
                } else {
                    status = AppProperties.getProperty("exp_task_close_ru");
                }
                bw.write("\"" + task.getName() + "\",\"" + formatter.format(task.getBegin())
                        + "\",\"" + formatter.format(task.getEnd())
                        + "\",\"Ложь\",,,,\"0.000\",\"0\",\"0\",\"Обычная\",,\"" + task.getDescription() + "\",,,,"
                        + "\"Обычная\",,,\"" + status + "\",,\"Ложь\"\r\n");
            }
        } catch (Exception ex) {
            request.getSession().setAttribute("message", "Save exception! " + ex.getMessage());
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Save error", ex);
        } finally {
            try {
                bw.close();
            } catch (IOException ex) {
                request.getSession().setAttribute("message", "Save exception! " + ex.getMessage());
                request.getSession().setAttribute("messageType", "failMessage");
                logger.error("Save error", ex);
            }
        }
        return null;
    }
}
