package no.novari.flyt.configuration.model.metadata

data class InstanceValueMetadata(
    var key: String? = null,
    var type: Type? = null,
) {
    enum class Type {
        STRING,
        FILE,
    }

    companion object {
        @JvmStatic
        fun builder(): Builder = Builder()
    }

    class Builder {
        private var key: String? = null
        private var type: Type? = null

        fun key(key: String?) = apply { this.key = key }

        fun type(type: Type?) = apply { this.type = type }

        fun build(): InstanceValueMetadata = InstanceValueMetadata(key = key, type = type)
    }
}
