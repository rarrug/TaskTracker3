package ttracker.ejb.emp;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.NoSuchEntityException;
import javax.ejb.RemoveException;
import javax.sql.DataSource;
import ttracker.dao.SQLConsts;
import ttracker.ejb.dept.DeptRecord;

/**
 * Employee bean implementation.
 */
public class EmpBean implements EntityBean {

    /* Bean attributes */
    private EntityContext context;
    private Integer id;
    private String name;
    private String job;
    private DeptRecord dept;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getJob() {
        return job;
    }

    public Integer getId() {
        return id;
    }

    public String getDeptName() {
        return dept.getDeptName();
    }

    /**
     * Find all employees
     * @return List of employee keys
     * @throws FinderException 
     */
    public Collection ejbFindAll() throws FinderException {
//        System.out.println("ejbFindAll()");
        Collection list = null;
        try {
            Connection con = getConnection();
            list = new ArrayList();
            PreparedStatement st = null;
            st = con.prepareStatement(SQLConsts.GET_EMP_KEYS);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                list.add(new Integer(rs.getInt("id_emp")));
            }
            st.close();
            st = null;
            releaseConnection(con);
        } catch (SQLException ex) {
            throw new FinderException("No employee was found: " + ex.getMessage());
        }
        return list;
    }

    /**
     * Find employee by id
     * @param id Employee id
     * @return Employee primary key
     * @throws FinderException Cannot find employee
     */
    public Integer ejbFindByPrimaryKey(Integer id) throws FinderException {
//        System.out.println("ejbFindByPrimaryKey()");
        try {
            Connection con = getConnection();
            boolean rowExists = false;
            PreparedStatement st = con.prepareStatement(SQLConsts.EXISTS_EMP);
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

    public void ejbStore() throws EJBException, RemoteException {
//        System.out.println("ejbStore()");
    }

    public void ejbLoad() throws EJBException, RemoteException {
//        System.out.println("ejbLoad()");
        try {
            this.id = (Integer) context.getPrimaryKey();
            Connection con = getConnection();
            PreparedStatement st = con.prepareStatement(SQLConsts.EMP_INFO_BY_ID);
            st.setInt(1, this.id.intValue());
            ResultSet rs = st.executeQuery();
            if (!rs.next()) {
                throw new NoSuchEntityException("In selectRow: Row does not exist");
            }

            this.name = rs.getString("emp_fio");
            this.job = rs.getString("job");
            this.dept = new DeptRecord(rs.getInt("id_dept"), rs.getString("dept_name"));

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

    public void setEntityContext(EntityContext context) throws EJBException,
            RemoteException {
        this.context = context;
    }

    public void unsetEntityContext() throws EJBException, RemoteException {
        context = null;
    }

    public void ejbRemove() throws RemoveException, EJBException, RemoteException {
    }

    /**
     * Create connection with database
     * @return Connection
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
