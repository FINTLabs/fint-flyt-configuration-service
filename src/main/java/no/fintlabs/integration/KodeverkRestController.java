package no.fintlabs.integration;

import no.fint.model.resource.FintLinks;
import no.fint.model.resource.arkiv.kodeverk.*;
import no.fint.model.resource.arkiv.noark.AdministrativEnhetResource;
import no.fint.model.resource.arkiv.noark.ArkivdelResource;
import no.fint.model.resource.arkiv.noark.KlassifikasjonssystemResource;
import no.fintlabs.integration.model.ResourceReference;
import no.fintlabs.kafka.consumer.cache.FintCacheManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/kodeverk/")
public class KodeverkRestController {
    private final FintCacheManager fintCacheManager;

    public KodeverkRestController(FintCacheManager fintCacheManager) {
        this.fintCacheManager = fintCacheManager;
    }

    @GetMapping("administrativenhet")
    public Collection<ResourceReference> getAdministrativEnheter() {
        return fintCacheManager
                .getCache("arkiv.noark.administrativenhet", String.class, AdministrativEnhetResource.class)
                .getAllDistinct()
                .stream()
                .map(administrativEnhetResource -> this.mapToResourceReference(administrativEnhetResource, administrativEnhetResource::getNavn))
                .collect(Collectors.toList());
    }

    @GetMapping("klassifikasjonssystem")
    public Collection<ResourceReference> getKlassifikasjonssystem() {
        return fintCacheManager
                .getCache("arkiv.noark.klassifikasjonssystem", String.class, KlassifikasjonssystemResource.class)
                .getAllDistinct()
                .stream()
                .map(klassifikasjonssystemResource -> this.mapToResourceReference(klassifikasjonssystemResource, klassifikasjonssystemResource::getTittel))
                .collect(Collectors.toList());
    }

//    @GetMapping("rolle")
//    public Collection<ResourceReference> getRolle() {
//        return fintCacheManager
//                .getCache("arkiv.noark.rolle", String.class, RolleResource.class)
//                .getAllDistinct()()
//                .stream()
//                .map(rolleResource -> this.mapToResourceReference(rolleResource, rolleResource::getNavn))
//                .collect(Collectors.toList());
//    }

    @GetMapping("sakstatus")
    public Collection<ResourceReference> getSakstatus() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.saksstatus", String.class, SaksstatusResource.class)
                .getAllDistinct()
                .stream()
                .map(saksstatusResource -> this.mapToResourceReference(saksstatusResource, saksstatusResource::getNavn))
                .collect(Collectors.toList());
    }

    @GetMapping("arkivdel")
    public Collection<ResourceReference> getArkivdel() {
        return fintCacheManager
                .getCache("arkiv.noark.arkivdel", String.class, ArkivdelResource.class)
                .getAllDistinct()
                .stream()
                .map(arkivdelResource -> this.mapToResourceReference(arkivdelResource, arkivdelResource::getTittel))
                .collect(Collectors.toList());
    }

    @GetMapping("skjermingshjemmel")
    public Collection<ResourceReference> getSkjermingshjemmel() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.skjermingshjemmel", String.class, SkjermingshjemmelResource.class)
                .getAllDistinct()
                .stream()
                .map(skjermingshjemmelResource -> this.mapToResourceReference(skjermingshjemmelResource, skjermingshjemmelResource::getNavn))
                .collect(Collectors.toList());
    }

    @GetMapping("tilgangsrestriksjon")
    public Collection<ResourceReference> getTilgangsrestriksjon() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.tilgangsrestriksjon", String.class, TilgangsrestriksjonResource.class)
                .getAllDistinct()
                .stream()
                .map(tilgangsrestriksjonResource -> this.mapToResourceReference(tilgangsrestriksjonResource, tilgangsrestriksjonResource::getNavn))
                .collect(Collectors.toList());
    }

    @GetMapping("klassifikasjonstype")
    public Collection<ResourceReference> getKlassifikasjonstype() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.klassifikasjonstype", String.class, KlassifikasjonstypeResource.class)
                .getAllDistinct()
                .stream()
                .map(klassifikasjonstypeResource -> this.mapToResourceReference(klassifikasjonstypeResource, klassifikasjonstypeResource::getNavn))
                .collect(Collectors.toList());
    }

    @GetMapping("dokumentstatus")
    public Collection<ResourceReference> getDokumentstatus() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.dokumentstatus", String.class, DokumentStatusResource.class)
                .getAllDistinct()
                .stream()
                .map(dokumentStatusResource -> this.mapToResourceReference(dokumentStatusResource, dokumentStatusResource::getNavn))
                .collect(Collectors.toList());
    }

    @GetMapping("dokumenttype")
    public Collection<ResourceReference> getDokumenttype() {
        return fintCacheManager
                .getCache("arkiv.kodeverk.dokumenttype", String.class, DokumentTypeResource.class)
                .getAllDistinct()
                .stream()
                .map(dokumentTypeResource -> this.mapToResourceReference(dokumentTypeResource, dokumentTypeResource::getNavn))
                .collect(Collectors.toList());
    }

    private ResourceReference mapToResourceReference(FintLinks resource, Supplier<String> getDisplayName) {
        return new ResourceReference(
                resource.getSelfLinks().stream().findFirst().orElseThrow().getHref(),
                getDisplayName.get()
        );
    }
}
