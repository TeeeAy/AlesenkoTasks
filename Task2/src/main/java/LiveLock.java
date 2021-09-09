import java.util.logging.Logger;

public class LiveLock {

    private static final Logger LOGGER = Logger.getLogger(DeadLock.class.getName());

    public static void workWithResourcesIfAvailable(Resource resource1, Resource resource2) {
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            resource1.setAvailable(false);
            LOGGER.info(Thread.currentThread().getName() + " is waiting for " + resource2 +
                    " to become available");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (resource2.isAvailable()) {
                LOGGER.info("Got " + resource2);
                resource1.setAvailable(true);
                break;
            } else {
                LOGGER.info(Thread.currentThread().getName() + " is setting " + resource1 +
                        " available");
                resource1.setAvailable(true);
            }
        }
    }


    public static void main(String[] args) {
        Resource resource1 = new Resource("resource №1");
        Resource resource2 = new Resource("resource №2");
        new Thread(() -> workWithResourcesIfAvailable(resource1, resource2)).start();
        new Thread(() -> workWithResourcesIfAvailable(resource2, resource1)).start();
    }


}
