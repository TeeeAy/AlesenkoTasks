import java.util.logging.Logger;

public class DeadLock {

    private static final Logger LOGGER = Logger.getLogger(DeadLock.class.getName());


    public void workWithResources(final Resource resource1, final Resource resource2) {
        synchronized (resource1) {
            LOGGER.info("Thread '" + Thread.currentThread().getName() + "' got " + resource1);
            sleep(1000);
            LOGGER.info("Thread '" + Thread.currentThread().getName() + "' is waiting for " + resource2);
            synchronized (resource2) {
                LOGGER.info("Thread '" + Thread.currentThread().getName() + "' has got all" +
                        " necessary resources and finished work");
            }
        }
    }


    public static void main(String[] args) {
        final Resource resource1 = new Resource("resource №1");
        final Resource resource2 = new Resource("resource №2");
        DeadLock deadLock = new DeadLock();
        new Thread(() -> deadLock.workWithResources(resource1, resource2)).start();
        new Thread(() -> deadLock.workWithResources(resource2, resource1)).start();
    }

    public static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}