package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.ValueMappingDto
import no.fintlabs.model.configuration.entities.ValueMapping
import no.fintlabs.model.metadata.InstanceElementMetadata
import no.fintlabs.validation.ConfigurationValidationContext
import no.fintlabs.validation.instancefield.InstanceFieldReferenceKeyExtractionService
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

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title")
                .build()

        when:
        boolean valid = instanceFieldReferenceKeysExistInMetadataValidator.isValid(
                valueMappingDto,
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

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title \$if{instanceFieldKey1} \$if{instanceFieldKey2}")
                .build()

        when:
        boolean valid = instanceFieldReferenceKeysExistInMetadataValidator.isValid(
                valueMappingDto,
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

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title \$if{instanceFieldKey1} \$if{instanceFieldKey2}")
                .build()

        when:
        boolean valid = instanceFieldReferenceKeysExistInMetadataValidator.isValid(
                valueMappingDto,
                hibernateConstraintValidatorContext
        )

        then:
        !valid
    }

}
