package com.brakid.runner.types;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

import static com.brakid.runner.utils.Utils.JSON_MAPPER;

class UserTest {
    @Test
    void givenUser_whenSerializeAsJson_thenReturnsJson() {
        String json = JSON_MAPPER.writeValueAsString(new User(1l, "test"));
        assertThat(json).isEqualTo("{\"id\":1,\"email_address\":\"test\"}");
    }
}
