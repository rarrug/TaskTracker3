package model;

public class DAOFactory {

    private static final int DB_FACTORY = 1; /* database factory type */

    private static IModel currentModel;

    public static IModel getInstance() {
        if (currentModel == null) {
            currentModel = getModel(DB_FACTORY);
        }
        return currentModel;
    }

    private static IModel getModel(int factoryType) {
        IModel model = null;
        switch (factoryType) {
            case DB_FACTORY:
                model = new DBModel();
                break;
        }
        return model;
    }
}
