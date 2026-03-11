package no.novari.flyt.configuration.security

fun interface AuditorScope : AutoCloseable {
    override fun close()
}
