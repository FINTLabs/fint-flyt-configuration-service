package no.fintlabs.integration.model.web;

import lombok.Data;
import no.fintlabs.integration.model.ConfigurationElement;

import javax.validation.constraints.NotNull;
import java.util.Collection;

@Data
public class ConfigurationPatch {

    @NotNull
    private String integrationMetadataId; // what if this is updated since last edit on uncompleted configuration?

    @NotNull
    private boolean completed;

    private String comment;

    private Collection<ConfigurationElement> elements;

}
