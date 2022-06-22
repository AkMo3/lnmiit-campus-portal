package com.example.application.service;

import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.TripDetailRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Set;

@Service
public class TripDetailService {

    private final TripDetailRepository tripDetailRepository;

    public TripDetailService(TripDetailRepository tripDetailRepository) {
        this.tripDetailRepository = tripDetailRepository;
    }

    @Transactional
    public List<TripDetail> findAllByHostelAndRollNumberAndReviewPendingAndCurrentRole
            (Set<String> hostel, String rollNumber, String currentRole) {
        if (rollNumber == null || rollNumber.isEmpty()) {
            return tripDetailRepository.findTripDetailByHostelAndReviewerRole(hostel, currentRole);
        }
        else {
            return tripDetailRepository
                    .findTripDetailByHostelAndRollNumberAndReviewPending(hostel, rollNumber, currentRole);
        }
    }

    public void deleteTrip(TripDetail tripDetail) {
        tripDetailRepository.delete(tripDetail);
    }

    public void saveTrip(TripDetail tripDetail) {
        if (tripDetail == null) {
            System.err.println("Contact is null. " +
                    "Are you sure you have connected your form to the application?");
            return;
        }
        tripDetailRepository.save(tripDetail);
    }

    public List<TripDetail> findAllByRollNumber(String rollNumber) {
        return tripDetailRepository
                .getTripDetailByStudentRollNumberOrderByTimeOfDepartureAsc(rollNumber);
    }

    public List<TripDetail> findAllPendingTrips(String rollNumber) {
        if (rollNumber == null || rollNumber.isEmpty()) {
            return tripDetailRepository.findAllPendingTripDetails();
        }
        else {
            return tripDetailRepository.findAllPendingTripDetailsByRollNumber(rollNumber);
        }
    }

    public List<TripDetail> findAllApprovedPagesForAdmin(String adminEmail, String rollNumber) {
        if (adminEmail == null || adminEmail.isEmpty()) {
            return tripDetailRepository.findAllReviewedTripDetails(adminEmail);
        }
        else return tripDetailRepository.findAllReviewedTripDetailsByRollNumber(adminEmail, rollNumber);
    }
}
