public class PassengersValidator implements Validator {

    @Override
    public boolean validate(ValidationInformation validationInfo) {
        boolean allPassengersArrivedToTheirFloors = validationInfo
                .getFloors()
                .stream()
                .allMatch(floor -> floor
                        .getArrivalContainer()
                        .stream()
                        .allMatch(passenger -> passenger.getDestinationFloor() == floor.getFloorNumber()));
        if (allPassengersArrivedToTheirFloors) {
            Validators.LOGGER.info("All passengers arrived to their floors - \u2713");
        } else {
            Validators.LOGGER.info("All passengers arrived to their floors  - \u274C");
        }
        return allPassengersArrivedToTheirFloors;
    }
}
