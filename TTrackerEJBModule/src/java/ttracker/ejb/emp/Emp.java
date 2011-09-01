package ttracker.ejb.emp;

import java.rmi.RemoteException;
import javax.ejb.EJBObject;

/**
 * Employee component remote interface. 
 */
public interface Emp extends EJBObject {

    public Integer getId() throws RemoteException;

    public String getName() throws RemoteException;

    public String getJob() throws RemoteException;

    public String getDeptName() throws RemoteException;

}
