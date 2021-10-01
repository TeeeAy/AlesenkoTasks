public class ElevatorValidator implements Validator {

    public boolean validate(ValidationInformation validationInformation) {
        boolean isElevatorEmpty = validationInformation.getElevator().isEmpty();
        String value;
        if (isElevatorEmpty) {
            value = "\u2713";
        } else {
            value = "\u274C";
        }
        Validators.LOGGER.info("Elevator is empty - " + value);
        return isElevatorEmpty;
    }
}
