package no.fintlabs.validation.valueparsability.validators;

import no.fintlabs.model.configuration.entities.ValueMapping;
import no.fintlabs.validation.valueparsability.ValueParsabilityValidator;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class ValueConvertingParsabilityValidator implements ValueParsabilityValidator {

    private static final Pattern numberPattern = Pattern.compile("\\d+");
    private static final Pattern valueConverterReferencePattern = Pattern.compile("\\$vc\\{" + numberPattern + "}");

    private static final Pattern instanceValueKeyPattern = Pattern.compile("(?:(?!\\$if\\{).)+");
    private static final Pattern ifReferencePattern = Pattern.compile("\\$if\\{" + instanceValueKeyPattern + "}");
    private static final Pattern instanceCollectionFieldReferencePattern = Pattern.compile("\\$icf\\{" + numberPattern + "}\\{" + instanceValueKeyPattern + "}");


    private static final Pattern valueConvertingPattern = Pattern.compile("^" + valueConverterReferencePattern + "(" + ifReferencePattern + "|" + instanceCollectionFieldReferencePattern + ")$");

    @Override
    public ValueMapping.Type getTypeToValidate() {
        return ValueMapping.Type.DYNAMIC_STRING;
    }

    @Override
    public boolean isValid(String value) {
        return valueConvertingPattern.matcher(value).matches();
    }

}
