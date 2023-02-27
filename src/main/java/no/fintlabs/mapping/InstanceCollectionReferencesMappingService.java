package no.fintlabs.mapping;

import no.fintlabs.model.configuration.entities.InstanceCollectionReference;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
public class InstanceCollectionReferencesMappingService {

    public List<InstanceCollectionReference> toEntity(List<String> instanceCollectionReferencesOrderedFromDto) {
        return IntStream.range(0, instanceCollectionReferencesOrderedFromDto.size())
                .mapToObj(index -> InstanceCollectionReference
                        .builder()
                        .index(index)
                        .reference(instanceCollectionReferencesOrderedFromDto.get(index))
                        .build()
                )
                .toList();
    }

    public List<String> toDto(Collection<InstanceCollectionReference> instanceCollectionReferencesOrdered) {
        return instanceCollectionReferencesOrdered.stream()
                .sorted(Comparator.comparingInt(InstanceCollectionReference::getIndex))
                .map(InstanceCollectionReference::getReference)
                .toList();
    }

}
