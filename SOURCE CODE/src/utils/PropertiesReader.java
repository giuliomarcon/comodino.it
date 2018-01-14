package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesReader {

    private Map<String, String> map = new HashMap<String, String>();
    private String filename;

    public PropertiesReader() {
        this( "config.properties");
    }

    public PropertiesReader(String filename) {
        this.filename = filename;
        readProperties();
        //printAllProperties();
    }

    private void readProperties(){
        Properties prop = new Properties();
        InputStream input = null;

        try {

            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            input = classLoader.getResourceAsStream("config.properties");

            if(input == null){
                System.out.println("Sorry, unable to find " + filename);
                return;
            }

            // load a properties file
            prop.load(input);

            // get the property value and print it out
            map.put("database", prop.getProperty("database"));
            map.put("user", prop.getProperty("user"));
            map.put("password", prop.getProperty("password"));

        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void printAllProperties(){
        for ( Map.Entry<String, String> entry : map.entrySet() ) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
    public String get(Object key){
        return this.map.get(key);
    }

    public static void main(String[] args) {
        PropertiesReader pr = new PropertiesReader();
    }


}
