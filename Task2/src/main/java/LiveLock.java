import java.util.logging.Logger;

public class LiveLock {

    private static final Logger LOGGER = Logger.getLogger(LiveLock.class.getName());

    public static void workWithResourcesIfAvailable(Resource resource1, Resource resource2) {
        while (resource1.isAvailable()) {
            synchronized (resource1) {
                resource1.setAvailable(false);
                LOGGER.info(Thread.currentThread().getName() + " is checking whether "
                        + resource2 + " is available");
                DeadLock.sleep(100);
                if (resource2.isAvailable()) {
                    synchronized (resource2) {
                        LOGGER.info(Thread.currentThread().getName() + " HAS GOT " + resource2);
                        resource2.notify();
                        break;
                    }
                } else {
                    LOGGER.info(Thread.currentThread().getName() + " is setting " + resource1 +
                            " available");
                    resource1.setAvailable(true);
                    wait(resource1, 7);
                }
            }
        }
        resource1.setAvailable(true);
    }


    public static void main(String[] args) {
        Resource resource1 = new Resource("resource №1");
        Resource resource2 = new Resource("resource №2");
        resource1.setAvailable(true);
        resource2.setAvailable(true);
        new Thread(() -> workWithResourcesIfAvailable(resource1, resource2)).start();
        new Thread(() -> workWithResourcesIfAvailable(resource2, resource1)).start();
    }

    public static void wait(Object obj, int millis) {
        try {
            obj.wait(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}