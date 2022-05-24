package com.example.application.data.repository;

import com.example.application.data.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, String> {

    public Optional<Person> findByRollNumber(String rollNumber);
}
