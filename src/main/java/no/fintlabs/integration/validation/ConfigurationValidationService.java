package no.fintlabs.integration.validation;

import lombok.Data;
import no.fintlabs.integration.model.Configuration;
import no.fintlabs.integration.model.ConfigurationElement;
import no.fintlabs.integration.model.FieldCollectionConfiguration;
import no.fintlabs.integration.model.FieldConfiguration;
import no.fintlabs.integration.model.web.ConfigurationPatch;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ConfigurationValidationService {

    private final UrlValidationService urlValidationService;
    private final BooleanValidationService booleanValidationService;
    private final DynamicStringValidationService dynamicStringValidationService;

    public ConfigurationValidationService(
            UrlValidationService urlValidationService,
            BooleanValidationService booleanValidationService,
            DynamicStringValidationService dynamicStringValidationService
    ) {
        this.urlValidationService = urlValidationService;
        this.booleanValidationService = booleanValidationService;
        this.dynamicStringValidationService = dynamicStringValidationService;
    }


    @Data
    public static class ElementErrors {
        private final String objectKey;
        private final Collection<ElementErrors> elementErrors;
        private final Collection<Error> errors;
    }


    @Data
    public static class Error {
        private final String message;
    }

    public Optional<ElementErrors> validate(Configuration configuration) {
        Collection<Error> duplicateKeys = validateForDuplicateKeys(
                configuration.getElements()
                        .stream()
                        .map(ConfigurationElement::getKey)
                        .toList()
        );
        Collection<ElementErrors> elementErrors = validateConfigurationElements(configuration.getElements());
        return duplicateKeys.isEmpty() && elementErrors.isEmpty()
                ? Optional.empty()
                : Optional.of(new ElementErrors(
                        "configuration",
                        elementErrors,
                        duplicateKeys
                )
        );
    }

    public Optional<ElementErrors> validate(ConfigurationPatch configurationPatch) {
        if (configurationPatch.getElements().isEmpty()) {
            return Optional.empty();
        }
        Collection<Error> duplicateKeys = validateForDuplicateKeys(
                configurationPatch.getElements().get()
                        .stream()
                        .map(ConfigurationElement::getKey)
                        .toList()
        );
        Collection<ElementErrors> elementErrors = validateConfigurationElements(
                configurationPatch.getElements().get()
        );
        return duplicateKeys.isEmpty() && elementErrors.isEmpty()
                ? Optional.empty()
                : Optional.of(new ElementErrors(
                        "configuration",
                        elementErrors,
                        duplicateKeys
                )
        );
    }

    private Collection<ElementErrors> validateConfigurationElements(Collection<ConfigurationElement> configurationElements) {
        return configurationElements.stream()
                .map(this::validateConfigurationElement)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ElementErrors> validateConfigurationElement(ConfigurationElement configurationElement) {
        Collection<ElementErrors> elementErrors = Stream.of(
                        validateFieldConfigurations(configurationElement.getFieldConfigurations()),
                        validateFieldCollectionConfigurations(configurationElement.getFieldCollectionConfigurations()),
                        validateConfigurationElements(configurationElement.getElements())
                )
                .flatMap(Collection::stream)
                .toList();

        Collection<Error> duplicateKeyErrors = validateForDuplicateKeys(configurationElement);
        return elementErrors.isEmpty() && duplicateKeyErrors.isEmpty()
                ? Optional.empty()
                : Optional.of(
                new ElementErrors(
                        configurationElement.getKey(),
                        elementErrors,
                        duplicateKeyErrors
                )
        );
    }

    private Collection<Error> validateForDuplicateKeys(ConfigurationElement configurationElement) {
        List<String> keys = Stream.of(
                        configurationElement.getFieldConfigurations()
                                .stream()
                                .map(FieldConfiguration::getKey)
                                .toList(),
                        configurationElement.getFieldCollectionConfigurations()
                                .stream()
                                .map(FieldCollectionConfiguration::getKey)
                                .toList(),
                        configurationElement.getElements()
                                .stream()
                                .map(ConfigurationElement::getKey)
                                .toList()
                )
                .flatMap(Collection::stream)
                .toList();

        return validateForDuplicateKeys(keys);
    }

    private Collection<Error> validateForDuplicateKeys(List<String> keys) {
        Set<String> keySet = new HashSet<>();
        Set<String> duplicateKeys = keys
                .stream()
                .filter(key -> !keySet.add(key))
                .collect(Collectors.toSet());

        return duplicateKeys
                .stream()
                .map(Error::new)
                .toList();
    }

    private Collection<ElementErrors> validateFieldConfigurations(Collection<FieldConfiguration> fieldConfigurations) {
        return fieldConfigurations.stream()
                .map(this::validateFieldConfiguration)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ElementErrors> validateFieldConfiguration(FieldConfiguration fieldConfiguration) {
        boolean valid = switch (fieldConfiguration.getType()) {
            case STRING -> true;
            case URL -> urlValidationService.validateValueIsParsableAsUrl(fieldConfiguration.getValue());
            case BOOLEAN -> booleanValidationService.validateValueIsParsableAsBoolean(fieldConfiguration.getValue());
            case DYNAMIC_STRING -> dynamicStringValidationService.validateValueIsParsableAsDynamicString(fieldConfiguration.getValue());
        };
        return valid
                ? Optional.empty()
                : Optional.of(
                new ElementErrors(
                        fieldConfiguration.getKey(),
                        Collections.emptyList(),
                        List.of(new Error("contains value that is not parsable as type=" + fieldConfiguration.getType()))
                )
        );
    }

    private Collection<ElementErrors> validateFieldCollectionConfigurations(Collection<FieldCollectionConfiguration> fieldCollectionConfigurations) {
        return fieldCollectionConfigurations
                .stream()
                .map(this::validateFieldCollectionConfiguration)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    private Optional<ElementErrors> validateFieldCollectionConfiguration(FieldCollectionConfiguration fieldCollectionConfiguration) {
        boolean valid = switch (fieldCollectionConfiguration.getType()) {
            case STRING -> true;
            case URL -> urlValidationService.validateValuesAreParsableAsUrls(fieldCollectionConfiguration.getValues());
            case BOOLEAN -> booleanValidationService.validateValuesAreParsableAsBooleans(fieldCollectionConfiguration.getValues());
        };
        return valid
                ? Optional.empty()
                : Optional.of(
                new ElementErrors(
                        fieldCollectionConfiguration.getKey(),
                        Collections.emptyList(),
                        List.of(new Error("contains values that are not parsable as type=" + fieldCollectionConfiguration.getType()))
                )
        );
    }

}
