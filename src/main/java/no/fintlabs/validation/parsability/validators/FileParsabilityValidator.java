package no.fintlabs.validation.parsability.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class FileParsabilityValidator implements FieldParsabilityValidator {

    private static final Pattern textPattern = Pattern.compile("(?:(?!\\$if\\{).)*");
    private static final Pattern fileStringPattern = Pattern.compile("^\\$if\\{" + textPattern + "}$");

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.FILE;
    }

    @Override
    public boolean isValid(String value) {
        return fileStringPattern.matcher(value).matches();
    }
}
