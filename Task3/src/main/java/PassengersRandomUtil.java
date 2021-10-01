import lombok.RequiredArgsConstructor;

import java.util.Random;

@RequiredArgsConstructor
public class PassengersRandomUtil {

    private final Random random = new Random();

    private final int floorsNumber;


    public Passenger randomizePassenger(int id) {
        int sourceFloor = randomize(floorsNumber);
        int destinationFloor = randomize(floorsNumber, sourceFloor);
        return new Passenger(id, sourceFloor, destinationFloor);
    }


    private int randomize(int limit) {
        return random.nextInt(limit);

    }

    private int randomize(int limit, int unavailable) {
        int result = unavailable;
        while (result == unavailable) {
            result = randomize(limit);
        }
        return result;
    }

}
