
import actions.IAction;
import actions.TaskActionFactory;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Task;
import model.TaskList;
import org.apache.log4j.Logger;

public class TaskServlet extends HttpServlet {

    private TaskActionFactory factory = new TaskActionFactory();

    /* Logger */
    //private static final Logger logger = Logger.getLogger(TaskServlet.class);
    public TaskServlet() {
        super();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IAction action = factory.create(getActionName(request));
        String url = action.perform(request, response);
        if (url != null) {
            //logger.debug("REDIRECT TO URL: " + url);
            getServletContext().getRequestDispatcher(url).forward(request, response);
        }
    }

    private String getActionName(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.substring(1, path.lastIndexOf("."));
    }
}
