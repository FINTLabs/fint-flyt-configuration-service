package no.fintlabs.integration;

import no.fintlabs.integration.model.Configuration;
import no.fintlabs.integration.model.web.ConfigurationPatch;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.UUID;

import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;

// TODO: 26/09/2022 Revert to published fint-flyt-resource-server version
@RestController
@RequestMapping(INTERNAL_API + "/konfigurasjoner")
public class ConfigurationController {

    private final ConfigurationService configurationService;

    public ConfigurationController(ConfigurationService configurationService) {
        this.configurationService = configurationService;
    }

    @GetMapping
    public ResponseEntity<Collection<Configuration>> getConfigurations() {
        return ResponseEntity.ok(configurationService.findAll());
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

        configuration.setIntegrationMetadataId(configurationPatch.getIntegrationMetadataId());
        configuration.setCompleted(configurationPatch.isCompleted());
        configuration.setComment(configurationPatch.getComment());
        configuration.getElements().clear();
        configuration.getElements().addAll(configurationPatch.getElements());

        Configuration resultConfiguration = configurationService.save(configuration);

        return ResponseEntity.ok(resultConfiguration);
    }

}
