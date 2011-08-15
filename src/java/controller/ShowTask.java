package controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.AppProperties;
import model.DAOFactory;
import model.Task;
import org.apache.log4j.Logger;
import weblogic.jms.utils.Simple;

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

        try {
    
            /* modify request (by id)*/
            
            String taskid = request.getParameter(AppProperties.getProperty("modify_req_param"));
            if (taskid != null) {
                int id = Integer.parseInt(taskid);
                Task task = ((List<Task>) DAOFactory.getInstance().getTaskById(id)).get(0);
                session.setAttribute("modifyTask", task);
                System.out.println("modifyTask.description: " + task.getDescription());
            } 
            
            /* find by id, name or user or hierarchical view */
            String findParameter = request.getParameter(AppProperties.getProperty("find_param"));
            if (findParameter != null) {
                if ("by_id".equals(findParameter)) {/* by id */
                    logger.info("FIND BY ID");
                    session.setAttribute("taskList", DAOFactory.getInstance().
                            getTaskById(Integer.parseInt(request.getParameter(AppProperties.getProperty("find_value")))));
                } else if ("by_name".equals(findParameter)) {/* by name */
                    logger.info("FIND BY NAME");
                    session.setAttribute("taskList", DAOFactory.getInstance().
                            getTaskByName(request.getParameter(AppProperties.getProperty("find_value"))));
                } else if ("by_user".equals(findParameter)) {/* by user */
                    logger.info("FIND BY User");
                    session.setAttribute("taskList", DAOFactory.getInstance().
                            getTaskByUser(request.getParameter(AppProperties.getProperty("find_value"))));
                } else if (AppProperties.getProperty("hierarchical_value").equals(findParameter)) {/* generate hierarchical list */
                    logger.info("HIERARCHICAL SEARCH");
                    session.setAttribute("hierarchyList", printHirerachicalStructure(
                            (List<Task>) DAOFactory.getInstance().getTaskList(true)));
                    session.setAttribute("hierarchy", "1");
                }
            } else {
                logger.debug("UPDATE TASK LIST");
                session.setAttribute("taskList", DAOFactory.getInstance().getTaskList(false));
                session.setAttribute("userList", DAOFactory.getInstance().getUserList());
                session.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
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
    public static String printHirerachicalStructure(List<Task> taskList) {
        String listAfterReplace = null;
        index = -1;
        listBuilder = new StringBuffer();

        try {
            listBuilder.append("<ul class=\"simpleTree\">\n").
                    append("<li class=\"root\" id='").append(index + 2).
                    append("'>ROOT\n").append("<ul>\n");

            while (index < taskList.size() - 1) {
                index++;
                printHierarchyNode((Task) taskList.get(index), taskList, "");
            }

            listBuilder.append("</ul>\n</li>\n</ul>\n");
            listAfterReplace = listBuilder.toString().replaceAll("</ul>\n<ul>", "");
            logger.debug("HIERARCHICAL LIST BUILT");
        } catch (IOException e) {
            logger.error("Hierarchical exception", e);
        } finally {
            return listAfterReplace;
        }
    }

    /**
     * Print parent node and its child nodes
     * @param out Out page stream
     * @param task Parent task
     * @param list User list
     * @param s Some charaters
     */
    public static void printHierarchyNode(Task task, List<Task> list, String s) throws IOException {
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
                printHierarchyNode(child, list, "");
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
