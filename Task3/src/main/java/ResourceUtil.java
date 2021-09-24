import java.io.InputStream;

public final class ResourceUtil {

    private ResourceUtil() {
    }

    public static InputStream getResourceByName(String name) {
        return ResourceUtil.class
                .getClassLoader()
                .getResourceAsStream(name);
    }
}
