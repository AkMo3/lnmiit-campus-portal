package com.example.application.data.service.user;

import com.example.application.data.entity.Person;
import com.example.application.data.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterService {

    private final PersonRepository personRepository;

    public RegisterService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public Optional<Person> registerUser(Person person) {
        if (personRepository.findByRollNumber(person.getRollNumber()).isPresent()) {
            return Optional.empty();
        }
        else {
            return Optional.of(personRepository.save(person));
        }
    }
}
