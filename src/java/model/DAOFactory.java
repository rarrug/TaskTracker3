package model;

public class DAOFactory {

    private static final int DB_FACTORY = 1; /* database factory type */

    private static IModel currentModel;
    private static int currentFactory = 1; /* current factory type */

    public static IModel getInstance() {
        if (currentModel == null) {
            currentModel = getModel(currentFactory);
        }
        return currentModel;
    }
    
    public static int getCurrentFactory() {
        return currentFactory;
    }

    public static void setCurrentFactory(int currentFactory) {
        DAOFactory.currentFactory = currentFactory;
    }

    private static IModel getModel(int factoryType) {
        IModel model = null;
        switch (factoryType) {
            case 1:
                model = new DBModel();
                break;
        }
        return model;
    }
}
