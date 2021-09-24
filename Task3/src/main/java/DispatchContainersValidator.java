public class DispatchContainersValidator implements Validator {

    @Override
    public boolean validate(ValidationInformation validationInformation) {
        boolean areDispatchContainersEmpty = validationInformation
                .getFloors()
                .stream()
                .allMatch(Floor::isDispatchContainerEmpty);
        if (areDispatchContainersEmpty) {
            Validators.LOGGER.info("All dispatchContainers are empty - \u2713");
        } else {
            Validators.LOGGER.info("All dispatchContainers are empty - \u274C");
        }
        return areDispatchContainersEmpty;
    }
}
