package no.fintlabs.integration.cache;

public interface FintCacheEventListener<K, V> {

    void onEvent(FintCacheEvent<K, V> event);

}
