import java.util.logging.Logger;

public class ElevatorMovementTask implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(ElevatorMovementTask.class.getName());
    private static final int TIMEOUT = 500;

    static {
        Controller.setLogger("validation.log", true, LOGGER);
    }

    private final Elevator elevator;
    private final Controller controller;


    ElevatorMovementTask(Elevator elevator, Controller controller) {
        this.controller = controller;
        this.elevator = elevator;
    }


    public void run() {
        controller.startElevator();
        do {
            controller.setElevatorReady();
            if (elevator.getCurrentFloor() == elevator.getTopFloor()) {
                elevator.setMovementDirection(MovementDirection.DOWN);
            } else if (elevator.getCurrentFloor() == 0) {
                elevator.setMovementDirection(MovementDirection.UP);
            }
            controller.moveElevator();
            sleep();
            controller.setElevatorReady();
            sleep();
        } while (!controller.checkIfCanFinishTransportation());
        controller.shutElevator();
        if (controller.validate(LOGGER)) {
            LOGGER.info("VALIDATION SUCCESSFUL");
        } else {
            LOGGER.info("VALIDATION FAILED. SOMETHING WENT WRONG!!!");
        }
    }


    private static void sleep() {
        try {
            Thread.sleep(TIMEOUT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
