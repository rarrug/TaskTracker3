package controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Load and save properties 
 */
public class AppProperties {

    private static Properties properties;

    private static Properties getInstance() {
        if (properties == null) {
            try {
                FileInputStream inFile = new FileInputStream("PropSource.properties");
                properties = new Properties();
                properties.load(inFile);
                inFile.close();
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return properties;
    }
    
    /**
     * Get property by name 
     * @param propName Property name
     * @return Property as string
     */
    public static String getProperty(String propName) {
        return getInstance().getProperty(propName);        
    }

}
