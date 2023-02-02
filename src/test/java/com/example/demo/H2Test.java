package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Sql({"/abc.sql"})
class H2Test {

	@Autowired
	private PersonRepository personRepository;

	@Test
	void shouldSavePerson() {

		Person a = new Person();
		a.setName("othername");

		Person savedPerson = personRepository.save(a);
		assertEquals(new Person(2, "othername"), savedPerson);
	}

}
