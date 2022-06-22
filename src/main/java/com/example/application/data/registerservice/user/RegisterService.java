package com.example.application.data.registerservice.user;

import com.example.application.data.entity.Student;
import com.example.application.data.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterService {

    private final StudentRepository studentRepository;

    public RegisterService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Optional<Student> registerStudent(Student person) {
        if (studentRepository.findByRollNumber(person.getRollNumber()).isPresent()) {
            return Optional.empty();
        }
        else {
            return Optional.of(studentRepository.save(person));
        }
    }
}
