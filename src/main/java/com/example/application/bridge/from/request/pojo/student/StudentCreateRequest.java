package com.example.application.bridge.from.request.pojo.student;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@Data
@Builder
public class StudentCreateRequest {

    @NotNull String firstName;
    String lastName;
    @NotNull String rollNumber;
    @NotNull String hashedPassword;
    @NotNull String hostel;
    @NotNull String phoneNumber;
}
