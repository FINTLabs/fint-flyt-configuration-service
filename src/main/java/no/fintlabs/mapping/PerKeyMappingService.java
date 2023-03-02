package no.fintlabs.mapping;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Service
public class PerKeyMappingService {

    public <T, R> Map<String, R> mapPerKey(
            Map<String, T> existingMap,
            Function<T, R> mapping
    ) {
        return existingMap
                .keySet()
                .stream()
                .collect(toMap(
                        Function.identity(),
                        key -> mapping.apply(existingMap.get(key))
                ));
    }

}
