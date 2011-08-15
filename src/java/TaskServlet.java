
import controller.IAction;
import controller.TaskActionFactory;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

public class TaskServlet extends HttpServlet {

    private TaskActionFactory factory = new TaskActionFactory();

    /* Logger */
    private static final Logger logger = Logger.getLogger(TaskServlet.class);

    public TaskServlet() {
        super();
    }

    @Override
    public void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        IAction action = factory.create(getActionName(request));
        String url = action.perform(request, response);
        if (url != null) {
            getServletContext().getRequestDispatcher(url).forward(request, response);
        } else {
            doDownload(request, response, "save_task_list.CSV", "task_list.CSV");
            logger.info("SAVED TASK LIST TO FILE");
        }
    }

    private String getActionName(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.substring(1, path.lastIndexOf("."));
    }

    private void doDownload(HttpServletRequest req, HttpServletResponse resp,
            String filename, String specifyName) throws IOException {
        File f = new File(filename);
        int length = 0;
        ServletOutputStream op = resp.getOutputStream();
        String mimetype = getServletConfig().getServletContext().getMimeType(filename);

        resp.setContentType((mimetype != null) ? mimetype : "application/octet-stream");
        resp.setContentLength((int) f.length());
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + specifyName + "\"");

        byte[] bbuf = new byte[1024];
        DataInputStream in = new DataInputStream(new FileInputStream(f));

        while ((in != null) && ((length = in.read(bbuf)) != -1)) {
            op.write(bbuf, 0, length);
        }

        in.close();
        op.flush();
        op.close();
    }
}
