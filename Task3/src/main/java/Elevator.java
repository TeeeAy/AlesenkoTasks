import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Elevator {

    @Getter
    private final int topFloor;
    private final int capacity;
    @Getter
    private final List<Passenger> container;
    @Getter
    @Setter
    private int currentFloor = 0;
    @Getter
    @Setter
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

    public void addPassenger(Passenger passenger) {
        container.add(passenger);
    }

    public void removePassenger(Passenger passenger) {
        container.remove(passenger);
    }

    public boolean hasPassenger(Passenger passenger) {
        return container.contains(passenger);
    }


}