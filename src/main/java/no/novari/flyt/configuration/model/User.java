package no.novari.flyt.configuration.model;

import jakarta.annotation.Nonnull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.extern.jackson.Jacksonized;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@Jacksonized
public class User {

    @Nonnull
    private UUID objectIdentifier;

    private String email;

    private String name;

    @Nonnull
    @Builder.Default
    private List<Long> sourceApplicationIds = new ArrayList<>();
}
