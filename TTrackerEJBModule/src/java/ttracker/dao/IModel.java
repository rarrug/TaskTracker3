package ttracker.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import ttracker.ejb.emp.EmpRecord;
import ttracker.ejb.task.TaskRecord;

public interface IModel {

    /* Task methods */
    boolean isTaskExists(Integer taskId, Connection con) throws SQLException;

    TaskRecord selectTaskById(Integer taskId, Connection con) throws SQLException;

    Collection selectTaskByParam(String param, Connection con, String sql) throws SQLException;

    Collection getAllTaskKeys(Connection con, boolean hier) throws SQLException;

    Integer insertTask(TaskRecord newTask, Connection con) throws SQLException;

    void deleteTask(Integer taskId, Connection con) throws SQLException;

    void updateTask(TaskRecord task, Connection con) throws SQLException;

    /* Employee methods */
    boolean isEmpExists(Integer empId, Connection con) throws SQLException;

    EmpRecord selectEmp(Integer empId, Connection con) throws SQLException;

    Collection getAllEmpKeys(Connection con) throws SQLException;
}
