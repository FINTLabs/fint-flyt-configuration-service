package no.fintlabs.integration.model.configuration.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigurationElementsDto {
    private Collection<ConfigurationElementDto> elements;
}
