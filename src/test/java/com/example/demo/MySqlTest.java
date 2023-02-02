package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = MySqlTest.DataSourceInitializer.class)
@Sql({"/abc.sql"})
class MySqlTest {

    @Container
    private static final MySQLContainer<?> database = new MySQLContainer<>("mysql:8.0.32");

    @Autowired
    private PersonRepository personRepository;

    public static class DataSourceInitializer
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                    applicationContext,
                    "spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect",
                    "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
                    "spring.datasource.url=" + database.getJdbcUrl(),
                    "spring.datasource.username=" + database.getUsername(),
                    "spring.datasource.password=" + database.getPassword()
            );
        }

    }

    @Test
    void shouldSavePerson() {

        Person a = new Person();
        a.setName("othername");

        Person savedPerson = personRepository.save(a);
        assertEquals(new Person(2, "othername"), savedPerson);
    }
}