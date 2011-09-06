package ttracker.ejb.task;

import ttracker.dao.SQLConsts;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.RemoveException;
import javax.sql.DataSource;
import ttracker.ejb.dept.DeptRecord;
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

            list = new ArrayList();
            PreparedStatement st = null;
            if (hier) {
                st = con.prepareStatement(SQLConsts.GET_TASK_HIERARCHY);
            } else {
                st = con.prepareStatement(SQLConsts.GET_TASK_KEYS);
            }
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("id_task")));
            }
            st.close();
            st = null;

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

            boolean rowExists = false;
            PreparedStatement st = con.prepareStatement(SQLConsts.EXISTS_TASK);
            st.setInt(1, id.intValue());
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                rowExists = true;
            }
            st.close();
            st = null;

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
    private Collection findByParameter(String param, String sql) throws FinderException {
        Collection list = null;
        try {
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, param);
            ResultSet rs = st.executeQuery();
            list = new ArrayList();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("id_task")));
            }
            st.close();
            st = null;
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
        return findByParameter(name, SQLConsts.SELECT_TASK_BY_NAME);
    }

    /**
     * Find task by employee name
     * @param emp Employee name
     * @return Task primary key
     * @throws FinderException Cannot find tasks
     */
    public Collection ejbFindByEmp(String emp) throws FinderException {
//        System.out.println("ejbFindByName()");
        return findByParameter(emp, SQLConsts.SELECT_TASK_BY_EMP);
    }

    /**
     * Create new task
     * @param task New task object
     * @return Primary key of new task
     * @throws CreateException
     * @throws RemoteException 
     */
    public Integer ejbCreate(TaskRecord newTask) throws CreateException, RemoteException {
        Connection con = null;
        Integer taskId = null;
        try {
            con = getConnection();
            PreparedStatement statement = con.prepareStatement(SQLConsts.INSERT_TASK);
            statement.setString(1, newTask.getName());
            if (newTask.getParentId().compareTo(Integer.valueOf(0)) == 0) {
                statement.setNull(2, java.sql.Types.INTEGER);
            } else {
                statement.setInt(2, newTask.getParentId());
            }
            statement.setString(3, newTask.getEmp().getEmpName());
            statement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(newTask.getBegin()));
            statement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(newTask.getEnd()));
            statement.setString(6, newTask.getStatus());
            statement.setString(7, newTask.getDescription());
            statement.executeQuery();
            statement = con.prepareStatement(SQLConsts.GET_TASK_ID_BY_NAME);
            statement.setString(1, newTask.getName());
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                taskId = Integer.valueOf(rs.getInt("id_task"));
            }
            statement.close();
            statement = null;
            releaseConnection(con);
        } catch (SQLException ex) {
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
            PreparedStatement statement = con.prepareStatement(SQLConsts.UPDATE_TASK);
            statement.setString(1, task.getName());
            if (task.getParentId() == 0) {
                statement.setNull(2, java.sql.Types.INTEGER);
            } else {
                statement.setInt(2, task.getParentId());
            }
            statement.setString(3, task.getEmp().getEmpName());
            statement.setString(4, new SimpleDateFormat("yyyy-MM-dd").format(task.getBegin()));
            statement.setString(5, new SimpleDateFormat("yyyy-MM-dd").format(task.getEnd()));
            statement.setString(6, task.getStatus());
            statement.setString(7, task.getDescription());
            statement.setInt(8, task.getId());
            statement.executeQuery();
            statement.close();
            statement = null;
            releaseConnection(con);
        } catch (SQLException ex) {
            throw new RemoteException(ex.getMessage());
        }
    }

    public void ejbStore() throws EJBException, RemoteException {
//        System.out.println("ejbStore()");
    }

    public void ejbLoad() throws EJBException, RemoteException {
//        System.out.println("ejbLoad()");
        this.id = (Integer) context.getPrimaryKey();
        try {
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(SQLConsts.SELECT_TASK_BY_ID);
            st.setInt(1, this.id.intValue());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                throw new NoSuchEntityException("In selectRow: Row does not exist");
            }

            this.name = rs.getString("task_name");
            this.parentId = new Integer(rs.getInt("parent_task"));
            this.begin = rs.getDate("date_begin");
            this.end = rs.getDate("date_end");
            this.status = rs.getString("status");
            this.employee = new EmpRecord(rs.getInt("id_emp"), rs.getString("emp_fio"), rs.getString("job"),
                    new DeptRecord(rs.getInt("id_dept"), rs.getString("dept_name")));
            this.description = rs.getString("descr");

            st.close();
            st = null;

            releaseConnection(con);
        } catch (SQLException ex) {
            throw new RemoteException(ex.getMessage());
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
        try {
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(SQLConsts.DELETE_TASK);
            st.setInt(1, this.id.intValue());
            st.executeQuery();
            st.close();
            st = null;
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
     * Free resources
     * @param con Connection for free
     * @throws SQLException 
     */
    private void releaseConnection(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }
}
