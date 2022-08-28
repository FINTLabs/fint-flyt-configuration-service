package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static no.fintlabs.resourceserver.UrlPaths.INTERNAL_API;

@RestController
@RequestMapping(INTERNAL_API + "/integrasjon/konfigurasjon")
public class IntegrationConfigurationController {

    private final IntegrationConfigurationService integrationConfigurationService;

    public IntegrationConfigurationController(IntegrationConfigurationService integrationConfigurationService) {
        this.integrationConfigurationService = integrationConfigurationService;
    }

    @GetMapping
    public ResponseEntity<Page<IntegrationConfiguration>> getLatestIntegrationConfigurations(
            @RequestParam(value = "page", defaultValue = "0") int pageIndex,
            @RequestParam(value = "size", defaultValue = "50") int pageSize) {

        List<IntegrationConfiguration> latestIntegrationConfigurations
                = integrationConfigurationService.getLatestIntegrationConfigurations();

        return ResponseEntity.ok(
                new PageImpl<>(
                        latestIntegrationConfigurations,
                        PageRequest.of(pageIndex, pageSize),
                        latestIntegrationConfigurations.size()
                )
        );
    }

    @PostMapping
    public ResponseEntity<String> newIntegrationConfiguration(
            @RequestBody IntegrationConfiguration integrationConfiguration,
            ServerHttpRequest httpRequest) {

        IntegrationConfiguration savedIntegrationConfiguration
                = integrationConfigurationService.newIntegrationConfiguration(integrationConfiguration);

        return ResponseEntity.created(UriComponentsBuilder
                        .fromHttpRequest(httpRequest)
                        .path("/" + savedIntegrationConfiguration.getSourceApplicationIntegrationId())
                        .build()
                        .toUri())
                .build();

    }

    @PutMapping("/{sourceApplicationIntegrationId}")
    public ResponseEntity<Void> addNewIntegrationConfigurationVersion(
            @PathVariable String sourceApplicationIntegrationId,
            @RequestBody IntegrationConfiguration integrationConfiguration) {

        integrationConfigurationService.addNewIntegrationConfigurationVersion(sourceApplicationIntegrationId, integrationConfiguration);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{sourceApplicationIntegrationId}")
    public ResponseEntity<Void> deleteIntegrationConfigurationById(@PathVariable String sourceApplicationIntegrationId) {
        integrationConfigurationService.deleteIntegrationConfigurationById(sourceApplicationIntegrationId);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{sourceApplicationIntegrationId}")
    public ResponseEntity<List<IntegrationConfiguration>> getIntegrationConfigurationsById(@PathVariable String sourceApplicationIntegrationId) {
        List<IntegrationConfiguration> integrationConfigurations
                = integrationConfigurationService.getIntegrationConfigurationById(sourceApplicationIntegrationId);

        if (integrationConfigurations.size() > 0) {
            return ResponseEntity.ok(integrationConfigurations);
        }

        throw new IntegrationConfigurationNotFound();
    }

    @GetMapping("/{sourceApplicationIntegrationId}/{version}")
    public ResponseEntity<IntegrationConfiguration> getIntegrationConfigurationsByIdAndVersion(
            @PathVariable String sourceApplicationIntegrationId,
            @PathVariable int version) {

        return ResponseEntity.ok(
                integrationConfigurationService
                        .getIntegrationConfigurationByIdAndVersion(sourceApplicationIntegrationId, version)
                        .orElseThrow(IntegrationConfigurationVersionNotFound::new)
        );
    }

    @GetMapping("/{sourceApplicationIntegrationId}/latest")
    public ResponseEntity<IntegrationConfiguration> getLatestIntegrationConfigurations(@PathVariable String sourceApplicationIntegrationId) {

        return ResponseEntity.ok(
                integrationConfigurationService
                        .getLatestIntegrationConfigurationById(sourceApplicationIntegrationId)
        );
    }

    @ExceptionHandler(IntegrationConfigurationNotFound.class)
    public ResponseEntity<Void> handleIntegrationConfigurationNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IntegrationConfigurationVersionNotFound.class)
    public ResponseEntity<Void> handleIntegrationConfigurationVersionNotFound() {
        return ResponseEntity.notFound().build();
    }
}
