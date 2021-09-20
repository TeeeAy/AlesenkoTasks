public class PassengerTransportationTask implements Runnable {

    Passenger passenger;
    Controller controller;

    PassengerTransportationTask(Passenger passenger, Controller controller) {
        this.controller = controller;
        this.passenger = passenger;
        passenger.setTransportationState(TransportationState.IN_PROGRESS);
    }

    public void run() {
        controller.boardPassenger(passenger);
        controller.deboardPassenger(passenger);
        passenger.setTransportationState(TransportationState.COMPLETED);
    }


}

