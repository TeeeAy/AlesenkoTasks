import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class Validators {

    public static final Logger LOGGER = LoggerFactory.getLogger("validation");

    List<Validator> validatorList = new ArrayList<>();

    public Validators(List<Validator> validators) {
        validatorList.addAll(validators);
    }

    public void validate(ValidationInformation validationInfo) {
        boolean isValid = validatorList.
                stream()
                .allMatch(validator -> validator.validate(validationInfo));
        String message;
        if (isValid) {
            message = "VALIDATION SUCCESSFUL";
        } else {
            message = "VALIDATION FAILED. SOMETHING WENT WRONG!!!";
        }
        Validators.LOGGER.info(message);
    }


}
