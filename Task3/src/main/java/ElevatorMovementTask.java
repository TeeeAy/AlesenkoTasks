import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ElevatorMovementTask implements Runnable {

    private final Controller controller;

    public void run() {
        controller.startElevator();
        do {
            controller.setElevatorReady();
            controller.setElevatorReady();
            controller.moveElevator();
        } while (!controller.checkIfCanFinishTransportation());
        controller.shutElevator();
    }


}
