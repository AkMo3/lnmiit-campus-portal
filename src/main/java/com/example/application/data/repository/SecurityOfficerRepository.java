package com.example.application.data.repository;

import com.example.application.data.entity.SecurityOfficer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecurityOfficerRepository extends JpaRepository<SecurityOfficer, UUID> {

    Optional<SecurityOfficer> findByEmailId(String emailId);
}
