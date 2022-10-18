package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.configuration.entities.FieldConfiguration;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class DynamicStringParsabilityValidator implements FieldParsabilityValidator {

    private static final Pattern textPattern = Pattern.compile("(?:(?!\\$if\\{).)*");
    private static final Pattern instanceFieldKeyPattern = Pattern.compile("(?:(?!\\$if\\{).)+");
    private static final Pattern ifReferencePattern = Pattern.compile("(?:\\$if\\{" + instanceFieldKeyPattern + "})*");
    private static final Pattern dynamicStringPattern = Pattern.compile("^(?:" + textPattern + "|" + ifReferencePattern + ")*$");

    @Override
    public FieldConfiguration.Type getTypeToValidate() {
        return FieldConfiguration.Type.DYNAMIC_STRING;
    }

    @Override
    public boolean isValid(String value) {
        return dynamicStringPattern.matcher(value).matches();
    }

}
