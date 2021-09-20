public class Passenger {

    private final int id;
    private final int sourceFloor;
    private final int destinationFloor;
    private TransportationState transportationState = TransportationState.NOT_STARTED;

    public Passenger(int id, int sourceFloor, int destinationFloor) {
        this.id = id;
        this.sourceFloor = sourceFloor;
        this.destinationFloor = destinationFloor;
    }

    public int getId() {
        return id;
    }

    public int getSourceFloor() {
        return sourceFloor;
    }

    public int getDestinationFloor() {
        return destinationFloor;
    }

    public TransportationState getTransportationState() {
        return transportationState;
    }

    public void setTransportationState(TransportationState transportationState) {
        this.transportationState = transportationState;
    }
}
