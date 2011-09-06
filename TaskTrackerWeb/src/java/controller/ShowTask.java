package controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import ttracker.dao.DAOFactory;
import ttracker.ejb.task.Task;
import ttracker.dao.exc.TrackerException;

/**
 * Generate task list by request
 */
public class ShowTask extends SomethingAction {

    /* Current index in user list  */
    private static int index;
    /* Save string with hierarchical list */
    private static StringBuffer listBuilder;

    /* Logger */
    private static final Logger logger = Logger.getLogger(ShowTask.class);

    public String perform(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        try {

            /* modify task by id */
            String taskid = request.getParameter(AppProperties.getProperty("modify_req_param"));
            if (taskid != null) {
                Integer id = new Integer(taskid);
                session.setAttribute("modifyTask", DAOFactory.getInstance().getTaskById(id));
            }

            /* find by id, name or user or hierarchical view */
            String findParameter = request.getParameter(AppProperties.getProperty("find_param"));
            if (findParameter != null) {
                String findValue = request.getParameter(AppProperties.getProperty("find_value"));

                if (AppProperties.getProperty("hierarchical_value").equals(findParameter)) {/* generate hierarchical list */
                    logger.info("HIERARCHICAL SEARCH");
                    Collection hier = DAOFactory.getInstance().getAllTasks(true);
                    String hStr = buildHirerachicalStructure((List<Task>) hier);
                    session.setAttribute("hierarchyList", hStr);
                    session.setAttribute("hierarchy", "1");
                } else { /* search */
                    Collection taskList = null;
                    if ("by_id".equals(findParameter)) {/* by id */
                        logger.info("FIND BY ID");
                        taskList = new ArrayList();
                        taskList.add(DAOFactory.getInstance().getTaskById(new Integer(findValue)));
                    } else if ("by_name".equals(findParameter)) {/* by name */
                        logger.info("FIND BY NAME");
                        taskList = DAOFactory.getInstance().getTaskByName(findValue);
                    } else if ("by_user".equals(findParameter)) {/* by user */
                        logger.info("FIND BY User");
                        taskList = DAOFactory.getInstance().getTaskByEmp(findValue);
                    }
                    session.setAttribute("taskList", taskList);
                }
            } else {/* build full task list and employee list */
                logger.debug("UPDATE TASK LIST");
                session.setAttribute("taskList", DAOFactory.getInstance().getAllTasks(false));

                /* write data to session */
                session.setAttribute("userList", DAOFactory.getInstance().getAllEmps());
                session.setAttribute("today", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            }

        } catch (TrackerException ex) {
            setNotifyMessage(session, logger, FAIL_MESS, "Show exception", ex.getMessage(), ex);
        }
        return "/showtask.jsp"; //redirected page path
    }

    

    /**
     * Print hierarchical structure of task list to JSP page
     * @param out Out page stream
     * @param taskList User list
     */
    public static String buildHirerachicalStructure(List<Task> taskList) {
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
            /*} catch (IOException e) {
            logger.error("Hierarchical exception", e);*/
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
                if (descr == null) {
                    descr = "&lt;empty desrc&gt;";
                } else if (descr.length() > 30) {
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
            if (child != null && task.getId().compareTo(child.getParentId()) == 0) {
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
