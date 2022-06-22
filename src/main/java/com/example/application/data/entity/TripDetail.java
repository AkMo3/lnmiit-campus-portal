package com.example.application.data.entity;

import lombok.Data;

import javax.annotation.Nullable;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Data
public class TripDetail extends AbstractEntity {

    @ManyToOne
    @NotNull
    private Student student;

    @NotNull
    private LocalDateTime timeOfDeparture;

    @NotNull
    private LocalDateTime timeOfArrival;

    @NotBlank
    private String toLocation;

    private boolean reviewPending = true;

    @Nullable
    private Boolean approved = null;

    private String purposeOfVisit;

    private String reviewerRole;

    private LocalDateTime actualOutTime;

    private LocalDateTime actualInTime;

    @NotNull
    private boolean leftCampus = false;

    @NotNull
    private boolean arrivedCampusBack = false;

    @ManyToOne
    private HostelAdmin reviewer;

    @PrePersist
    @PreUpdate
    private void initReviewer() {

        if (timeOfDeparture.toLocalDate().equals(timeOfArrival.toLocalDate())) {
            reviewerRole = "CARETAKER";
        }
        else reviewerRole = "WARDEN";
    }

    public Optional<HostelAdmin> getOptionalReviewer() {
        return Optional.ofNullable(reviewer);
    }

    public String getReviewerName() {
        return (getOptionalReviewer().isPresent() ? getOptionalReviewer().get().getAccountName() : "");
    }

    public String getApprovalStatus() {
        return approved == null ? "Pending" : approved ? "Approved" : "Declined";
    }
}
