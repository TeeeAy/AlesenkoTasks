public class Resource {

    private boolean isAvailable;
    private final String resourceName;

    public Resource(String resourceName) {
        this.resourceName = resourceName;
    }

    public String toString() {
        return resourceName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}