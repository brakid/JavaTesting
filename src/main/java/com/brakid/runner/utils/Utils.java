package com.brakid.runner.utils;

import tools.jackson.databind.json.JsonMapper;
import tools.jackson.datatype.guava.GuavaModule;

public class Utils {
    public final static JsonMapper JSON_MAPPER = 
            JsonMapper.builder()
                    .addModule(new GuavaModule())
                    .build();
}
