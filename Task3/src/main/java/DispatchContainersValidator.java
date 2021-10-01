public class DispatchContainersValidator implements Validator {

    @Override
    public boolean validate(ValidationInformation validationInformation) {
        boolean areDispatchContainersEmpty = validationInformation
                .getFloors()
                .stream()
                .allMatch(Floor::isDispatchContainerEmpty);
        String value;
        if (areDispatchContainersEmpty) {
            value = "\u2713";
        } else {
            value = "\u274C";
        }
        Validators.LOGGER.info("All dispatchContainers are empty - " + value);
        return areDispatchContainersEmpty;
    }
}
