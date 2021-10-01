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
        String value;
        if (allPassengersArrivedToTheirFloors) {
            value = "\u2713";
        } else {
            value = "\u274C";
        }
        Validators.LOGGER.info("All passengers arrived to their floors - " + value);
        return allPassengersArrivedToTheirFloors;
    }
}
