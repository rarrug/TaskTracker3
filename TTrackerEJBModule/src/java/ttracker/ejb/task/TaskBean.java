package ttracker.ejb.task;

import ttracker.dao.SQLConsts;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.sql.DataSource;
import ttracker.dao.DAOFactory;
import ttracker.ejb.emp.EmpRecord;

/**
 * Task bean implementation.
 */
public class TaskBean implements EntityBean {

    /* Bean attributes */
    private EntityContext context;
    private Integer id;
    private String name;
    private Integer parentId;
    private Date begin;
    private Date end;
    private String status;
    private String description;
    private EmpRecord employee;

    public Integer getId() {
        return id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDeptName() {
        return employee.getDept().getDeptName();
    }

    public String getEmp() {
        return employee.getEmpName();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * Find all tasks
     * @param hier true if need hierarchical list and false another
     * @return List of task keys
     * @throws FinderException Cannot find tasks
     */
    public Collection ejbFindAll(boolean hier) throws FinderException {
//        System.out.println("ejbFindAll()");
        Collection list = null;
        try {
            Connection con = getConnection();
            list = DAOFactory.getInstance().getAllTaskKeys(con, hier);
            releaseConnection(con);
        } catch (SQLException ex) {
            throw new FinderException("No task was found: " + ex.getMessage());
        }
        return list;
    }

    /**
     * Find task by id
     * @param id Task primary key
     * @return Task primary key
     * @throws FinderException Cannot find task
     */
    public Integer ejbFindByPrimaryKey(Integer id) throws FinderException {
//        System.out.println("ejbFindByPrimaryKey()");
        try {
            Connection con = getConnection();
            boolean rowExists = DAOFactory.getInstance().isTaskExists(id, con);
            releaseConnection(con);
            if (!rowExists) {
                throw new FinderException("No task was found. Id = " + id);
            }
        } catch (SQLException e) {
            throw new FinderException("SQL error while getting task: " + e);
        }
        this.id = id;
        return id;
    }

    /**
     * Find tasks by some string parameter
     * @param param Find parameter
     * @param sql SQL query
     * @return List of tasks keys
     * @throws FinderException Cannot find tasks
     */
    private Collection findByParametr(String param, String sql) throws FinderException {
        Collection list = null;
        try {
            Connection con = getConnection();
            list = DAOFactory.getInstance().selectTaskByParam(param, con, sql);
            releaseConnection(con);
        } catch (SQLException ex) {
            throw new FinderException("No task was found: " + ex.getMessage());
        }
        return list;
    }

    /**
     * Find task by name
     * @param name Task name
     * @return Task primary key
     * @throws FinderException Cannot find tasks
     */
    public Collection ejbFindByName(String name) throws FinderException {
//        System.out.println("ejbFindByName()");
        return findByParametr(name, SQLConsts.SELECT_TASK_BY_NAME);
    }

    /**
     * Find task by employee name
     * @param emp Employee name
     * @return Task primary key
     * @throws FinderException Cannot find tasks
     */
    public Collection ejbFindByEmp(String emp) throws FinderException {
//        System.out.println("ejbFindByName()");
        return findByParametr(emp, SQLConsts.SELECT_TASK_BY_EMP);
    }
    
    /**
     * Create new task
     * @param task New task object
     * @return Primary key of new task
     * @throws CreateException
     * @throws RemoteException 
     */
    public Integer ejbCreate(TaskRecord task) throws CreateException, RemoteException {
        Connection con = null;
        Integer taskId = null;
        try {
            try {
                con = getConnection();
                taskId = DAOFactory.getInstance().insertTask(task, con);
            } finally {
                releaseConnection(con);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new CreateException(ex.getMessage());
        }
        return taskId;
    }

    public void ejbPostCreate(TaskRecord task)
            throws CreateException {
    }

    /**
     * Modify task
     * @param task New task object
     * @throws RemoteException 
     */
    public void ejbHomeModify(TaskRecord task) throws RemoteException {
        //System.out.println("ejbHomeModify()");
        try {
            Connection con = getConnection();
            DAOFactory.getInstance().updateTask(task, con);
            releaseConnection(con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void ejbStore() throws EJBException, RemoteException {
//        System.out.println("ejbStore()");
    }

    public void ejbLoad() throws EJBException, RemoteException {
//        System.out.println("ejbLoad()");
        try {
            this.id = (Integer) context.getPrimaryKey();
            Connection con = getConnection();
            TaskRecord record = DAOFactory.getInstance().selectTaskById(id, con);

            this.name = record.getName();
            this.parentId = record.getParentId();
            this.begin = record.getBegin();
            this.end = record.getEnd();
            this.status = record.getStatus();
            this.employee = record.getEmp();
            this.description = record.getDescription();

            releaseConnection(con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void ejbActivate() throws EJBException, RemoteException {
//        System.out.println("ejbActivate()");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
//        System.out.println("ejbPassivate()");
    }

    /**
     * Remove task from database
     * @throws RemoveException
     * @throws EJBException
     * @throws RemoteException 
     */
    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
//        System.out.println("ejbRemove()");
        try {
            Connection con = getConnection();
            DAOFactory.getInstance().deleteTask(id, con);
            releaseConnection(con);
        } catch (SQLException ex) {
            throw new RemoveException(ex.getMessage());
        }
    }

    public void setEntityContext(EntityContext context) throws EJBException,
            RemoteException {
        this.context = context;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
        context = null;
    }

    /**
     * Create connection with database
     * @return Created connection
     */
    private Connection getConnection() {
        Connection conn = null;
        try {
            DataSource ds = (DataSource) context.lookup(SQLConsts.DB_NAME);
            conn = ds.getConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return conn;
    }

    /**
     * Free resources.
     */
    private void releaseConnection(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }

    
}
