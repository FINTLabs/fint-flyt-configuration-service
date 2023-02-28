package no.fintlabs.validation.valueparsability.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.valueparsability.ValueParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class DynamicStringParsabilityValidator implements ValueParsabilityValidator {

    private static final Pattern textPattern = Pattern.compile("(?:(?!\\$if\\{).)*");
    private static final Pattern instanceValueKeyPattern = Pattern.compile("(?:(?!\\$if\\{).)+");
    private static final Pattern ifReferencePattern = Pattern.compile("(?:\\$if\\{" + instanceValueKeyPattern + "})*");
    private static final Pattern dynamicStringPattern = Pattern.compile("^(?:" + textPattern + "|" + ifReferencePattern + ")*$");

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.DYNAMIC_STRING;
    }

    @Override
    public boolean isValid(String value) {
        return dynamicStringPattern.matcher(value).matches();
    }

}
