public class PassengersNumberValidator implements Validator {

    @Override
    public boolean validate(ValidationInformation validationInfo) {
        boolean numberOfPassengersIsOk = validationInfo
                .getFloors()
                .stream()
                .mapToLong(floor -> floor.getArrivalContainer().size())
                .sum() == validationInfo.getPassengersNumber();
        if (numberOfPassengersIsOk) {
            Validators.LOGGER.info("The amount of passengers in arrival containers" +
                    " is equal to the initial passengersNumber - \u2713");
        } else {
            Validators.LOGGER.info("The amount of passengers in arrival containers" +
                    " is equal to the initial passengersNumber - \u274C");
        }
        return numberOfPassengersIsOk;
    }
}
