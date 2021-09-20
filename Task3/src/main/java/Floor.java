import java.util.ArrayList;
import java.util.List;

public class Floor {

    private final List<Passenger> dispatchContainer = new ArrayList<>();
    private final List<Passenger> arrivalContainer = new ArrayList<>();
    private final int floorNumber;


    Floor(int floor) {
        this.floorNumber = floor;
    }


    public int getFloorNumber() {
        return floorNumber;
    }

    public void addToDispatchContainer(Passenger passenger) {
        dispatchContainer.add(passenger);
    }

    public void addToArrivalContainer(Passenger passenger) {
        arrivalContainer.add(passenger);
    }

    public void removeFromDispatchContainer(Passenger passenger) {
        dispatchContainer.remove(passenger);
    }

    public boolean isDispatchContainerEmpty() {
        return dispatchContainer.isEmpty();
    }

    public List<Passenger> getArrivalContainer() {
        return arrivalContainer;
    }


}