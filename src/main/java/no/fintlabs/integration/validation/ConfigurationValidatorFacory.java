package no.fintlabs.integration.validation;

import lombok.Getter;
import no.fintlabs.integration.model.metadata.InstanceElementMetadata;
import org.hibernate.validator.HibernateValidatorFactory;

import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ConfigurationValidatorFacory {

    @Getter
    private final ValidatorFactory validatorFactory;

    public ConfigurationValidatorFacory(ValidatorFactory validatorFactory) {
        this.validatorFactory = validatorFactory;
    }

    public Validator getValidator(Collection<InstanceElementMetadata> instanceElementMetadata) {
        return validatorFactory
                .unwrap(HibernateValidatorFactory.class)
                .usingContext()
                .constraintValidatorPayload(new ConfigurationValidationContext(
                        getInstanceFieldTypePerKey(instanceElementMetadata)
                                .stream()
                                .collect(Collectors.toMap(
                                        AbstractMap.SimpleEntry::getKey,
                                        AbstractMap.SimpleEntry::getValue
                                ))
                ))
                .getValidator();
    }

    private List<AbstractMap.SimpleEntry<String, InstanceElementMetadata.Type>> getInstanceFieldTypePerKey(
            Collection<InstanceElementMetadata> instanceElementMetadata
    ) {
        return instanceElementMetadata.stream()
                .map(this::getInstanceFieldTypePerKey)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    private List<AbstractMap.SimpleEntry<String, InstanceElementMetadata.Type>> getInstanceFieldTypePerKey(
            InstanceElementMetadata instanceElementMetadata
    ) {
        List<AbstractMap.SimpleEntry<String, InstanceElementMetadata.Type>> typesPerKey =
                getInstanceFieldTypePerKey(instanceElementMetadata.getChildren());
        if (instanceElementMetadata.getKey().isPresent() && instanceElementMetadata.getType().isPresent()) {
            typesPerKey.add(new AbstractMap.SimpleEntry<>(
                    instanceElementMetadata.getKey().get(),
                    instanceElementMetadata.getType().get()
            ));
        }
        return typesPerKey;
    }
}
