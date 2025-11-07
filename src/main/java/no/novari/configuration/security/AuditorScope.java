package no.novari.configuration.security;

@FunctionalInterface
public interface AuditorScope extends AutoCloseable {
    @Override
    void close();
}