package controller;

import javax.naming.NamingException;
import model.Task;
import java.io.IOException;
import java.sql.*;
import java.util.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import org.jdom.*;
import org.jdom.input.SAXBuilder;

/**
 * Contains methods for operations with database
 */
public class Operations {

    /* Connection data source */
    public static final String SETTINGS_FILE = "C:/dataSource.xml";

    /* Constants for SQL queries */
    private static final String GET_ALL_TASKS = "SELECT Task.id_task, Task.task_name, "
            + "Task.parent_task, Task.date_begin, Task.date_end, Task.status, "
            + "Employee.emp_fio, Department.dept_name FROM Task, Employee, Department "
            + "WHERE Task.employee = Employee.id_emp AND Employee.dept = Department.id_dept "
            + "ORDER BY ";
    private static final String GET_ALL_USERS = "SELECT emp_fio FROM Employee";
    private static final String ADD_TASK = "INSERT INTO Task VALUES(task_seq.nextval, "
            + "?, (SELECT id_task FROM Task WHERE task_name = ?), "
            + "(SELECT id_emp FROM Employee WHERE emp_fio = ?), TO_DATE(?, 'YYYY-MM-DD'), "
            + "TO_DATE(?, 'YYYY-MM-DD'), ?)";
    private static final String DELETE_TASK = "DELETE FROM Task WHERE id_task = ?";
    private static final String MODIFY_TASK = "UPDATE Task SET task_name = ?, "
            + "parent_task = (SELECT id_task FROM Task WHERE task_name = ?), "
            + "employee = (SELECT id_emp FROM Employee WHERE emp_fio = ?), "
            + "date_begin = TO_DATE(?, 'YYYY-MM-DD'), date_end = "
            + "TO_DATE(?, 'YYYY-MM-DD'), status = ? WHERE id_task = ?";
    private static final String GET_HIERARCHY = "SELECT id_task, task_name, parent_task "
            + "FROM Task START WITH parent_task IS null CONNECT BY PRIOR id_task = parent_task "
            + "ORDER SIBLINGS BY id_task";

    /* Current connection to database */
    private static Connection connection;

    /**
     * Connect to database
     * @throws NamingException Wrong JNDI name
     * @throws SQLException SQL error
     */
    public static void connect2() throws NamingException, SQLException {

        /* Create environment */
        Hashtable ht = new Hashtable();
        ht.put(Context.INITIAL_CONTEXT_FACTORY,
                "weblogic.jndi.WLInitialContextFactory");
        ht.put(Context.PROVIDER_URL,
                "t3://localhost:7001");
        
        /* Create context */
        Context ctx = new InitialContext(ht);
        DataSource ds = (DataSource) ctx.lookup("TaskTrackerDataSource");
        
        /* Create connection */
        connection = ds.getConnection();
        if (connection == null) {
            throw new SQLException(
                    "Connection has not created");
        }
    }

    /**
     * Connect to database
     * @throws ClassNotFoundException Cannot find driver class
     * @throws SQLException SQL error
     * @throws JDOMException Parsing XML error
     * @throws IOException I/O error
     */
    public static void connect(String settingsXML) throws ClassNotFoundException, SQLException, JDOMException, IOException {

        SAXBuilder builder = new SAXBuilder(true);
        Document document = builder.build(settingsXML);
        Element root = document.getRootElement();
        Element datacourse = root.getChild("datasource");

        String connectionUrl = datacourse.getChildText("connection-url");
        String driverClass = datacourse.getChildText("driver-class");
        String username = datacourse.getChildText("user-name");
        String password = datacourse.getChildText("password");

        Class.forName(driverClass);

        Locale.setDefault(Locale.ENGLISH);
        connection = DriverManager.getConnection(connectionUrl, username, password);
    }

    /**
     * Disconnect from database
     * @throws SQLException SQL error
     */
    public static void disconnect() throws SQLException {
        connection.close();
    }

    /**
     * Get task list of all tasks
     * @param column Column name. Used for sorting tasks
     * @param type Sort type. Used for sorting tasks
     * @return Task list
     * @throws SQLException SQL error
     */
    public static Collection getAllTasks(String column, String type) throws SQLException {
        Collection tasks = new ArrayList();
        PreparedStatement statement = connection.prepareStatement(GET_ALL_TASKS + column + " " + type);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            tasks.add(new Task(
                    result.getInt("ID_TASK"),
                    result.getString("TASK_NAME"),
                    result.getInt("PARENT_TASK"),
                    result.getDate("DATE_BEGIN"),
                    result.getDate("DATE_END"),
                    result.getString("STATUS"),
                    result.getString("EMP_FIO"),
                    result.getString("DEPT_NAME")));
        }
        return tasks;
    }
    
    public static Collection<String> getAllUsers()  throws SQLException {
        Collection<String> users = new TreeSet<String>();
        PreparedStatement statement = connection.prepareStatement(GET_ALL_USERS);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            users.add(result.getString("EMP_FIO"));
        }
        return users;
    }

    /**
     * Get hierarchical task list
     * @return Hierarchical task list
     * @throws SQLException  SQL error
     */
    public static Collection getHierarchy() throws SQLException {
        Collection tasks = new ArrayList();
        PreparedStatement statement = connection.prepareStatement(GET_HIERARCHY);
        ResultSet result = statement.executeQuery();

        while (result.next()) {
            Task task = new Task(
                    result.getInt("ID_TASK"),
                    result.getString("TASK_NAME"),
                    result.getInt("PARENT_TASK"), 
                    null, null, null, null, null);
            // TODO add all params to hierarchical method
            tasks.add(task);
        }
        return tasks;
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
    public static void addNewTask(String name, String parent, String user,
            String begin, String end, String status) throws SQLException {

        PreparedStatement statement = connection.prepareStatement(ADD_TASK);
        statement.setString(1, name);
        if ("no".equals(parent)) {
            parent = "null";
        }
        statement.setString(2, parent);
        statement.setString(3, user);
        statement.setString(4, begin);
        statement.setString(5, end);
        statement.setString(6, status);
        statement.executeUpdate();
        statement.close();
    }

    /**
     * Delete task
     * @param id Task id
     * @throws SQLException SQL error
     */
    public static void deleteTask(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(DELETE_TASK);
        statement.setInt(1, id);
        statement.executeUpdate();
        statement.close();
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
    public static void modifyTask(int id, String name, String parent, String user,
            String begin, String end, String status) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(MODIFY_TASK);
        statement.setString(1, name);
        if ("no".equals(parent)) {
            parent = "null";
        }        
        statement.setString(2, parent);
        statement.setString(3, user);
        statement.setString(4, begin);
        statement.setString(5, end);
        statement.setString(6, status);
        statement.setInt(7, id);
        statement.executeUpdate();
        statement.close();
    }
}
