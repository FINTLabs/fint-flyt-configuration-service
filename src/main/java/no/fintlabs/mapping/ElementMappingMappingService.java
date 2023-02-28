package no.fintlabs.mapping;

public interface ElementMappingMappingService<E, DTO> {

    E toEntity(DTO dto);

    DTO toDto(E entity);

}
