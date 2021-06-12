package com.example.aboutspring.util.jackson.objectmapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.tuple;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.junit.jupiter.api.DisplayName;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            User user = (User) o;
            return age == user.age && name.equals(user.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
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
    void readValueForObject() throws JsonProcessingException {
        // given
        String jsonUser = "{\"name\":\"Ryan\",\"age\":30}";

        // when
        User user = objectMapper.readValue(jsonUser, User.class);

        // then
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(new User("Ryan", 30));
    }

    @Test
    void readValueForList() throws JsonProcessingException {
        // given
        String jsonArr = "[{\"name\":\"Ryan\",\"age\":30},{\"name\":\"Jake\",\"age\":20}]";

        // when
        List<User> users = objectMapper.readValue(jsonArr, new TypeReference<>() {
        });

        // then
        assertThat(users).hasSize(2);
        assertThat(users).extracting("name", "age")
                .contains(tuple("Ryan", 30), tuple("Jake", 20));
    }

    @Test
    void readValueForMap() throws JsonProcessingException {
        // given
        String jsonArr = "{\"name\":\"Ryan\",\"age\":30}";

        // when
        Map<String, Object> user = objectMapper.readValue(jsonArr, new TypeReference<>() {
        });

        // then
        assertThat(user).hasSize(2);
        assertThat(user).extractingByKey("name")
                .isEqualTo("Ryan");
        assertThat(user).extractingByKey("age")
                .isEqualTo(30);
    }

    @Test
    void exceptionToReadValueWithConfigurationWithFAIL_ON_UNKNOWN_PROPERTIES() {
        // given
        String json = "{\"name\":\"Ryan\",\"age\":30,\"sex\":\"M\"}";

        // when & then
        assertThatThrownBy(() -> objectMapper.readValue(json, User.class))
                .isInstanceOf(UnrecognizedPropertyException.class);
    }

    @DisplayName("변환하려는 Object에 없는 필드를 JSON으로 변환하려는 경우에 사용하는 설정")
    @Test
    void readValueWithConfigurationWithFAIL_ON_UNKNOWN_PROPERTIES() throws JsonProcessingException {
        // given
        String json = "{\"name\":\"Ryan\",\"age\":30,\"sex\":\"M\"}";

        // when
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User user = objectMapper.readValue(json, User.class);

        // then
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(new User("Ryan", 30));
    }

    @DisplayName("변환하려는 Object에 있는 primitive field에 null이 들어가는 경우 실패하는 옵션을 해제")
    @Test
    void readValueWithConfigurationWithFAIL_ON_NULL_FOR_PRIMITIVES() throws JsonProcessingException {
        // given
        String json = "{\"name\":\"Ryan\",\"age\":null}";

        // when
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        User user = objectMapper.readValue(json, User.class);

        // then
        assertThat(user).isNotNull();
        assertThat(user).isEqualTo(new User("Ryan", 0));
    }
}
