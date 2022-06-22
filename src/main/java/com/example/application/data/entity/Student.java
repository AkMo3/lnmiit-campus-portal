package com.example.application.data.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.util.Objects;

import static javax.persistence.GenerationType.AUTO;

@AllArgsConstructor
@Builder(toBuilder = true)
@Data
@Entity
@NoArgsConstructor
@Table
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = AUTO)
    private Long memberId;

    @NotBlank
    private String rollNumber;

    @NotEmpty
    private String firstName;

    private String lastName;

    @NotEmpty
    @NotNull
    private String phoneNumber;

    @NotBlank
    private String hostel;


    @NotEmpty
    @NotNull
    private String hashedPassword;

    @NotEmpty
    @NotNull
    private String role = "STUDENT";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Student student = (Student) o;
        return memberId != null && Objects.equals(memberId, student.memberId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public String getName() {
        return this.firstName + " " + this.lastName;
    }
}
