import lombok.Builder;
import lombok.Getter;

import java.util.List;


@Builder(setterPrefix = "with", toBuilder = true)
public class ValidationInformation {

    @Getter
    private final List<Floor> floors;

    @Getter
    private final Elevator elevator;

    @Getter
    private final int passengersNumber;


}
