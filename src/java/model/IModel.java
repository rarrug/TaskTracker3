package model;

import java.sql.SQLException;
import java.util.Collection;
import javax.naming.NamingException;
import model.exc.ConnectionException;

public interface IModel {

    Collection<Task> getTaskList(String column, String type) throws SQLException, ConnectionException;

    Collection<Task> getTaskList(boolean hierarchy) throws SQLException, ConnectionException;

    Collection<Task> getTaskById(int id) throws SQLException, ConnectionException;

    Collection<Task> getTaskByName(String taskName) throws SQLException, ConnectionException;

    Collection<Task> getTaskByUser(String taskName) throws SQLException, ConnectionException;

    Collection<String> getUserList() throws SQLException, ConnectionException;

    void addNewTask(String name, String parent, String user, String begin,
            String end, String status, String descr) throws SQLException, ConnectionException;

    void deleteTask(int id) throws SQLException, ConnectionException;

    void modifyTask(int id, String name, String parent, String user, String begin,
            String end, String status, String descr) throws SQLException, ConnectionException;

}
