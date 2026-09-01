package com.brakid.runner.types;

import lombok.extern.jackson.Jacksonized;

@Jacksonized
public record Product(long id, String name, float price) {}