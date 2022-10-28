package no.fintlabs.validation.instancefield;

import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

@Service
public class InstanceFieldReferenceKeyExtractionService {

    private static final Pattern ifReferenceExtractionPattern = Pattern.compile("\\$if\\{[^}]+}");

    public Collection<String> extractIfReferenceKeys(String value) {
        return value == null
                ? Collections.emptyList()
                : ifReferenceExtractionPattern.matcher(value)
                .results()
                .map(MatchResult::group)
                .map(ifReference -> ifReference.replace("$if{", "").replace("}", ""))
                .toList();
    }
}
