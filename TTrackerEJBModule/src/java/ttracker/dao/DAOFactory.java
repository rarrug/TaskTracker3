package ttracker.dao;

/**
 * Create model of some type 
 */
public class DAOFactory {

    /* database factory type */
    private static final int DB_MODEL = 1; 
    /* current model */
    private static IModel currentModel;

    /**
     * Get instanse of model
     * @return  Instanse of model
     */
    public static IModel getInstance() {
        if (currentModel == null) {
            currentModel = getModel(DB_MODEL);
        }
        return currentModel;
    }

    /**
     * Choose and create type of model 
     * @param modelType Type of model
     * @return Model object
     */
    private static IModel getModel(int modelType) {
        IModel model = null;
        switch (modelType) {
            case DB_MODEL:
                model = new DBModel();
                break;
        }
        return model;
    }
}
