package org.chip.ihl.surveymanager.rest.validator;

import org.chip.ihl.surveymanager.config.WrapperConfiguration;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

/**
 * Created by sboykin on 12/9/2014.
 */
public class ConfigValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return WrapperConfiguration.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "redcapApiToken", "valid.token");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "messagingUrl", "valid.broker.url");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "messagingUsername", "valid.broker.username");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "messagingQueue", "valid.broker.queue");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "messagingSendTimeout", "valid.broker.send.timeout");
    }
}
