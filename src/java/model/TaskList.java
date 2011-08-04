package model;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Save list of tasks
 */
public class TaskList {

    /* List of tasks */
    private ArrayList<Task> taskList;

    public TaskList() {
        taskList = new ArrayList<Task>();
    }

    public TaskList(Collection<Task> c) {
        taskList = new ArrayList<Task>(c);
    }

    /**
     * Delete task from list by id
     * @param id 
     */
    public void removeByID(int id) {
        for(int index=0; index<taskList.size(); index++) {
            Task task = taskList.get(index);
            if (task.getId() == id) {
                taskList.remove(index);
                break;
            }
        }
    }

    /**
     * Remove all tasks
     */
    public void removeAll() {
        taskList.clear();
    }

    /**
     * Get task list
     * @return  Task list as List interfase
     */
    public List<Task> getTaskList() {
        return taskList;
    }

}
