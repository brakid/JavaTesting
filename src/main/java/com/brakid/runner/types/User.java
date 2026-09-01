package com.brakid.runner.types;

import com.fasterxml.jackson.annotation.JsonProperty;

public record User (
    Long id,
    @JsonProperty("email_address") String emailAddress
) {}