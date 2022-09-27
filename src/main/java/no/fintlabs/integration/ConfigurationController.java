package no.fintlabs.integration;

import no.fintlabs.integration.model.Configuration;
import no.fintlabs.integration.model.web.ConfigurationPatch;
import no.fintlabs.integration.validation.ConfigurationValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;

// TODO: 26/09/2022 Revert to published fint-flyt-resource-server version
@RestController
@RequestMapping(INTERNAL_API + "/konfigurasjoner")
public class ConfigurationController {

    private final ConfigurationService configurationService;
    private final ConfigurationValidationService configurationValidationService;

    public ConfigurationController(
            ConfigurationService configurationService,
            ConfigurationValidationService configurationValidationService
    ) {
        this.configurationService = configurationService;
        this.configurationValidationService = configurationValidationService;
    }

    @GetMapping
    public ResponseEntity<Collection<Configuration>> getConfigurations(
            @RequestParam Optional<String> integrationId
    ) {
        return ResponseEntity.ok(
                integrationId
                        .map(configurationService::findAllForIntegrationId)
                        .orElseGet(configurationService::findAll)
        );
    }

    @GetMapping("{configurationId}")
    public ResponseEntity<Configuration> getConfiguration(
            @PathVariable UUID configurationId
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
        configurationValidationService.validate(configuration).ifPresent(
                elementErrors -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            String.join(", ", formatValidationErrors(elementErrors))
                    );
                }
        );
        return ResponseEntity.ok(configurationService.save(configuration));
    }

    @PatchMapping("{configurationId}")
    public ResponseEntity<Configuration> patchConfiguration(
            @PathVariable UUID configurationId,
            @RequestBody ConfigurationPatch configurationPatch
    ) {
        Configuration configuration = configurationService.findById(configurationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (configuration.isCompleted()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Configuration is already complete, and cannot be altered"
            );
        }

        configurationValidationService.validate(configurationPatch).ifPresent(
                elementErrors -> {
                    throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                            String.join(", ", formatValidationErrors(elementErrors))
                    );
                }
        );

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

    private Collection<String> formatValidationErrors(ConfigurationValidationService.ElementErrors elementErrors) {
        return Stream.concat(
                elementErrors.getErrors()
                        .stream()
                        .map(error -> elementErrors.getObjectKey() + " " + error.getMessage()),

                elementErrors.getElementErrors()
                        .stream()
                        .map(this::formatValidationErrors)
                        .flatMap(Collection::stream)
                        .map(errorString -> elementErrors.getObjectKey() + "." + errorString)
        ).toList();
    }

}
