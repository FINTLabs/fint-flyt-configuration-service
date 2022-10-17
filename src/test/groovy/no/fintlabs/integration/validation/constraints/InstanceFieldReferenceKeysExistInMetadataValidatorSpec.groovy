package no.fintlabs.integration.validation.constraints

import no.fintlabs.integration.model.configuration.FieldConfiguration
import no.fintlabs.integration.model.metadata.InstanceElementMetadata
import no.fintlabs.integration.validation.ConfigurationValidationContext
import no.fintlabs.integration.validation.instancefield.InstanceFieldReferenceKeyExtractionService
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

class InstanceFieldReferenceKeysExistInMetadataValidatorSpec extends Specification {

    InstanceFieldReferenceKeysExistInMetadataValidator instanceFieldReferenceKeysExistInMetadataValidator

    def setup() {
        instanceFieldReferenceKeysExistInMetadataValidator = new InstanceFieldReferenceKeysExistInMetadataValidator(
                new InstanceFieldReferenceKeyExtractionService()
        )
    }

    def 'Should return true when value contains no instance field references'() {
        given:
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .metadataInstanceFieldTypePerKey(Map.of(
                        "instanceFieldKey1", InstanceElementMetadata.Type.STRING,
                        "instanceFieldKey2", InstanceElementMetadata.Type.STRING,
                        "instanceFieldKey3", InstanceElementMetadata.Type.STRING,
                ))
                .build()
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class) >> configurationValidationContext

        FieldConfiguration fieldConfiguration = FieldConfiguration
                .builder()
                .key("fieldConfigurationKey")
                .type(FieldConfiguration.Type.DYNAMIC_STRING)
                .value("Title")
                .build()

        when:
        boolean valid = instanceFieldReferenceKeysExistInMetadataValidator.isValid(
                fieldConfiguration,
                hibernateConstraintValidatorContext
        )

        then:
        valid
    }

    def 'Should return true when all instance field reference keys are found in the metadata'() {
        given:
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .metadataInstanceFieldTypePerKey(Map.of(
                        "instanceFieldKey1", InstanceElementMetadata.Type.STRING,
                        "instanceFieldKey2", InstanceElementMetadata.Type.STRING,
                        "instanceFieldKey3", InstanceElementMetadata.Type.STRING,
                ))
                .build()
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class) >> configurationValidationContext

        FieldConfiguration fieldConfiguration = FieldConfiguration
                .builder()
                .key("fieldConfigurationKey")
                .type(FieldConfiguration.Type.DYNAMIC_STRING)
                .value("Title \$if{instanceFieldKey1} \$if{instanceFieldKey2}")
                .build()

        when:
        boolean valid = instanceFieldReferenceKeysExistInMetadataValidator.isValid(
                fieldConfiguration,
                hibernateConstraintValidatorContext
        )

        then:
        valid
    }

    def 'Should return false when an instance field reference keys is not found in the metadata'() {
        given:
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .metadataInstanceFieldTypePerKey(Map.of(
                        "instanceFieldKey1", InstanceElementMetadata.Type.STRING,
                        "instanceFieldKey3", InstanceElementMetadata.Type.STRING,
                ))
                .build()
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class) >> configurationValidationContext

        FieldConfiguration fieldConfiguration = FieldConfiguration
                .builder()
                .key("fieldConfigurationKey")
                .type(FieldConfiguration.Type.DYNAMIC_STRING)
                .value("Title \$if{instanceFieldKey1} \$if{instanceFieldKey2}")
                .build()

        when:
        boolean valid = instanceFieldReferenceKeysExistInMetadataValidator.isValid(
                fieldConfiguration,
                hibernateConstraintValidatorContext
        )

        then:
        !valid
    }

}
