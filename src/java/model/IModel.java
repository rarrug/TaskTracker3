package model;

import java.sql.SQLException;
import java.util.Collection;

public interface IModel {

    void connect() throws SQLException, ClassNotFoundException;

    Collection<Task> getTaskList(String column, String type) throws SQLException, ClassNotFoundException;

    Collection<Task> getTaskList(boolean hierarchy) throws SQLException, ClassNotFoundException;

    Collection<Task> getTaskById(int id) throws SQLException, ClassNotFoundException;

    Collection<Task> getTaskByName(String taskName) throws SQLException, ClassNotFoundException;

    Collection<Task> getTaskByUser(String taskName) throws SQLException, ClassNotFoundException;

    Collection<String> getUserList() throws SQLException, ClassNotFoundException;

    void addNewTask(String name, String parent, String user, String begin,
            String end, String status, String descr) throws SQLException, ClassNotFoundException;

    void deleteTask(int id) throws SQLException, ClassNotFoundException;

    void modifyTask(int id, String name, String parent, String user, String begin,
            String end, String status, String descr) throws SQLException, ClassNotFoundException;

    void disconnect() throws SQLException, ClassNotFoundException;
}
