import lombok.Getter;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class Building {

    private final PropertyUtil propertyUtil = new PropertyUtil("config.properties");

    @Getter
    private final int passengersNumber = propertyUtil.getIntegerValueByPropertyName("passengersNumber");
    @Getter
    private final Elevator elevator = initializeElevator(
            propertyUtil.getIntegerValueByPropertyName("floorsNumber"),
            propertyUtil.getIntegerValueByPropertyName("elevatorCapacity"));
    @Getter
    private final List<Passenger> passengers = initializePassengers(
            propertyUtil.getIntegerValueByPropertyName("floorsNumber"));
    @Getter
    private final List<Floor> floors = initializeFloors(
            propertyUtil.getIntegerValueByPropertyName("floorsNumber"));
    @Getter
    private final Controller controller = initializeController(
            propertyUtil.getIntegerValueByPropertyName("floorsNumber"));
    @Getter
    private final Validators validators = initializeValidators();


    private Elevator initializeElevator(int floorsNumber, int elevatorCapacity) {
        return new Elevator(floorsNumber - 1, elevatorCapacity);
    }

    private Validators initializeValidators() {
        List<Validator> validatorList = List.of(
                new ElevatorValidator(),
                new DispatchContainersValidator(),
                new TransportationStatesValidator(),
                new PassengersValidator(),
                new PassengersNumberValidator()
        );
        return new Validators(validatorList);
    }

    private Controller initializeController(int floorsNumber) {
        Lock lock = new ReentrantLock();
        Map<Integer, Condition> mapOfConditions =
                Stream.iterate(0, n -> n + 1)
                        .limit(floorsNumber)
                        .collect(Collectors.toMap(i -> i, (i) -> lock.newCondition()));
        return new Controller(floors, elevator, mapOfConditions, lock);
    }

    private List<Passenger> initializePassengers(int floorsNumber) {
        PassengersRandomUtil passengersRandomUtil = new PassengersRandomUtil(floorsNumber);
        return Stream.iterate(0, n -> n + 1)
                .limit(passengersNumber)
                .map(passengersRandomUtil::randomizePassenger)
                .collect(Collectors.toList());
    }

    private List<Floor> initializeFloors(int floorsNumber) {
        Map<Integer, Floor> mapOfFloors =
                Stream.iterate(0, n -> n + 1)
                        .limit(floorsNumber)
                        .map(Floor::new)
                        .collect(Collectors.toMap(Floor::getFloorNumber, Function.identity()));
        passengers
                .forEach(passenger -> mapOfFloors.get(passenger.getSourceFloor()).addToDispatchContainer(passenger));
        return new ArrayList<>(mapOfFloors.values());
    }

}
