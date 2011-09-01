package ttracker.ejb.task;

import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.CreateException;
import javax.ejb.EJBHome;
import javax.ejb.FinderException;

/**
 * Task home interface.
 */
public interface TaskHome extends EJBHome {

    /**
     * Find single task by unique id.
     * @param id
     * @return
     * @throws FinderException
     * @throws RemoteException
     */
    public Task findByPrimaryKey(Integer id) throws FinderException, RemoteException;

    /* Find tasks by name */
    public Collection findByName(String name) throws FinderException, RemoteException;
    
    /* Find tasks by employee */
    public Collection findByEmp(String emp) throws FinderException, RemoteException;

    /* Find all tasks */
    public Collection findAll(boolean hier) throws FinderException, RemoteException;

    /* Create task */
    public Task create(TaskRecord task) throws CreateException, RemoteException;

    /* Modify task */
    public void modify(TaskRecord task) throws RemoteException;
}
