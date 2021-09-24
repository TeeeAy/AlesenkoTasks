import lombok.Getter;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class Building {

    @Getter
    private List<Floor> floors;
    @Getter
    private Controller controller;
    @Getter
    private Elevator elevator;
    @Getter
    private List<Passenger> passengers;
    @Getter
    private int passengersNumber;
    @Getter
    private Validators validators;


    public Building() {
        initialize();
    }

    private void initialize() {
        PropertyUtil propertyUtil = new PropertyUtil("config.properties");
        int floorsNumber = propertyUtil.getIntegerValueByPropertyName("floorsNumber");
        int elevatorCapacity = propertyUtil.getIntegerValueByPropertyName("elevatorCapacity");
        passengersNumber = propertyUtil.getIntegerValueByPropertyName("passengersNumber");
        initializePassengers(floorsNumber);
        initializeFloors(floorsNumber);
        elevator = new Elevator(floorsNumber - 1, elevatorCapacity);
        initializeController(floorsNumber);
        initializeValidators();
    }

    private void initializeValidators() {
        List<Validator> validatorList = List.of(
                new ElevatorValidator(),
                new DispatchContainersValidator(),
                new TransportationStatesValidator(),
                new PassengersValidator(),
                new PassengersNumberValidator()
        );
        validators = new Validators(validatorList);
    }

    private void initializeController(int floorsNumber) {
        Lock lock = new ReentrantLock();
        Map<Integer, Condition> mapOfConditions =
                Stream.iterate(0, n -> n + 1)
                        .limit(floorsNumber)
                        .collect(Collectors.toMap(i -> i, (i) -> lock.newCondition()));
        controller = new Controller(floors, elevator, mapOfConditions, lock);
    }

    private void initializePassengers(int floorsNumber) {
        PassengersRandomUtil passengersRandomUtil = new PassengersRandomUtil(floorsNumber);
        passengers = Stream.iterate(0, n -> n + 1)
                .limit(passengersNumber)
                .map(passengersRandomUtil::randomizePassenger)
                .collect(Collectors.toList());
    }

    private void initializeFloors(int floorsNumber) {
        Map<Integer, Floor> mapOfFloors =
                Stream.iterate(0, n -> n + 1)
                        .limit(floorsNumber)
                        .map(Floor::new)
                        .collect(Collectors.toMap(Floor::getFloorNumber, Function.identity()));
        passengers
                .forEach(passenger -> mapOfFloors.get(passenger.getSourceFloor()).addToDispatchContainer(passenger));
        floors = new ArrayList<>(mapOfFloors.values());
    }

}
