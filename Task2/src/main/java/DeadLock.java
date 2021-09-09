import java.util.logging.Logger;

public class DeadLock {

    private static final Logger LOGGER = Logger.getLogger(DeadLock.class.getName());
    private final Resource resource1 = new Resource("resource №1");
    private final Resource resource2 = new Resource("resource №2");


    public void workWithResources(){
        synchronized (resource1){
            LOGGER.info("Thread '"+ Thread.currentThread().getName()+"' got "+resource1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("Thread '"+ Thread.currentThread().getName()+"' is waiting for "+resource2);
            synchronized (resource2) {
                LOGGER.info("Thread '"+ Thread.currentThread().getName()+"' has got all" +
                        " necessary resources and finished work");
            }
        }
    }

    public void workWithResourcesInReverseOrder(){
        synchronized (resource2){
            LOGGER.info("Thread '"+ Thread.currentThread().getName()+"' got "+resource2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info("Thread '"+ Thread.currentThread().getName()+"' is waiting for "+resource1);
            synchronized (resource1) {
                LOGGER.info("Thread '"+ Thread.currentThread().getName()+"' has got all" +
                        " necessary resources and finished work");
            }
        }
    }


    public static void main(String[] args){
        DeadLock deadLock = new DeadLock();
        new Thread(deadLock::workWithResources).start();
        new Thread(deadLock::workWithResourcesInReverseOrder).start();
    }
}