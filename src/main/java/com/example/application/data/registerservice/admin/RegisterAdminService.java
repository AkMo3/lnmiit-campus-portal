package com.example.application.data.registerservice.admin;

import com.example.application.data.entity.HostelAdmin;
import com.example.application.data.repository.HostelAdminRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RegisterAdminService {

    private final HostelAdminRepository hostelAdminRepository;

    public RegisterAdminService(HostelAdminRepository repository) {
        this.hostelAdminRepository = repository;
    }

    public Optional<HostelAdmin> registerStudent(HostelAdmin person) {
        if (hostelAdminRepository.findHostelAdminByEmailId(person.getEmailId()).isPresent()) {
            return Optional.empty();
        }
        else {
            return Optional.of(hostelAdminRepository.save(person));
        }
    }
}
