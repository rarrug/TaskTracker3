package model;

import java.io.IOException;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.jdom.*;

/**
 * Contains methods for operations with database
 */
public class DBModel implements IModel {

    /* Current connection to database */
    private Connection connection;

    /**
     * Connect to database
     * @throws ClassNotFoundException Cannot find driver class
     * @throws SQLException SQL error
     * @throws JDOMException Parsing XML error
     * @throws IOException I/O error
     */
    /*public void connect() throws ClassNotFoundException, SQLException {
    
    String connectionUrl = AppProperties.getProperty("connection-url");
    String driverClass = AppProperties.getProperty("driver-class");
    String username = AppProperties.getProperty("user-name");
    String password = AppProperties.getProperty("password");
    
    Locale.setDefault(Locale.ENGLISH);
    Class.forName(driverClass);
    connection = DriverManager.getConnection(connectionUrl, username, password);
    
    }*/
    private void connect() throws NamingException, SQLException {

        /* Create environment */
        Hashtable ht = new Hashtable();
        ht.put(Context.INITIAL_CONTEXT_FACTORY,
                "weblogic.jndi.WLInitialContextFactory");
        ht.put(Context.PROVIDER_URL,
                "t3://localhost:7001");

        /* Create context */
        Context ctx = new InitialContext(ht);
        DataSource ds = (DataSource) ctx.lookup("multTTrTest1");

        /* Create connection */
        connection = ds.getConnection();
        if (connection == null) {
            throw new SQLException(
                    "Connection has not created");
        }
    }

    /**
     * Disconnect from database
     * @throws SQLException SQL error
     */
    private void disconnect() throws SQLException {
        connection.close();
    }

    /**
     * Get task list of all tasks
     * @param column Column name. Used for sorting tasks
     * @param type Sort type. Used for sorting tasks
     * @return Task list
     * @throws SQLException SQL error
     */
    private Collection<Task> getTaskList(String column, String type, boolean hierarchy)
            throws SQLException, NamingException {
        connect();
        String sql = AppProperties.getProperty("get_hierarchy_sql");
        if (!hierarchy) {
            sql = AppProperties.getProperty("get_all_tasks_sql") + " " + column + " " + type;
        }
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet result = statement.executeQuery();
        Collection<Task> tasks = new ArrayList<Task>();

        Date beginDate = null;
        Date endDate = null;
        String status = null;
        String empFio = null;
        String deptName = null;

        while (result.next()) {
            if (!hierarchy) {
                beginDate = result.getDate("DATE_BEGIN");
                endDate = result.getDate("DATE_END");
                status = result.getString("STATUS");
                empFio = result.getString("EMP_FIO");
                deptName = result.getString("DEPT_NAME");
            }
            Task task = new Task(
                    result.getInt("ID_TASK"),
                    result.getString("TASK_NAME"),
                    result.getInt("PARENT_TASK"),
                    beginDate,
                    endDate,
                    status,
                    empFio,
                    deptName,
                    result.getString("DESCR"));
            tasks.add(task);
        }
        statement.close();
        disconnect();
        return tasks;
    }

    public Collection<Task> getTaskList(String column, String type)
            throws SQLException, NamingException {
        return getTaskList(column, type, false);
    }

    /**
     * Get hierarchical task list
     * @return Hierarchical task list
     * @throws SQLException  SQL error
     */
    public Collection<Task> getTaskList(boolean hierarchy)
            throws SQLException, NamingException {
        if (!hierarchy) {
            return getTaskList("Task.id_task", "ASC", false);
        } else {
            return getTaskList(null, null, true);
        }
    }

    private Collection<Task> getTask(ResultSet result) throws SQLException {
        Collection<Task> tasks = new ArrayList<Task>();
        while (result.next()) {
            Task task = new Task(
                    result.getInt("ID_TASK"),
                    result.getString("TASK_NAME"),
                    result.getInt("PARENT_TASK"),
                    result.getDate("DATE_BEGIN"),
                    result.getDate("DATE_END"),
                    result.getString("STATUS"),
                    result.getString("EMP_FIO"),
                    result.getString("DEPT_NAME"),
                    result.getString("DESCR"));
            tasks.add(task);
        }
        return tasks;
    }

    public Collection<Task> getTaskById(int id) throws SQLException, NamingException {
        connect();
        PreparedStatement statement = connection.prepareStatement(AppProperties.getProperty("get_task_by_id") + " Task.id_task ASC");
        statement.setInt(1, id);
        ResultSet result = statement.executeQuery();
        Collection<Task> tasks = getTask(result);
        statement.close();
        disconnect();
        return tasks;
    }

    public Collection<Task> getTaskByName(String taskName) throws SQLException, NamingException {
        connect();
        PreparedStatement statement = connection.prepareStatement(AppProperties.getProperty("get_task_by_name") + " Task.id_task ASC");
        statement.setString(1, taskName);
        ResultSet result = statement.executeQuery();
        Collection<Task> tasks = getTask(result);
        statement.close();
        disconnect();
        return tasks;
    }

    public Collection<Task> getTaskByUser(String userName) throws SQLException, NamingException {
        connect();
        PreparedStatement statement = connection.prepareStatement(AppProperties.getProperty("get_task_by_user") + " Task.id_task ASC");
        statement.setString(1, userName);
        ResultSet result = statement.executeQuery();
        Collection<Task> tasks = getTask(result);
        statement.close();
        disconnect();
        return tasks;
    }

    public Collection<String> getUserList()
            throws SQLException, NamingException {
        connect();
        Collection<String> users = new TreeSet<String>();
        PreparedStatement statement = connection.prepareStatement(AppProperties.getProperty("get_all_users_sql"));
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            users.add(result.getString("EMP_FIO"));
        }
        statement.close();
        disconnect();
        return users;
    }

    /**
     * Add new task to database
     * @param name Task name
     * @param parent Parent task 
     * @param user User
     * @param begin Begin date
     * @param end End date
     * @param status Task status
     * @throws SQLException SQl error
     */
    public void addNewTask(String name, String parent, String user,
            String begin, String end, String status, String descr)
            throws SQLException, NamingException {

        connect();
        PreparedStatement statement = connection.prepareStatement(AppProperties.getProperty("add_task_sql"));
        statement.setString(1, name);
        if ("no".equals(parent)) {
            parent = "null";
        }
        statement.setString(2, parent);
        statement.setString(3, user);
        statement.setString(4, begin);
        statement.setString(5, end);
        statement.setString(6, status);
        statement.setString(7, descr);
        statement.executeUpdate();
        statement.close();
        disconnect();
    }

    /**
     * Delete task
     * @param id Task id
     * @throws SQLException SQL error
     */
    public void deleteTask(int id) throws SQLException, NamingException {
        connect();
        PreparedStatement statement = connection.prepareStatement(AppProperties.getProperty("delete_task_sql"));
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
        disconnect();
    }

    /**
     * Modify task
     * @param id Task id
     * @param name Task name
     * @param parent Parent task
     * @param user User
     * @param begin Begin date
     * @param end End date
     * @param status Task status
     * @throws SQLException SQL error
     */
    public void modifyTask(int id, String name, String parent, String user,
            String begin, String end, String status, String descr)
            throws SQLException, NamingException {
        connect();
        PreparedStatement statement = connection.prepareStatement(AppProperties.getProperty("modify_task_sql"));
        statement.setString(1, name);
        if ("no".equals(parent)) {
            parent = "null";
        }
        statement.setString(2, parent);
        statement.setString(3, user);
        statement.setString(4, begin);
        statement.setString(5, end);
        statement.setString(6, status);
        statement.setString(7, descr);
        statement.setInt(8, id);
        statement.executeUpdate();
        statement.close();
        disconnect();
    }
}
