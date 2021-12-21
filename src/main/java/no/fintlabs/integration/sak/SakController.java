package no.fintlabs.integration.sak;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.arkiv.noark.MappeResource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping("sak")
public class SakController {

    private final SakRequestService sakRequestService;

    public SakController(SakRequestService sakRequestService) {
        this.sakRequestService = sakRequestService;
    }

    @GetMapping("tittel/mappeid/{caseYear}/{caseNumber}")
    public String getSakTittel(@PathVariable String caseYear, @PathVariable String caseNumber) {
        String mappeId = caseYear + "/" + caseNumber;
        return sakRequestService.getByMappeId(mappeId)
                .map(MappeResource::getTittel)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        String.format("Case with mappeId=%s not found", mappeId)
                ));
    }

}
