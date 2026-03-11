package no.novari.flyt.configuration.security

object CurrentAuditor {
    private val currentAuditor = InheritableThreadLocal<String>()

    @JvmStatic
    fun set(value: String?) = currentAuditor.set(value)

    @JvmStatic
    fun get(): String? = currentAuditor.get()

    @JvmStatic
    fun clear() = currentAuditor.remove()
}
