package no.novari.flyt.configuration.security;

@FunctionalInterface
public interface AuditorScope extends AutoCloseable {
    @Override
    void close();
}