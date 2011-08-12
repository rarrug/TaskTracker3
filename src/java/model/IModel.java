package model;

import java.sql.SQLException;
import java.util.Collection;
import javax.naming.NamingException;

public interface IModel {

    Collection<Task> getTaskList(String column, String type) throws SQLException, NamingException;

    Collection<Task> getTaskList(boolean hierarchy) throws SQLException, NamingException;

    Collection<Task> getTaskById(int id) throws SQLException, NamingException;

    Collection<Task> getTaskByName(String taskName) throws SQLException, NamingException;

    Collection<Task> getTaskByUser(String taskName) throws SQLException, NamingException;

    Collection<String> getUserList() throws SQLException, NamingException;

    void addNewTask(String name, String parent, String user, String begin,
            String end, String status, String descr) throws SQLException, NamingException;

    void deleteTask(int id) throws SQLException, NamingException;

    void modifyTask(int id, String name, String parent, String user, String begin,
            String end, String status, String descr) throws SQLException, NamingException;

}
