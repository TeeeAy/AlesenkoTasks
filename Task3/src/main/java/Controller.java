import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class Controller {

    private static final Logger LOGGER = Logger.getLogger(Controller.class.getName());

    Map<Integer, Condition> floorConditions;

    List<Floor> floors;

    Elevator elevator;

    int passengersNumber;

    Lock lock;

    private boolean passengersFlowFlag;

    static {
        setLogger("controller.log", false, LOGGER);
    }

    public static void setLogger(String fileName, boolean useParentHandlers, Logger logger) {
        try {
            ClassLoader classloader = Thread.currentThread().getContextClassLoader();
            String platformIndependentPath = Paths.get(Objects.requireNonNull(classloader.getResource(fileName))
                    .toURI()).toString();
            FileHandler fh = new FileHandler(platformIndependentPath);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.setUseParentHandlers(useParentHandlers);
        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public Controller(List<Floor> floors, Elevator elevator, Map<Integer, Condition> floorConditions,
                      Lock lock, int passengersNumber) {
        this.elevator = elevator;
        this.floors = floors;
        this.floorConditions = floorConditions;
        this.lock = lock;
        this.passengersNumber = passengersNumber;
    }

    public void startElevator() {
        LOGGER.info("STARTING_TRANSPORTATION");
    }

    public void shutElevator() {
        LOGGER.info("COMPLETION_TRANSPORTATION");
    }


    public void moveElevator() {
        lock.lock();
        int currentFloor = elevator.getCurrentFloor();
        int nextFloor;
        switch (elevator.getMovementDirection()) {
            case UP -> nextFloor = currentFloor + 1;
            case DOWN -> nextFloor = currentFloor - 1;
            default -> throw new IllegalArgumentException();
        }
        elevator.setCurrentFloor(nextFloor);
        LOGGER.info("MOVING_ELEVATOR from floor " + currentFloor + " to floor " + nextFloor);
        lock.unlock();
    }

    public void setElevatorReady() {
        lock.lock();
        int currentFloor = elevator.getCurrentFloor();
        passengersFlowFlag = !passengersFlowFlag;
        floorConditions.get(currentFloor).signalAll();
        lock.unlock();
    }


    public void boardPassenger(Passenger passenger) {
        lock.lock();
        while (!(elevator.getCurrentFloor() == passenger.getSourceFloor()
                && elevator.hasAvailablePlaces() && !passengersFlowFlag)) {
            await();
        }
        Floor floor = floors.get(elevator.getCurrentFloor());
        LOGGER.info("BOARDING_OF_PASSENGER (passengerID-" + passenger.getId()
                + " on floor-" + floor.getFloorNumber() + ") ");
        floor.removeFromDispatchContainer(passenger);
        elevator.addPassenger(passenger);
        lock.unlock();
    }

    public void deboardPassenger(Passenger passenger) {
        lock.lock();
        while (!(elevator.getCurrentFloor() == passenger.getDestinationFloor()
                && elevator.hasPassenger(passenger) && passengersFlowFlag)) {
            await();
        }
        Floor floor = floors.get(elevator.getCurrentFloor());
        LOGGER.info("DEBOARDING_OF_PASSENGER (passengerID-" + passenger.getId()
                + " on floor-" + floor.getFloorNumber() + ") ");
        elevator.removePassenger(passenger);
        floor.addToArrivalContainer(passenger);
        lock.unlock();
    }

    public boolean checkIfCanFinishTransportation() {
        lock.lock();
        boolean isElevatorEmpty = elevator.isEmpty();
        boolean areDispatchContainersEmpty = floors
                .stream()
                .allMatch(Floor::isDispatchContainerEmpty);
        lock.unlock();
        return isElevatorEmpty && areDispatchContainersEmpty;
    }


    public boolean validate(Logger logger) {
        lock.lock();
        boolean isElevatorEmpty = elevator.isEmpty();
        if (isElevatorEmpty) {
            logger.info("Elevator is empty - \u2713");
        } else {
            logger.info("Elevator is empty - \u274C");
        }
        boolean areDispatchContainersEmpty = validateDispatcherContainers();
        if (areDispatchContainersEmpty) {
            logger.info("All dispatchContainers are empty - \u2713");
        } else {
            logger.info("All dispatchContainers are empty - \u274C");
        }
        boolean allTransportationsAreCompleted = validateTransportationStates();
        if (allTransportationsAreCompleted) {
            logger.info("All passengers' transportation states are COMPLETED - \u2713");
        } else {
            logger.info("All passengers' transportation states are COMPLETED - \u274C");
        }
        boolean allPassengersArrivedToTheirFloors = validatePassengers();
        if (allPassengersArrivedToTheirFloors) {
            logger.info("All passengers arrived to their floors - \u2713");
        } else {
            logger.info("All passengers arrived to their floors  - \u274C");
        }
        boolean numberOfPassengersIsOk = validateNumberOfPassengers();
        if (numberOfPassengersIsOk) {
            logger.info("The amount of passengers in arrival containers" +
                    " is equal to the initial passengersNumber - \u2713");
        } else {
            logger.info("The amount of passengers in arrival containers" +
                    " is equal to the initial passengersNumber - \u274C");
        }
        lock.unlock();
        return isElevatorEmpty && areDispatchContainersEmpty
                && allTransportationsAreCompleted && allPassengersArrivedToTheirFloors
                && numberOfPassengersIsOk;
    }

    private boolean validateDispatcherContainers() {
        return floors
                .stream()
                .allMatch(Floor::isDispatchContainerEmpty);
    }


    private boolean validateTransportationStates() {
        return floors
                .stream()
                .flatMap(floor -> floor.getArrivalContainer().stream())
                .allMatch(passenger -> passenger.getTransportationState() == TransportationState.COMPLETED);
    }

    private boolean validatePassengers() {
        return floors
                .stream()
                .allMatch(floor -> floor
                        .getArrivalContainer()
                        .stream()
                        .allMatch(passenger -> passenger.getDestinationFloor() == floor.getFloorNumber()));
    }

    private boolean validateNumberOfPassengers() {
        return floors
                .stream()
                .mapToLong(floor -> floor.getArrivalContainer().size())
                .sum() == passengersNumber;
    }

    private void await() {
        try {
            floorConditions.get(elevator.getCurrentFloor()).await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


}
