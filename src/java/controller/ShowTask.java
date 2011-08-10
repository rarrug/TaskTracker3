package controller;

import java.sql.SQLException;
import model.Operations;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspWriter;
import model.AppProperties;
import model.DAOFactory;
import model.Task;
import org.apache.log4j.Logger;

/**
 * Generate task list by request
 */
public class ShowTask implements IAction {

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
        //Collection<Task> taskList = (Collection<Task>) session.getAttribute("taskList");

        try {

            /* find by id, name or user or hierarchical view */
            String findParameter = request.getParameter(AppProperties.getProperty("find_param"));
            if (findParameter != null) {
                if ("1".equals(findParameter)) {/* by id */
                    logger.info("FIND BY ID");
                    session.setAttribute("taskList", DAOFactory.getInstance().
                            getTaskById(Integer.parseInt(request.getParameter("findName"))));
                } else if ("2".equals(findParameter)) {/* by name */
                    logger.info("FIND BY NAME");
                    session.setAttribute("taskList", DAOFactory.getInstance().
                            getTaskByName(request.getParameter("findName")));
                } else if ("3".equals(findParameter)) {/* by user */
                    logger.info("FIND BY User");
                    session.setAttribute("taskList", DAOFactory.getInstance().
                            getTaskByUser(request.getParameter("findName")));
                } else if (AppProperties.getProperty("hierarchical_value").equals(findParameter)) {/* generate hierarchical list */
                    logger.info("HIERARCHICAL SEARCH");
                    session.setAttribute("hierarchyList", DAOFactory.getInstance().getTaskList(true));
                    session.setAttribute("hierarchy", "1");
                }
            } else {
                logger.debug("UPDATE TASK LIST");
                session.setAttribute("taskList", DAOFactory.getInstance().getTaskList(false));
            }

        } catch (Exception ex) {
            request.getSession().setAttribute("message", "Show exception! " + ex.getMessage());
            request.getSession().setAttribute("messageType", "failMessage");
            logger.error("Show exception", ex);
        }
        return "/showtask.jsp"; //redirected page path
    }

    /**
     * Print hierarchical structure of task list to JSP page
     * @param out Out page stream
     * @param taskList User list
     */
    public static void printHirerachicalStructure(JspWriter out, List<Task> taskList) {
        index = -1;
        listBuilder = new StringBuffer();

        try {
            listBuilder.append("<ul class=\"simpleTree\">\n").
                    append("<li class=\"root\" id='").append(index + 2).
                    append("'>ROOT\n").append("<ul>\n");

            while (index < taskList.size() - 1) {
                index++;
                printHierarchyNode(out, (Task) taskList.get(index), taskList, "");
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
    public static void printHierarchyNode(JspWriter out, Task task, List<Task> list, String s) throws IOException {
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
                String descr = task.getDescription();
                if (descr.length() > 30) {
                    descr = descr.substring(0, 30) + "...";
                }
                String item = (s + "<li id=\'" + (index + 2) + "\'" + classOpen + "><span>" + task.getId() + " - " + task.getName()
                        + " - " + descr + "</span>");
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
   
}
