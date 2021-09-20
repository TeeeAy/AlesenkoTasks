import java.util.ArrayList;
import java.util.List;

public class Elevator {

    private final int topFloor;
    private final int capacity;
    private final List<Passenger> container;
    private int currentFloor = 0;
    private MovementDirection movementDirection = MovementDirection.UP;


    public Elevator(int topFloor, int capacity) {
        this.topFloor = topFloor;
        this.capacity = capacity;
        container = new ArrayList<>(capacity);
    }

    public boolean hasAvailablePlaces() {
        return capacity - container.size() != 0;
    }

    public boolean isEmpty() {
        return container.isEmpty();
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void addPassenger(Passenger passenger) {
        container.add(passenger);
    }

    public void removePassenger(Passenger passenger) {
        container.remove(passenger);
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public void setMovementDirection(MovementDirection movementDirection) {
        this.movementDirection = movementDirection;
    }

    public MovementDirection getMovementDirection() {
        return movementDirection;
    }

    public int getTopFloor() {
        return topFloor;
    }

    public boolean hasPassenger(Passenger passenger) {
        return container.contains(passenger);
    }

}