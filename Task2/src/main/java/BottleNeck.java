import java.util.logging.Logger;
import java.util.stream.IntStream;

public class BottleNeck {

    private static final Logger LOGGER = Logger.getLogger(DeadLock.class.getName());

    public static void main(String[] args) {
        IntStream.range(0, 10).forEach((i) -> new Thread(() -> doTask(i)).start());
    }

    public static synchronized void doTask(int taskNumber) {
        LOGGER.info(Thread.currentThread().getName() + " is doing a task № " + taskNumber);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}