package com.example.application.data.repository;

import com.example.application.data.entity.HostelAdmin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HostelAdminRepository extends JpaRepository<HostelAdmin, Long> {

    Optional<HostelAdmin> findHostelAdminByEmailId(String emailId);
}
