package actions;

import controller.Operations;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import model.Task;
import model.TaskList;
import org.apache.log4j.Logger;

/**
 * Generate task list by request
 */
public class ShowTask extends TaskAction {

    /*
     * Current index in user list.
     * Use in hierarchical output
     */
    private static int index;
    private static StringBuffer listBuilder;

    /* Logger */
    private static final Logger logger = Logger.getLogger(ShowTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {

        HttpSession session = request.getSession();
        TaskList taskList = (TaskList) session.getAttribute("taskList");

        String saveFormat = request.getParameter("save");
        if (saveFormat != null) {
            session.setAttribute("saveToFile", "1");
        }

        try {
            Operations.connect(Operations.SETTINGS_FILE);
            //Operations.connect2();

            /* find by id, name or user */
            String findKind = request.getParameter("findKind");
            if ("1".equals(findKind)) {/* by id */
                getListById(request);
            } else if ("2".equals(findKind)) {/* by name */
                getListByName(request);
            } else if ("3".equals(findKind)) {/* by user */
                getListByUser(request);
            } else if ("hier".equals(findKind)) {/* generate hierarchical list */
                logger.info("HIERARCHICAL SEARCH");
                taskList = new TaskList(Operations.getHierarchy());
                session.setAttribute("hierarchyTaskList", taskList);
                session.setAttribute("hierarchy", "1");
            }

            /*
             * Fill list if it does not exists or empty
             */
            if (taskList == null || taskList.getTaskList().isEmpty()) {
                logger.debug("UPDATE TASK LIST");
                taskList = new TaskList(Operations.getAllTasks("Task.id_task", "ASC"));
                session.setAttribute("taskList", taskList);
            }

            Operations.disconnect();
        } catch (Exception ex) {
            logger.error("Show exception", ex);
        }
        return "/showtask.jsp"; //redirected page path
    }
    
    public static void generateImportFile(Writer writer, Collection<Task> taskList) throws IOException {

        BufferedWriter bw = new BufferedWriter(writer);
        bw.write("<div align=center><textarea rows=\"30\" style=\"height:50%; width:80%; font-size:8pt;\" name=\"text\" id=\"saveArea\" wrap=\"off\" readonly=false>");
        bw.write("\"Тема\",\"Дата начала\",\"Срок\",\"Напоминание вкл/выкл\","
                + "\"Дата напоминания\",\"Время напоминания\",\"Дата завершения\","
                + "\"Готово,%\",\"Всего работы\",\"Проделано\",\"Важность\","
                + "\"Важность Schedule+\",\"Заметки\",\"Категории\",\"Контакты\","
                + "\"Организация\",\"Пометка\",\"Расстояние\",\"Роль\",\"Состояние\","
                + "\"Счета\",\"Частное\"\r\n");

        for (Task task : taskList) {
            Format formatter = new SimpleDateFormat("d.M.yyyy");
            bw.write("\"" + task.getName() + "\",\"" + formatter.format(task.getDateBegin())
                    + "\",\"" + formatter.format(task.getDateBegin())
                    + "\",\"Ложь\",,,,\"0.000\",\"0\",\"0\",\"Обычная\",,\"\",,,,"
                    + "\"Обычная\",,\"\",\"Выполняется\",,\"Ложь\"\r\n");
        }

        bw.write("</textarea></div>\n");
        bw.write("<hr/>\n");
        bw.write("<p>Copy this text and insert them into file <b>*.CSV</b> (Outlook file format)</p>\n");
        bw.close();
    }

    /**
     * Print hierarchical structure of task list to JSP page
     * @param out Out page stream
     * @param taskList User list
     */
    public static void printHirerachicalStructure(JspWriter out, TaskList taskList) {
        index = -1;
        listBuilder = new StringBuffer();

        try {

            listBuilder.append("<ul class=\"simpleTree\">\n").
                    append("<li class=\"root\" id='").append(index + 2).
                    append("'>ROOT\n").append("<ul>\n");

            while (index < taskList.getTaskList().size() - 1) {
                index++;
                printHierarchyNode(out, (Task) taskList.getTaskList().get(index), taskList.getTaskList(), "");
            }

            listBuilder.append("</ul>\n</li>\n</ul>\n");

            String listAfterReplace = listBuilder.toString().replaceAll("</ul>\n<ul>", "");
            out.println(listAfterReplace);
            logger.debug("HIERARCHICAL LIST BUILT");

        } catch (IOException e) {
            logger.error("Hierarchical exception", e);
        }
    }

    /**
     * Print parent node and its child nodes
     * @param out Out page stream
     * @param task Parent task
     * @param list User list
     * @param s Some charaters
     */
    public static void printHierarchyNode(JspWriter out, Task task, List list, String s) throws IOException {
        boolean flag = true;
        if (index != 0) {
            listBuilder.append((s + "<ul>\n"));
        }
        while (index < list.size()) {
            if (flag) {
                String classOpen = "";
                if (index == 0) {
                    classOpen = " class=\"open\"";
                }
                String item = (s + "<li id=\'" + (index + 2) + "\'" + classOpen + "><span>" + task.getId() + " - " + task.getName()
                        + " - " + task.getParentId() + "</span>");
                listBuilder.append(item).append("\n");
                flag = false;
            }
            if (index >= list.size() - 1) {
                break;
            }
            Task child = (Task) list.get(index + 1);
            if (child != null && task.getId() == child.getParentId()) {
                index++;
                printHierarchyNode(out, child, list, "");
            } else {
                break;
            }
        }
        listBuilder.append((s + "</li>\n"));
        if (index != 0) {
            listBuilder.append((s + "</ul>\n"));
        }
    }

    /**
     * Add attribute 'id' to session for sort task list by task id
     * @param request Request from JSP page
     */
    private void getListById(HttpServletRequest request) {
        logger.info("FIND BY ID");
        int id = Integer.parseInt(request.getParameter("findName"));
        request.getSession().setAttribute("findKind", "1");
        request.getSession().setAttribute("findName", "" + id);
    }

    /**
     * Add attribute 'name' to session for sort task list by task name
     * @param request Request from JSP page
     */
    private void getListByName(HttpServletRequest request) {
        logger.info("FIND BY NAME");
        request.getSession().setAttribute("findKind", "2");
        request.getSession().setAttribute("findName", request.getParameter("findName"));
    }

    /**
     * Add attribute 'user' to session for sort task list by user
     * @param request Request from JSP page
     */
    private void getListByUser(HttpServletRequest request) {
        logger.info("FIND BY USER");
        request.getSession().setAttribute("findKind", "3");
        request.getSession().setAttribute("findName", request.getParameter("findName"));
    }
}
