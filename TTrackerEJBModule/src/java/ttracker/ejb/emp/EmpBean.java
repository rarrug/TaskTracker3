package ttracker.ejb.emp;

import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.sql.DataSource;
import ttracker.dao.DAOFactory;
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
            list = DAOFactory.getInstance().getAllEmpKeys(con);
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
            boolean rowExists = DAOFactory.getInstance().isEmpExists(id, con);
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
            EmpRecord record = DAOFactory.getInstance().selectEmp(id, con);

            this.name = record.empName;
            this.job = record.job;
            this.dept = record.dept;

            releaseConnection(con);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void ejbActivate() throws EJBException, RemoteException {
//        System.out.println("ejbActivate()");
    }

    public void ejbPassivate() throws EJBException, RemoteException {
        System.out.println("ejbPassivate()");
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
     * Free resources.
     */
    private void releaseConnection(Connection con) throws SQLException {
        if (con != null) {
            con.close();
        }
    }
}
