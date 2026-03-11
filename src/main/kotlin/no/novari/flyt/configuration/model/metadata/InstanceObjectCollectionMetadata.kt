package no.novari.flyt.configuration.model.metadata

data class InstanceObjectCollectionMetadata(
    var key: String? = null,
    @Suppress("unused")
    var objectMetadata: InstanceMetadataContent? = null,
) {
    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var key: String? = null
        private var objectMetadata: InstanceMetadataContent? = null

        fun key(key: String?) = apply { this.key = key }

        fun objectMetadata(objectMetadata: InstanceMetadataContent?) = apply { this.objectMetadata = objectMetadata }

        fun build(): InstanceObjectCollectionMetadata =
            InstanceObjectCollectionMetadata(key = key, objectMetadata = objectMetadata)
    }
}
