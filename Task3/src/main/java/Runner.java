import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Runner {

    public static void main(String[] args) {
        Building building = new Building();
        Elevator elevator = building.getElevator();
        List<Passenger> passengers = building.getPassengers();
        Controller controller = building.getController();
        List<Floor> floors = building.getFloors();
        int passengersNumber = building.getPassengersNumber();
        Validators validators = building.getValidators();
        ValidationInformation validationInformation = ValidationInformation
                .builder()
                .withElevator(elevator)
                .withFloors(floors)
                .withPassengersNumber(passengersNumber).build();
        ExecutorService tasks = Executors.newFixedThreadPool(passengersNumber + 1);
        tasks.execute(new ElevatorMovementTask(controller));
        passengers
                .forEach((passenger -> tasks.execute(new PassengerTransportationTask(passenger, controller))));
        tasks.shutdown();
        awaitTermination(tasks);
        validators.validate(validationInformation);
    }


    private static void awaitTermination(ExecutorService tasks) {
        try {
            tasks.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
