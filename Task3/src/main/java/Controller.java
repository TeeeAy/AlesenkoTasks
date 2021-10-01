import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class Controller {

    private final Map<Integer, Condition> floorConditions;

    private final List<Floor> floors;

    private final Elevator elevator;

    private final Lock lock;

    private boolean passengersFlowFlag = true;


    private final static Logger LOGGER = LoggerFactory.getLogger("controller");

    public Controller(List<Floor> floors, Elevator elevator, Map<Integer, Condition> floorConditions,
                      Lock lock) {
        this.elevator = elevator;
        this.floors = floors;
        this.floorConditions = floorConditions;
        this.lock = lock;
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
        if (currentFloor == elevator.getTopFloor()) {
            elevator.setMovementDirection(MovementDirection.DOWN);
        } else if (currentFloor == 0) {
            elevator.setMovementDirection(MovementDirection.UP);
        }
        int nextFloor = currentFloor + elevator.getMovementDirection().floorDelta();
        elevator.setCurrentFloor(nextFloor);
        LOGGER.info("MOVING_ELEVATOR from floor " + currentFloor + " to floor " + nextFloor);
        floorConditions.get(currentFloor).signalAll();
        lock.unlock();
    }

    public void setElevatorReady() {
        lock.lock();
        int currentFloor = elevator.getCurrentFloor();
        if (passengersFlowFlag) {
            waitForPassengersToDeboard();
        } else {
            waitForPassengersToBoard();
        }
        passengersFlowFlag = !passengersFlowFlag;
        floorConditions.get(currentFloor).signalAll();
        lock.unlock();
    }


    private void waitForPassengersToDeboard() {
        while (!canDeboard()) {
            await();
        }
    }

    private void waitForPassengersToBoard() {
        while (!canBoard()) {
            await();
        }
    }

    private boolean canBoard() {
        return floors.get(elevator.getCurrentFloor()).isDispatchContainerEmpty()
                || !elevator.hasAvailablePlaces();
    }

    private boolean canDeboard() {
        return elevator.isEmpty()
                || elevator.getContainer().stream()
                .noneMatch(passenger -> passenger.getDestinationFloor() == elevator.getCurrentFloor());
    }


    public void boardPassenger(Passenger passenger) {
        lock.lock();
        while (!(canBoard(passenger))) {
            await();
        }
        Floor floor = floors.get(elevator.getCurrentFloor());
        LOGGER.info("BOARDING_OF_PASSENGER (passengerID-" + passenger.getId()
                + " on floor-" + floor.getFloorNumber() + ") ");
        floor.removeFromDispatchContainer(passenger);
        elevator.addPassenger(passenger);
        floorConditions.get(elevator.getCurrentFloor()).signalAll();
        lock.unlock();
    }

    private boolean canBoard(Passenger passenger) {
        return elevator.getCurrentFloor() == passenger.getSourceFloor()
                && elevator.hasAvailablePlaces() && !passengersFlowFlag;
    }

    public void deboardPassenger(Passenger passenger) {
        lock.lock();
        while (!canDeboard(passenger)) {
            await();
        }
        Floor floor = floors.get(elevator.getCurrentFloor());
        LOGGER.info("DEBOARDING_OF_PASSENGER (passengerID-" + passenger.getId()
                + " on floor-" + floor.getFloorNumber() + ") ");
        elevator.removePassenger(passenger);
        floor.addToArrivalContainer(passenger);
        floorConditions.get(elevator.getCurrentFloor()).signalAll();
        lock.unlock();
    }

    private boolean canDeboard(Passenger passenger) {
        return elevator.getCurrentFloor() == passenger.getDestinationFloor()
                && elevator.hasPassenger(passenger) && passengersFlowFlag;
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


    private void await() {
        try {
            floorConditions.get(elevator.getCurrentFloor()).await();
        } catch (InterruptedException exception) {
            throw new RuntimeException(exception);
        }
    }


}
