package no.fintlabs.integration.validation;

import org.springframework.stereotype.Service;

@Service
public class DynamicStringValidationService {

    private static String regex = "";

    public boolean validateValueIsParsableAsDynamicString(String value) {
        // TODO: 27/09/2022 Validate string  $iem{}
        // TODO: 27/09/2022 Extract iem-ids
        // TODO: 27/09/2022 Find metadata from iem id
        // TODO: 27/09/2022 Validate iem types match element type
        return true;
    }

}
