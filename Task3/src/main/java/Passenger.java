import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
public class Passenger {

    @Getter
    private final int id;
    @Getter
    private final int sourceFloor;
    @Getter
    private final int destinationFloor;
    @Getter
    @Setter
    private TransportationState transportationState = TransportationState.NOT_STARTED;

}
