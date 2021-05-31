package com.example.aboutspring.util.jackson.objectmapper;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;

class ObjectMapperTest {

    private static class User {

        private final String name;
        private final int age;

        public User(String name, int age) {
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

        // when
        ObjectMapper objectMapper = new ObjectMapper();
        User user = new User("Ryan", 30);
        objectMapper.writeValue(resultFile, user);

        // then
        assertThat(Files.readAllLines(Paths.get(pathname)).get(0))
                .isEqualTo("{\"name\":\"Ryan\",\"age\":30}");
    }
}
