package com.example.aboutspring.util.jackson.objectmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

class ObjectMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static class User {

        private final String name;
        private final int age;

        @JsonCreator
        public User(@JsonProperty("name") String name, @JsonProperty("age") int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

    @Test
    void writeValue() throws IOException {
        // given
        String pathname = "user.json";
        File resultFile = new File(pathname);
        User user = new User("Ryan", 30);

        // when
        objectMapper.writeValue(resultFile, user);

        // then
        assertThat(Files.readAllLines(Paths.get(pathname)).get(0))
                .isEqualTo("{\"name\":\"Ryan\",\"age\":30}");
    }

    @Test
    void writeValueAsString() throws JsonProcessingException {
        // given
        User user = new User("Ryan", 30);

        // when
        String userAsString = objectMapper.writeValueAsString(user);

        // then
        assertThat(userAsString)
                .isEqualTo("{\"name\":\"Ryan\",\"age\":30}");
    }

    @Test
    void readTree() throws JsonProcessingException {
        // given
        String json = "{\"name\":\"Ryan\",\"age\":30}";

        // when
        JsonNode jsonNode = objectMapper.readTree(json);

        // then
        assertThat(jsonNode.get("name").asText())
                .isEqualTo("Ryan");
        assertThat(jsonNode.get("age").asInt())
                .isEqualTo(30);
    }

    @Test
    void readValue() throws JsonProcessingException {
        String jsonArr = "[{\"name\":\"Ryan\",\"age\":30},{\"name\":\"Jake\",\"age\":20}]";
        List<User> users = objectMapper.readValue(jsonArr, new TypeReference<>() {
        });
        assertThat(users).hasSize(2);
        assertThat(users).extracting("name", "age")
                .contains(tuple("Ryan", 30), tuple("Jake", 20));
    }
}
