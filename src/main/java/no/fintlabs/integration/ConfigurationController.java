package no.fintlabs.integration;

import no.fintlabs.integration.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.integration.model.configuration.dtos.ConfigurationPatchDto;
import no.fintlabs.integration.validation.ConfigurationValidatorFacory;
import no.fintlabs.integration.validation.ValidationErrorsFormattingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;

@RestController
@RequestMapping(INTERNAL_API + "/konfigurasjoner")
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final ConfigurationValidatorFacory configurationValidatorFacory;
    private final ValidationErrorsFormattingService validationErrorsFormattingService;

    public ConfigurationController(
            ConfigurationService configurationService,
            ConfigurationValidatorFacory configurationValidatorFacory,
            ValidationErrorsFormattingService validationErrorsFormattingService
    ) {
        this.configurationService = configurationService;
        this.configurationValidatorFacory = configurationValidatorFacory;
        this.validationErrorsFormattingService = validationErrorsFormattingService;
    }

    @GetMapping
    public ResponseEntity<Collection<ConfigurationDto>> getConfigurations(
            @RequestParam(name = "integrasjonId") Optional<Long> integrationId,
            @RequestParam(name = "ekskluderElementer", required = false) boolean excludeElements
    ) {
        return ResponseEntity.ok(
                integrationId
                        .map(iid -> configurationService.findByIntegrationId(iid, excludeElements))
                        .orElseGet(() -> configurationService.findAll(excludeElements))
        );
    }

    @GetMapping("{configurationId}")
    public ResponseEntity<ConfigurationDto> getConfiguration(
            @PathVariable Long configurationId,
            @RequestParam(name = "ekskluderElementer", required = false) boolean excludeElements
    ) {
        return ResponseEntity.ok(
                configurationService
                        .findById(configurationId, excludeElements)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @PostMapping
    public ResponseEntity<ConfigurationDto> postConfiguration(
            @RequestBody ConfigurationDto configurationDto
    ) {
        validateBeanConstraints(configurationDto.getIntegrationId(), configurationDto.getIntegrationMetadataId(), configurationDto);
        return ResponseEntity.ok(configurationService.save(configurationDto));
    }

    @PatchMapping("{configurationId}")
    public ResponseEntity<ConfigurationDto> patchConfiguration(
            @PathVariable Long configurationId,
            @RequestBody ConfigurationPatchDto configurationPatchDto
    ) {
        ConfigurationDto configurationDto = configurationService.findById(configurationId, false)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        validateIsNotCompleted(configurationDto);

        configurationPatchDto.getIntegrationMetadataId().ifPresent(configurationDto::setIntegrationMetadataId);
        configurationPatchDto.isCompleted().filter(Boolean::booleanValue).ifPresent(configurationDto::setCompleted);
        configurationPatchDto.getComment().ifPresent(configurationDto::setComment);
        configurationPatchDto.getElements().ifPresent(configurationDto::setElements);

        validateBeanConstraints(
                configurationDto.getIntegrationId(),
                configurationDto.getIntegrationMetadataId(),
                configurationDto
        );

        return ResponseEntity.ok(configurationService.updateById(configurationId, configurationPatchDto));
    }

    @DeleteMapping("{configurationId}")
    public ResponseEntity<?> deleteConfiguration(
            @PathVariable Long configurationId
    ) {
        ConfigurationDto configurationDto = configurationService.findById(configurationId, true)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        validateIsNotCompleted(configurationDto);
        configurationService.deleteById(configurationId);
        return ResponseEntity.noContent().build();
    }

    private void validateIsNotCompleted(ConfigurationDto configurationDto) {
        if (configurationDto.isCompleted()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Configuration is completed, and cannot be altered"
            );
        }
    }

    private <T> void validateBeanConstraints(long integrationId, long metadataId, T object) {
        Validator validator = configurationValidatorFacory.getValidator(
                integrationId, metadataId
        );
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(object);
        if (!constraintViolations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    validationErrorsFormattingService.format(constraintViolations)
            );
        }
    }

}
