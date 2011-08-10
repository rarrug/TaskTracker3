package controller;


import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

public class TaskActionFactory {

    /* List of actions */
    protected Map listActions = getDefaultMap();
    
    /* Logger */
    private static final Logger logger = Logger.getLogger(TaskActionFactory.class);

    /**
     * Create and fill list of actions
     * @return List of actions
     */
    private Map getDefaultMap() {
        Map map = new HashMap();
        map.put("index", ShowTask.class);
        map.put("addtask", AddTask.class);
        map.put("removetask", RemoveTask.class);
        map.put("modifytask", ModifyTask.class);
        map.put("savetask", SaveTask.class);
        return map;
    }

    /**
     * Create action by name
     * @param actionName Action name
     * @return Action object
     */
    public IAction create(String actionName) {
        Class klass = (Class) listActions.get(actionName);
        if (klass == null) {
            throw new RuntimeException(getClass() + " was unable to find an " + "action named '" + actionName + "'.");
        }
        IAction actionInstance = null;
        try {
            actionInstance = (IAction) klass.newInstance();
        } catch (InstantiationException ex) {
            logger.error("Create action exception", ex);
        } catch (IllegalAccessException ex) {
            logger.error("Create action exception", ex);
        }
        return actionInstance;
    }
}
