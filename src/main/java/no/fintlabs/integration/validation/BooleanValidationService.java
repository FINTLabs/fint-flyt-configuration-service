package no.fintlabs.integration.validation;

import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class BooleanValidationService {

    public boolean validateValuesAreParsableAsBooleans(Collection<String> values) {
        return values.stream().allMatch(this::validateValueIsParsableAsBoolean);
    }

    public boolean validateValueIsParsableAsBoolean(String value) {
        return value.equals("true") || value.equals("false");
    }

}
