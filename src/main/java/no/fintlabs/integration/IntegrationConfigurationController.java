package no.fintlabs.integration;

import no.fintlabs.integration.model.IntegrationConfiguration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/integration/configuration")
public class IntegrationConfigurationController {

    private final IntegrationConfigurationService integrationConfigurationService;

    public IntegrationConfigurationController(IntegrationConfigurationService integrationConfigurationService) {
        this.integrationConfigurationService = integrationConfigurationService;
    }

    @GetMapping
    public ResponseEntity<Page<IntegrationConfiguration>> getIntegrationConfigurations(@RequestParam(value = "page", defaultValue = "0") int pageIndex,
                                                                                       @RequestParam(value = "size", defaultValue = "50") int pageSize) {

        return ResponseEntity.ok(integrationConfigurationService.getAllIntegrationConfiguration(PageRequest.of(pageIndex, pageSize)));
    }

    @PostMapping
    public ResponseEntity<String> newIntegrationConfiguration(@RequestBody IntegrationConfiguration integrationConfiguration,
                                                              ServerHttpRequest httpRequest) {
        IntegrationConfiguration savedIntegrationConfiguration = integrationConfigurationService.newIntegrationConfiguration(integrationConfiguration);
        return ResponseEntity.created(UriComponentsBuilder
                        .fromHttpRequest(httpRequest)
                        .path("/" + savedIntegrationConfiguration.getId())
                        .build()
                        .toUri())
                .build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateIntegrationConfiguration(@RequestBody IntegrationConfiguration integrationConfiguration,
                                                              ServerHttpRequest httpRequest) {
        integrationConfigurationService.updateIntegrationConfiguration(integrationConfiguration);
        return ResponseEntity.ok().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<List<IntegrationConfiguration>> getIntegrationConfigurationsById(@PathVariable String id) {
        List<IntegrationConfiguration> integrationConfigurations = integrationConfigurationService.getIntegrationConfigurationById(id);

        if (integrationConfigurations.size() > 0) {
            return ResponseEntity.ok(integrationConfigurations);
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}/{version}")
    public ResponseEntity<IntegrationConfiguration> getIntegrationConfigurationsByIdAndVersion(
            @PathVariable String id,
            @PathVariable int version) {

        return ResponseEntity.ok(
                integrationConfigurationService
                        .getIntegrationConfigurationByIdAndVersion(id, version)
                        .orElseThrow(IntegrationConfigurationVersionNotFound::new)
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
