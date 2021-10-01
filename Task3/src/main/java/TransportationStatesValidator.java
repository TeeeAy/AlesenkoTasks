public class TransportationStatesValidator implements Validator {

    @Override
    public boolean validate(ValidationInformation validationInfo) {
        boolean allTransportationsAreCompleted = validationInfo
                .getFloors()
                .stream()
                .flatMap(floor -> floor.getArrivalContainer().stream())
                .allMatch(passenger -> passenger.getTransportationState() == TransportationState.COMPLETED);
        String value;
        if (allTransportationsAreCompleted) {
            value = "\u2713";
        } else {
            value = "\u274C";
        }
        Validators.LOGGER.info("All passengers' transportation states are COMPLETED - " + value);
        return allTransportationsAreCompleted;
    }
}
