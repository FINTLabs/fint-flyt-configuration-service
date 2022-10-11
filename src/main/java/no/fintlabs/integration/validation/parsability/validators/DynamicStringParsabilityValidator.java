package no.fintlabs.integration.validation.parsability.validators;

import no.fintlabs.integration.model.FieldConfiguration;
import no.fintlabs.integration.validation.parsability.FieldParsabilityValidator;
import org.springframework.stereotype.Service;

@Service
public class DynamicStringParsabilityValidator implements FieldParsabilityValidator {

    private static String regex = "";

    @Override
    public FieldConfiguration.Type getFieldValueType() {
        return FieldConfiguration.Type.DYNAMIC_STRING;
    }

    @Override
    public boolean isValid(String value) {
        // TODO: 27/09/2022 Validate string  $iem{}
        // TODO: 27/09/2022 Extract iem-ids
        // TODO: 27/09/2022 Find metadata from iem id
        // TODO: 27/09/2022 Validate iem types match element type
        return true;
    }

}
