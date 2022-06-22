package com.example.application.data.repository;

import com.example.application.data.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {

    public Optional<Student> findByRollNumber(String rollNumber);
}
