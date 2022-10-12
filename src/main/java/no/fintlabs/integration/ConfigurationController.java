package no.fintlabs.integration;

import no.fintlabs.integration.model.configuration.Configuration;
import no.fintlabs.integration.model.web.ConfigurationPatch;
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
    private final Validator validator;
    private final ValidationErrorsFormattingService validationErrorsFormattingService;

    public ConfigurationController(
            ConfigurationService configurationService,
            Validator validator,
            ValidationErrorsFormattingService validationErrorsFormattingService
    ) {
        this.configurationService = configurationService;
        this.validator = validator;
        this.validationErrorsFormattingService = validationErrorsFormattingService;
    }

    @GetMapping
    public ResponseEntity<Collection<Configuration>> getConfigurations(
            @RequestParam(name = "integrasjonId") Optional<Long> integrationId
    ) {
        return ResponseEntity.ok(
                integrationId
                        .map(configurationService::findAllForIntegrationId)
                        .orElseGet(configurationService::findAll)
        );
    }

    @GetMapping("{configurationId}")
    public ResponseEntity<Configuration> getConfiguration(
            @PathVariable Long configurationId
    ) {
        Configuration configuration = configurationService
                .findById(configurationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(configuration);
    }

    @PostMapping
    public ResponseEntity<Configuration> postConfiguration(
            @RequestBody Configuration configuration
    ) {
        Set<ConstraintViolation<Configuration>> constraintViolations = validator.validate(configuration);
        if (!constraintViolations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    validationErrorsFormattingService.format(constraintViolations)
            );
        }
        return ResponseEntity.ok(configurationService.save(configuration));
    }

    @PatchMapping("{configurationId}")
    public ResponseEntity<Configuration> patchConfiguration(
            @PathVariable Long configurationId,
            @RequestBody ConfigurationPatch configurationPatch
    ) {
        Configuration configuration = configurationService.findById(configurationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (configuration.isCompleted()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Configuration is already complete, and cannot be altered"
            );
        }

        Set<ConstraintViolation<ConfigurationPatch>> constraintViolations = validator.validate(configurationPatch);
        if (!constraintViolations.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    validationErrorsFormattingService.format(constraintViolations)
            );
        }

        configurationPatch.getIntegrationMetadataId().ifPresent(configuration::setIntegrationMetadataId);
        configurationPatch.isCompleted().ifPresent(configuration::setCompleted);
        configurationPatch.getComment().ifPresent(configuration::setComment);
        configurationPatch.getElements().ifPresent(elements -> {
            configuration.getElements().clear();
            configuration.getElements().addAll(elements);
        });

        Configuration resultConfiguration = configurationService.save(configuration);

        return ResponseEntity.ok(resultConfiguration);
    }

}
