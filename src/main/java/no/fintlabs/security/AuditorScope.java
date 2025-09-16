package no.fintlabs.security;

@FunctionalInterface
public interface AuditorScope extends AutoCloseable {
    @Override
    void close();
}