package no.novari.flyt.configuration.validation.constraints

import no.novari.flyt.configuration.model.configuration.dtos.ObjectMappingDto
import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock

class AtLeastOneChildValidatorTest {
    private lateinit var atLeastOneChildValidator: AtLeastOneChildValidator

    @BeforeEach
    fun setup() {
        atLeastOneChildValidator = AtLeastOneChildValidator()
    }

    @Test
    fun shouldReturnTrueWhenObjectMappingHasChildren() {
        val objectMappingDto = ObjectMappingDto.builder().build()
        objectMappingDto.valueMappingPerKey["valueMappingKey"] = mock<ValueMappingDto>()

        val valid =
            atLeastOneChildValidator.isValid(
                objectMappingDto,
                mock<HibernateConstraintValidatorContext>(),
            )

        assertTrue(valid)
    }

    @Test
    fun shouldReturnFalseWhenObjectMappingHasNoChildren() {
        val valid =
            atLeastOneChildValidator.isValid(
                ObjectMappingDto.builder().build(),
                mock<HibernateConstraintValidatorContext>(),
            )

        assertFalse(valid)
    }
}
