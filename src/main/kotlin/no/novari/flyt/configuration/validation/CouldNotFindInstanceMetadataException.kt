package no.novari.flyt.configuration.validation

class CouldNotFindInstanceMetadataException(
    metadataId: Long,
) : RuntimeException("Could not find instance metadata for metadataId=$metadataId")
