package no.fintlabs.validation.constraints

import no.fintlabs.model.configuration.dtos.ValueMappingDto
import no.fintlabs.model.configuration.entities.ValueMapping
import no.fintlabs.model.metadata.InstanceValueMetadata
import no.fintlabs.validation.ConfigurationValidationContext
import no.fintlabs.validation.instancereference.InstanceValueKeyExtractionService
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import spock.lang.Specification

class InstanceValueKeysAreDefinedInMetadataValidatorSpec extends Specification {

    InstanceValueKeysAreDefinedInMetadataValidator instanceValueExistInMetadataValidator

    def setup() {
        instanceValueExistInMetadataValidator = new InstanceValueKeysAreDefinedInMetadataValidator(
                new InstanceValueKeyExtractionService()
        )
    }

    def 'Should return true when value contains no instance field references'() {
        given:
        ConfigurationValidationContext configurationValidationContext = ConfigurationValidationContext
                .builder()
                .instanceValueTypePerKey(Map.of(
                        "instanceValueKey1", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey2", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3", InstanceValueMetadata.Type.STRING,
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
        boolean valid = instanceValueExistInMetadataValidator.isValid(
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
                .instanceValueTypePerKey(Map.of(
                        "instanceValueKey1", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey2", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3", InstanceValueMetadata.Type.STRING,
                ))
                .build()
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class) >> configurationValidationContext

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title \$if{instanceValueKey1} \$if{instanceValueKey2}")
                .build()

        when:
        boolean valid = instanceValueExistInMetadataValidator.isValid(
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
                .instanceValueTypePerKey(Map.of(
                        "instanceValueKey1", InstanceValueMetadata.Type.STRING,
                        "instanceValueKey3", InstanceValueMetadata.Type.STRING,
                ))
                .build()
        HibernateConstraintValidatorContext hibernateConstraintValidatorContext = Mock(HibernateConstraintValidatorContext.class)
        hibernateConstraintValidatorContext.getConstraintValidatorPayload(ConfigurationValidationContext.class) >> configurationValidationContext

        ValueMappingDto valueMappingDto = ValueMappingDto
                .builder()
                .type(ValueMapping.Type.DYNAMIC_STRING)
                .mappingString("Title \$if{instanceValueKey1} \$if{instanceValueKey2}")
                .build()

        when:
        boolean valid = instanceValueExistInMetadataValidator.isValid(
                valueMappingDto,
                hibernateConstraintValidatorContext
        )

        then:
        !valid
    }

}
