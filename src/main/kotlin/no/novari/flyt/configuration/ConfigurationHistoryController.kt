package no.novari.flyt.configuration

import no.novari.flyt.audit.web.HistoryControllerSupport
import no.novari.flyt.configuration.model.configuration.dtos.ConfigurationSnapshot
import no.novari.flyt.configuration.model.configuration.entities.Configuration
import no.novari.flyt.webresourceserver.UrlPaths.INTERNAL_API
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("$INTERNAL_API/konfigurasjoner")
class ConfigurationHistoryController(
    historyService: ConfigurationHistoryService,
) : HistoryControllerSupport<Configuration, Long, ConfigurationSnapshot>(historyService)
