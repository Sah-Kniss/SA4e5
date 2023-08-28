package com.example.sa4.controller;

import com.example.sa4.model.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepository extends CrudRepository<Person, Integer > {
}