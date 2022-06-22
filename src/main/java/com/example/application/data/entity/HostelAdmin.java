package com.example.application.data.entity;

import com.example.application.converter.StringListConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.repository.cdi.Eager;

import javax.persistence.Convert;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static javax.persistence.GenerationType.AUTO;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@NoArgsConstructor
@Table
@ToString
public class HostelAdmin {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long memberId;

    @ElementCollection
    private Set<String> hostel = new HashSet<>();

    @NotBlank
    private String emailId;

    @NotBlank
    private String accountName;

    @NotEmpty
    @NotNull
    private String hashedPassword;

    @NotEmpty
    @NotNull
    private String role;
}
