public class PassengersNumberValidator implements Validator {

    @Override
    public boolean validate(ValidationInformation validationInfo) {
        boolean numberOfPassengersIsOk = validationInfo
                .getFloors()
                .stream()
                .mapToLong(floor -> floor.getArrivalContainer().size())
                .sum() == validationInfo.getPassengersNumber();
        String value;
        if (numberOfPassengersIsOk) {
            value = "\u2713";
        } else {
            value = "\u274C";
        }
        Validators.LOGGER.info("The amount of passengers in arrival containers" +
                " is equal to the initial passengersNumber - " + value);
        return numberOfPassengersIsOk;
    }
}
