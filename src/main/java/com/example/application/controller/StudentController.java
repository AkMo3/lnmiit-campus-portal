package com.example.application.controller;

import com.example.application.data.entity.TripDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/student")
@PreAuthorize("hasAuthority('STUDENT')")
public class StudentController {

    @PostMapping("/createTrip")
    public ResponseEntity<TripDetail> createTrip(@RequestBody TripDetail tripDetail) {
        return ResponseEntity.ok().build();
    }
}
