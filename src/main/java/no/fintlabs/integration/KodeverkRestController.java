package no.fintlabs.integration;

import lombok.extern.slf4j.Slf4j;
import no.fint.model.resource.FintLinks;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.ArkivressursResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.integration.model.ResourceReference;
import no.fintlabs.kafka.consumer.cache.FintCacheManager;
import no.fintlabs.kafka.util.links.ResourceLinkUtil;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/kodeverk/")
public class KodeverkRestController {
    private final FintCacheManager fintCacheManager;
    private final ArkivressursReferenceMapper arkivressursReferenceMapper;

    public KodeverkRestController(FintCacheManager fintCacheManager, ArkivressursReferenceMapper arkivressursReferenceMapper) {
        this.fintCacheManager = fintCacheManager;
        this.arkivressursReferenceMapper = arkivressursReferenceMapper;
    }

    @GetMapping("administrativenhet")
    public Collection<ResourceReference> getAdministrativEnheter() {
        return fintCacheManager
                .getCache("arkiv.noark.administrativenhet", String.class, AdministrativEnhetResource.class)
                .getAllDistinct()
                .stream()
                .map(administrativEnhetResource -> this.mapToResourceReference(administrativEnhetResource, administrativEnhetResource.getNavn()))
                .collect(Collectors.toList());
    }

    @GetMapping("klassifikasjonssystem")
    public Collection<ResourceReference> getKlassifikasjonssystem() {
        return fintCacheManager
                .getCache("arkiv.noark.klassifikasjonssystem", String.class, KlassifikasjonssystemResource.class)
                .getAllDistinct()
                .stream()
                .map(klassifikasjonssystemResource -> this.mapToResourceReference(klassifikasjonssystemResource, klassifikasjonssystemResource.getTittel()))
                .collect(Collectors.toList());
    }

    @GetMapping("klasse/{klassifikasjonssystemLink}")
    public Collection<ResourceReference> getKlasse(@PathVariable String klassifikasjonssystemLink) {
        return fintCacheManager
                .getCache("arkiv.noark.klassifikasjonssystem", String.class, KlassifikasjonssystemResource.class)
                .get(klassifikasjonssystemLink)
                .getKlasse()
                .stream()
                .map(klasse -> new ResourceReference(klasse.getKlasseId(), klasse.getTittel()))
                .collect(Collectors.toList());
    }

    @GetMapping("sakstatus")
    public Collection<ResourceReference> getSakstatus() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.saksstatus", String.class, SaksstatusResource.class)
                .getAllDistinct()
                .stream()
                .map(saksstatusResource -> this.mapToResourceReference(saksstatusResource, saksstatusResource.getNavn()))
                .collect(Collectors.toList());
    }

    @GetMapping("arkivdel")
    public Collection<ResourceReference> getArkivdel() {
        return fintCacheManager
                .getCache("arkiv.noark.arkivdel", String.class, ArkivdelResource.class)
                .getAllDistinct()
                .stream()
                .map(arkivdelResource -> this.mapToResourceReference(arkivdelResource, arkivdelResource.getTittel()))
                .collect(Collectors.toList());
    }

    @GetMapping("skjermingshjemmel")
    public Collection<ResourceReference> getSkjermingshjemmel() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.skjermingshjemmel", String.class, SkjermingshjemmelResource.class)
                .getAllDistinct()
                .stream()
                .map(skjermingshjemmelResource -> this.mapToResourceReference(
                        skjermingshjemmelResource,
                        String.format("%s (%s)", skjermingshjemmelResource.getNavn(), skjermingshjemmelResource.getSystemId())
                ))
                .collect(Collectors.toList());
    }

    @GetMapping("tilgangsrestriksjon")
    public Collection<ResourceReference> getTilgangsrestriksjon() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.tilgangsrestriksjon", String.class, TilgangsrestriksjonResource.class)
                .getAllDistinct()
                .stream()
                .map(tilgangsrestriksjonResource -> this.mapToResourceReference(tilgangsrestriksjonResource, tilgangsrestriksjonResource.getNavn()))
                .collect(Collectors.toList());
    }

    @GetMapping("dokumentstatus")
    public Collection<ResourceReference> getDokumentstatus() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.dokumentstatus", String.class, DokumentStatusResource.class)
                .getAllDistinct()
                .stream()
                .map(dokumentStatusResource -> this.mapToResourceReference(dokumentStatusResource, dokumentStatusResource.getNavn()))
                .collect(Collectors.toList());
    }

    @GetMapping("dokumenttype")
    public Collection<ResourceReference> getDokumenttype() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.dokumenttype", String.class, DokumentTypeResource.class)
                .getAllDistinct()
                .stream()
                .map(dokumentTypeResource -> this.mapToResourceReference(dokumentTypeResource, dokumentTypeResource.getNavn()))
                .collect(Collectors.toList());
    }

    @GetMapping("journalstatus")
    public Collection<ResourceReference> getJournalstatus() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.journalstatus", String.class, JournalStatusResource.class)
                .getAllDistinct()
                .stream()
                .map(journalStatusResource -> this.mapToResourceReference(journalStatusResource, journalStatusResource.getNavn()))
                .collect(Collectors.toList());
    }

    @GetMapping("variantformat")
    public Collection<ResourceReference> getVariantformat() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.variantformat", String.class, VariantformatResource.class)
                .getAllDistinct()
                .stream()
                .map(variantformatResource -> this.mapToResourceReference(variantformatResource, variantformatResource.getNavn()))
                .collect(Collectors.toList());
    }

    @GetMapping("arkivressurs")
    public Collection<ResourceReference> getArkivressurs() {
        return fintCacheManager
                .getCache("arkiv.noark.arkivressurs", String.class, ArkivressursResource.class)
                .getAllDistinct()
                .stream()
                .map(this.arkivressursReferenceMapper::map)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
    }

    private ResourceReference mapToResourceReference(FintLinks resource, String displayName) {
        return new ResourceReference(ResourceLinkUtil.getFirstSelfLink(resource), displayName);
    }

}
