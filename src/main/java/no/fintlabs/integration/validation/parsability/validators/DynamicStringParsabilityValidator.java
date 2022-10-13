package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.configuration.FieldConfiguration;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import no.fintlabs.integration.validation.ConfigurationValidationContext;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collection;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Order(0)
public class DynamicStringParsabilityValidator implements FieldParsabilityValidator {

    private final InstanceFieldReferenceValidationService instanceFieldReferenceValidationService;

    private static final Pattern textPattern = Pattern.compile("(?:(?!\\$if\\{).)*");
    private static final Pattern instanceFieldKeyPattern = Pattern.compile("(?:(?!\\$if\\{).)+");
    private static final Pattern ifReferencePattern = Pattern.compile("(?:\\$if\\{" + instanceFieldKeyPattern + "})*");
    private static final Pattern dynamicStringPattern = Pattern.compile(
            "^(?:" + textPattern + "|" + ifReferencePattern + ")*$");

    private static final Pattern ifReferenceExtractionPattern = Pattern.compile("\\$if\\{[^}]+}");

    public DynamicStringParsabilityValidator(InstanceFieldReferenceValidationService instanceFieldReferenceValidationService) {
        this.instanceFieldReferenceValidationService = instanceFieldReferenceValidationService;
    }

    @Override
    public FieldConfiguration.Type getTypeToValidate() {
        return FieldConfiguration.Type.DYNAMIC_STRING;
    }

    @Override
    public boolean isValid(String value, ConfigurationValidationContext configurationValidationContext) {
        return dynamicStringPattern.matcher(value).matches() &&
                instanceFieldReferenceValidationService.isValid(
                        extractIfReferenceKeys(value),
                        Arrays.stream(InstanceElementMetadata.Type.values()).collect(Collectors.toSet()),
                        configurationValidationContext.getMetadataInstanceFieldTypePerKey()
                );
    }

    private Collection<String> extractIfReferenceKeys(String value) {
        return ifReferenceExtractionPattern.matcher(value)
                .results()
                .map(MatchResult::group)
                .map(ifReference -> ifReference.replace("$if{", "").replace("}", ""))
                .toList();
    }

}
