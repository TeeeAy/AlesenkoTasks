import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class PropertyUtil {

    Properties properties;

    PropertyUtil(String propertiesFileName) {
        properties = readPropertiesFile(
                Objects.requireNonNull(propertiesFileName));

    }

    public String getStringValueByPropertyName(String name) {
        return properties.getProperty(name);
    }

    public Integer getIntegerValueByPropertyName(String name) {
        return Integer.valueOf(getStringValueByPropertyName(name));
    }


    private Properties readPropertiesFile(String fileName) {
        try (InputStream inputStream = ResourceUtil.getResourceByName(fileName)) {
            Properties properties = new Properties();
            properties.load(inputStream);
            return properties;
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }


}
