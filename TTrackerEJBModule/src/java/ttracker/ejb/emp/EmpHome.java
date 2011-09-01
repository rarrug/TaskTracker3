package ttracker.ejb.emp;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Employee home interface.
 */
public interface EmpHome extends EJBHome {

    /* Find single employee by unique id.  */
    public Emp findByPrimaryKey(Integer id) throws FinderException, RemoteException;
    
    /* Find all employees */
    public Collection findAll() throws FinderException, RemoteException;

}
