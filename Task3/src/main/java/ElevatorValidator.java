public class ElevatorValidator implements Validator {

    public boolean validate(ValidationInformation validationInformation) {
        boolean isElevatorEmpty = validationInformation.getElevator().isEmpty();
        if (isElevatorEmpty) {
            Validators.LOGGER.info("Elevator is empty - \u2713");
        } else {
            Validators.LOGGER.info("Elevator is empty - \u274C");
        }
        return isElevatorEmpty;
    }
}
