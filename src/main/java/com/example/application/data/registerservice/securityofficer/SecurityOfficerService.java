package com.example.application.data.registerservice.securityofficer;

import com.example.application.data.entity.SecurityOfficer;
import com.example.application.data.repository.SecurityOfficerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SecurityOfficerService {

    private final SecurityOfficerRepository securityOfficerRepository;

    public SecurityOfficerService(SecurityOfficerRepository securityOfficerRepository) {
        this.securityOfficerRepository = securityOfficerRepository;
    }

    public Optional<SecurityOfficer> registerSecurityOfficer(SecurityOfficer person) {
        if (securityOfficerRepository.findByEmailId(person.getEmailId()).isPresent()) {
            return Optional.empty();
        }
        else {
            return Optional.of(securityOfficerRepository.save(person));
        }
    }
}
