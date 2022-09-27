package no.fintlabs.integration.validation;

import org.springframework.stereotype.Service;

import java.net.URL;
import java.util.Collection;

@Service
public class UrlValidationService {

    public boolean validateValuesAreParsableAsUrls(Collection<String> values) {
        return values.stream().allMatch(this::validateValueIsParsableAsUrl);
    }

    public boolean validateValueIsParsableAsUrl(String value) {
        try {
            new URL(value).toURI();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
