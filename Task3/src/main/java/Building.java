import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class Building {

    private List<Floor> floors;

    Controller controller;

    private Elevator elevator;

    List<Passenger> passengersList;

    public Building() {
        initialize();
    }


    private void initialize() {
        Properties prop = readPropertiesFile(
                Objects.requireNonNull(Building.class.getResource("config.properties")).getPath());
        int floorsNumber = Integer.parseInt(prop.getProperty("floorsNumber"));
        int elevatorCapacity = Integer.parseInt(prop.getProperty("elevatorCapacity"));
        int passengersNumber = Integer.parseInt(prop.getProperty("passengersNumber"));
        initializePassengers(passengersNumber, floorsNumber);
        initializeFloors(floorsNumber);
        elevator = new Elevator(floorsNumber - 1, elevatorCapacity);
        initializeController(passengersNumber, floorsNumber);
    }

    private void initializeController(int passengersNumber, int floorsNumber) {
        Lock lock = new ReentrantLock();
        Map<Integer, Condition> mapOfConditions =
                Stream.iterate(0, n -> n + 1)
                        .limit(floorsNumber)
                        .collect(Collectors.toMap(i -> i, (i) -> lock.newCondition()));
        controller = new Controller(floors, elevator, mapOfConditions, lock, passengersNumber);
    }

    private void initializePassengers(int passengersNumber, int floorsNumber) {
        Random random = new Random();
        passengersList = Stream.iterate(0, n -> n + 1)
                .limit(passengersNumber)
                .map(id -> randomizePassenger(id, floorsNumber, random))
                .collect(Collectors.toList());
    }

    private void initializeFloors(int floorsNumber) {
        Map<Integer, Floor> mapOfFloors =
                Stream.iterate(0, n -> n + 1)
                        .limit(floorsNumber)
                        .map(Floor::new)
                        .collect(Collectors.toMap(Floor::getFloorNumber, Function.identity()));
        passengersList
                .forEach(passenger -> mapOfFloors.get(passenger.getSourceFloor()).addToDispatchContainer(passenger));
        floors = new ArrayList<>(mapOfFloors.values());
    }

    private static Passenger randomizePassenger(int id, int floorsNumber, Random random) {
        int sourceFloor = randomize(floorsNumber, random);
        int destinationFloor = randomize(floorsNumber, random, sourceFloor);
        return new Passenger(id, sourceFloor, destinationFloor);
    }


    private static int randomize(int limit, Random random) {
        return random.nextInt(limit);

    }

    private static int randomize(int limit, Random random, int unavailable) {
        int result = unavailable;
        while (result == unavailable) {
            result = randomize(limit, random);
        }
        return result;
    }


    public static void main(String[] args) {
        Building building = new Building();
        ExecutorService tasks = Executors.newFixedThreadPool(building.passengersList.size() + 1);
        tasks.execute(new ElevatorMovementTask(building.elevator, building.controller));
        building.passengersList.
                forEach((passenger -> tasks.execute(new PassengerTransportationTask(passenger, building.controller))));
        tasks.shutdown();
    }

    private static Properties readPropertiesFile(String fileName) {
        Properties prop = new Properties();
        try (FileInputStream fis = new FileInputStream(fileName)) {
            prop.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop;
    }

}
