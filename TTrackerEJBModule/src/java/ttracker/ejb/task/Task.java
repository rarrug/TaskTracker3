package ttracker.ejb.task;

import java.rmi.RemoteException;
import java.util.Date;
import javax.ejb.EJBObject;

/**
 * Task component interface. 
 */
public interface Task extends EJBObject {

    public Integer getId() throws RemoteException;

    public String getName() throws RemoteException;

    public Integer getParentId() throws RemoteException;

    public Date getBegin() throws RemoteException;

    public Date getEnd() throws RemoteException;

    public String getStatus() throws RemoteException;

    public String getDescription() throws RemoteException;

    public String getDeptName() throws RemoteException;

    public String getEmp() throws RemoteException;
}
