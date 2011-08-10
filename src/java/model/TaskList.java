package model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Save list of tasks
 */
public class TaskList implements Iterable<Task> {

    /* List of tasks */
    private Map<Integer, Task> list;

    public TaskList() {
        list = new HashMap<Integer, Task>();
    }

    public TaskList(Collection<Task> tasks) {
        list = new TreeMap<Integer, Task>();
        for (Task task : tasks) {
            list.put(new Integer(task.getId()), task);
        }
    }

    /**
     * Delete task from list by id
     * @param id 
     */
    public void removeByID(int id) {
        list.remove(new Integer(id));
    }

    /**
     * Remove all tasks from list
     */
    public void removeAll() {
        list.clear();
    }

    /**
     * Check is empty list
     * @return true if list is empty or false another
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**
     * Return tasks count in list
     * @return Tasks count 
     */
    public int size() {
        return list.size();
    }

    /**
     * Iterator for tasks list
     * @return Iterator
     */
    public Iterator<Task> iterator() {
        return list.values().iterator();

    }

    /**
     * Generate list of tasks from map
     * @return List of tasks
     */
    public Collection<Task> asList() {
        return list.values();
    }
}
