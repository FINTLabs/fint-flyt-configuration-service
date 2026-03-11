package no.novari.flyt.configuration.validation.valueparsability

import no.novari.flyt.configuration.model.configuration.dtos.ValueMappingDto
import no.novari.flyt.configuration.model.configuration.entities.ValueMapping

interface ValueParsabilityValidator {
    fun isValid(valueMappingDto: ValueMappingDto): Boolean =
        valueMappingDto.type != getTypeToValidate() ||
            valueMappingDto.mappingString?.let(::isValid) != false

    fun getTypeToValidate(): ValueMapping.Type

    fun isValid(value: String): Boolean
}
