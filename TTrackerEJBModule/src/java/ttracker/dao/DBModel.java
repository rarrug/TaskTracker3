package ttracker.dao;

import java.rmi.RemoteException;
import javax.naming.NamingException;
import java.util.Collection;
import javax.ejb.FinderException;
import javax.naming.Context;
import javax.naming.InitialContext;
import ttracker.dao.exc.TrackerException;
import ttracker.ejb.emp.Emp;
import ttracker.ejb.emp.EmpHome;
import ttracker.ejb.task.Task;
import ttracker.ejb.task.TaskHome;
import ttracker.ejb.task.TaskRecord;

/**
 * Make operation with database
 */
public class DBModel implements IModel {

    private TaskHome taskHome;
    private EmpHome empHome;

    public DBModel() {
        try {
            /* initialize task home and emp home contexts */
            Context initial = new InitialContext();
            Object objRef = initial.lookup(SQLConsts.JNDI_TASK);
            taskHome = (TaskHome) javax.rmi.PortableRemoteObject.narrow(
                    objRef, TaskHome.class);
            objRef = initial.lookup(SQLConsts.JNDI_EMP);
            empHome = (EmpHome) javax.rmi.PortableRemoteObject.narrow(
                    objRef, EmpHome.class);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Select task by id
     * @param taskId Task number
     * @return Task with identificator id
     */
    public Task getTaskById(Integer taskId) throws TrackerException {
        Task task = null;
        try {
            task = taskHome.findByPrimaryKey(taskId);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task by id: " + ex.getMessage());
        } finally {
            return task;
        }
    }

    /**
     * Get list of task keys by some string parameter 
     * @param param Some string parameter (name, user name, etc.)
     * @param sql SQL query
     * @return
     */
    private Collection getTaskByParam(String param, String sql) throws FinderException, RemoteException {
        Collection list = null;
        if (sql.equals(SQLConsts.SELECT_TASK_BY_NAME)) {
            list = taskHome.findByName(param);
        } else if (sql.equals(SQLConsts.SELECT_TASK_BY_EMP)) {
            list = taskHome.findByEmp(param);
        }
        return list;

    }

    /**
     * Select task by name
     * @param tName Task name
     * @return List of tasks with name - tName
     */
    public Collection getTaskByName(String tName) throws TrackerException {
        Collection list = null;
        try {
            list = getTaskByParam(tName, SQLConsts.SELECT_TASK_BY_NAME);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task by name: " + ex.getMessage());
        }
        return list;
    }

    /**
     * Select task by employee name
     * @param eName Employee name
     * @return List of tasks with employee name - eName
     */
    public Collection getTaskByEmp(String eName) throws TrackerException {
        Collection list = null;
        try {
            list = getTaskByParam(eName, SQLConsts.SELECT_TASK_BY_EMP);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task by emp name: " + ex.getMessage());
        }
        return list;
    }

    /**
     * Get full task list
     * @param hier true if need hierarchical list or false another
     * @return Task list
     */
    public Collection getAllTasks(boolean hier) throws TrackerException {
        Collection list = null;
        try {
            list = taskHome.findAll(hier);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get task list: " + ex.getMessage());
        } finally {
            return list;
        }
    }

    /**
     * Add task record to database
     * @param newTask New task object
     */
    public void addTask(TaskRecord newTask) throws TrackerException {
        try {
            taskHome.create(newTask);
        } catch (Exception ex) {
            throw new TrackerException("Cannot add task: " + ex.getMessage());
        }
    }

    /**
     * Delete task from database by id
     * @param taskId Task number
     */
    public void deleteTask(Integer taskId) throws TrackerException {
        try {
            taskHome.findByPrimaryKey(taskId).remove();
        } catch (Exception ex) {
            throw new TrackerException("Cannot delete task: " + ex.getMessage());
        }
    }

    /**
     * Update task in database
     * @param task Modify task object
     */
    public void modifyTask(TaskRecord task) throws TrackerException {
        try {
            taskHome.modify(task);
        } catch (RemoteException ex) {
            throw new TrackerException("Cannot modify task: " + ex.getMessage());
        }
    }

    /**
     * Get employee from database by id
     * @param empId Employee number
     * @return Employee object
     */
    public Emp getEmpById(Integer empId) throws TrackerException {
        Emp emp = null;
        try {
            empHome.findByPrimaryKey(empId);
        } catch (Exception ex) {
            throw new TrackerException("Cannot get employee by id: " + ex.getMessage());
        } finally {
            return emp;
        }
    }

    /**
     * Get employee list
     * @return List of employee
     */
    public Collection getAllEmps() throws TrackerException {
        Collection list = null;
        try {
            list = empHome.findAll();
        } catch (Exception ex) {
            throw new TrackerException("Cannot get employee list: " + ex.getMessage());
        } finally {
            return list;
        }
    }
}
