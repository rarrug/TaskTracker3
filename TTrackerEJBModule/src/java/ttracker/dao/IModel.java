package ttracker.dao;

import java.util.Collection;
import ttracker.dao.exc.TrackerException;
import ttracker.ejb.emp.Emp;
import ttracker.ejb.task.Task;
import ttracker.ejb.task.TaskRecord;

public interface IModel {

    /* Task methods */
    Task getTaskById(Integer taskId) throws TrackerException;

    Collection getTaskByName(String name) throws TrackerException;
    
    Collection getTaskByEmp(String emp) throws TrackerException;

    Collection getAllTasks(boolean hier) throws TrackerException;

    void addTask(TaskRecord newTask) throws TrackerException;

    void deleteTask(Integer taskId) throws TrackerException;

    void modifyTask(TaskRecord task) throws TrackerException;

    /* Employee methods */
    Emp getEmpById(Integer empId) throws TrackerException;

    Collection getAllEmps() throws TrackerException;
}
