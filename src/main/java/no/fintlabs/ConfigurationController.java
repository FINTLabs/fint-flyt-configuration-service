package no.fintlabs;

import no.fintlabs.model.configuration.dtos.ConfigurationDto;
import no.fintlabs.model.configuration.dtos.ConfigurationPatchDto;
import no.fintlabs.validation.ConfigurationValidatorFacory;
import no.fintlabs.validation.ValidationErrorsFormattingService;
import no.fintlabs.validation.groups.Completed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.groups.Default;
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
    public ResponseEntity<Page<ConfigurationDto>> getConfigurations(
            @RequestParam(name = "side") int page,
            @RequestParam(name = "antall") int size,
            @RequestParam(name = "sorteringFelt") String sortProperty,
            @RequestParam(name = "sorteringRetning") Sort.Direction sortDirection,
            @RequestParam(name = "integrasjonId") Optional<Long> integrationId,
            @RequestParam(name = "ferdigstilt") Optional<Boolean> complete,
            @RequestParam(name = "ekskluderMapping", required = false) boolean excludeMapping
    ) {
        ConfigurationFilter filter = ConfigurationFilter
                .builder()
                .integrationId(integrationId.orElse(null))
                .completed(complete.orElse(null))
                .build();

        PageRequest pageRequest = PageRequest
                .of(page, size)
                .withSort(sortDirection, sortProperty);

        return ResponseEntity.ok(configurationService.findAll(filter, excludeMapping, pageRequest));
    }

    @GetMapping("{configurationId}")
    public ResponseEntity<ConfigurationDto> getConfiguration(
            @PathVariable Long configurationId,
            @RequestParam(name = "ekskluderMapping", required = false) boolean excludeMapping
    ) {
        return ResponseEntity.ok(
                configurationService
                        .findById(configurationId, excludeMapping)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
        );
    }

    @PostMapping
    public ResponseEntity<ConfigurationDto> postConfiguration(
            @RequestBody ConfigurationDto configurationDto
    ) {
        validateBeanConstraints(
                configurationDto.getIntegrationId(),
                configurationDto.getIntegrationMetadataId(),
                configurationDto
        );
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

        ConfigurationDto.ConfigurationDtoBuilder configurationDtoBuilder = configurationDto.toBuilder();

        configurationPatchDto.getIntegrationMetadataId().ifPresent(configurationDtoBuilder::integrationMetadataId);
        configurationPatchDto.isCompleted().filter(Boolean::booleanValue).ifPresent(configurationDtoBuilder::completed);
        configurationPatchDto.getComment().ifPresent(configurationDtoBuilder::comment);
        configurationPatchDto.getMapping().ifPresent(configurationDtoBuilder::mapping);

        ConfigurationDto newConfigurationDto = configurationDtoBuilder.build();

        validateBeanConstraints(
                newConfigurationDto.getIntegrationId(),
                newConfigurationDto.getIntegrationMetadataId(),
                newConfigurationDto
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

    private void validateBeanConstraints(long integrationId, long metadataId, ConfigurationDto configurationDto) {
        Validator validator = configurationValidatorFacory.getValidator(
                integrationId, metadataId
        );
        Set<ConstraintViolation<ConfigurationDto>> constraintViolations = configurationDto.isCompleted()
                ? validator.validate(configurationDto, Default.class, Completed.class)
                : validator.validate(configurationDto, Default.class);
        if (!constraintViolations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    validationErrorsFormattingService.format(constraintViolations)
            );
        }
    }

}
