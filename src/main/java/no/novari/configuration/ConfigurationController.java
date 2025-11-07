package no.novari.configuration;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.groups.Default;
import no.novari.configuration.model.configuration.dtos.ConfigurationDto;
import no.novari.configuration.model.configuration.dtos.ConfigurationPatchDto;
import no.novari.configuration.security.AuditorScope;
import no.novari.configuration.security.TokenParsingUtils;
import no.novari.configuration.validation.ConfigurationValidatorFactory;
import no.novari.configuration.validation.ValidationErrorsFormattingService;
import no.novari.configuration.validation.groups.Completed;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.Set;

import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;

@RestController
@RequestMapping(INTERNAL_API + "/konfigurasjoner")
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final ConfigurationValidatorFactory configurationValidatorFactory;
    private final ValidationErrorsFormattingService validationErrorsFormattingService;
    private final TokenParsingUtils tokenParsingUtils;

    public ConfigurationController(
            ConfigurationService configurationService,
            ConfigurationValidatorFactory configurationValidatorFactory,
            ValidationErrorsFormattingService validationErrorsFormattingService,
            TokenParsingUtils tokenParsingUtils
    ) {
        this.configurationService = configurationService;
        this.configurationValidatorFactory = configurationValidatorFactory;
        this.validationErrorsFormattingService = validationErrorsFormattingService;
        this.tokenParsingUtils = tokenParsingUtils;
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
            Authentication authentication,
            @RequestBody ConfigurationDto configurationDto
    ) {
        try (AuditorScope ignored = tokenParsingUtils.bindAuditor(authentication)) {
            validateBeanConstraints(
                    configurationDto.getIntegrationId(),
                    configurationDto.getIntegrationMetadataId(),
                    configurationDto
            );
            return ResponseEntity.ok(configurationService.save(configurationDto));
        }
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
        Validator validator = configurationValidatorFactory.getValidator(
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
