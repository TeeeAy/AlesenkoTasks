public class TransportationStatesValidator implements Validator {

    @Override
    public boolean validate(ValidationInformation validationInfo) {
        boolean allTransportationsAreCompleted = validationInfo
                .getFloors()
                .stream()
                .flatMap(floor -> floor.getArrivalContainer().stream())
                .allMatch(passenger -> passenger.getTransportationState() == TransportationState.COMPLETED);
        if (allTransportationsAreCompleted) {
            Validators.LOGGER.info("All passengers' transportation states are COMPLETED - \u2713");
        } else {
            Validators.LOGGER.info("All passengers' transportation states are COMPLETED - \u274C");
        }
        return allTransportationsAreCompleted;
    }
}
