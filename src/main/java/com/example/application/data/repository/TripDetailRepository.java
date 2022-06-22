package com.example.application.data.repository;

import com.example.application.data.entity.TripDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface TripDetailRepository extends JpaRepository<TripDetail, UUID> {

    @Query("SELECT c from TripDetail c where c.student.hostel in :hostelName " +
            "and c.reviewPending = true and c.reviewerRole = :currentRole")
    List<TripDetail> findTripDetailByHostelAndReviewerRole(@Param("hostelName") Set<String> hostelName,
                                                           @Param("currentRole") String currentRole);


    @Query("SELECT c from TripDetail c where c.student.hostel in :hostelName and " +
            "lower(c.student.rollNumber) like lower(concat('%', :rollNumber, '%')) " +
            "and c.reviewPending = true and c.reviewerRole = :currentRole")
    List<TripDetail> findTripDetailByHostelAndRollNumberAndReviewPending
            (@Param("hostelName") Set<String> hostelName,
             @Param("rollNumber") String rollNumber,
             @Param("currentRole") String currentRole);

    List<TripDetail> getTripDetailByStudentRollNumberOrderByTimeOfDepartureAsc(String rollNumber);

    @Query("SELECT c FROM TripDetail c WHERE c.leftCampus = false AND " +
            "c.approved = true ORDER BY c.timeOfDeparture")
    List<TripDetail> findAllPendingTripDetails();

    @Query("SELECT c FROM TripDetail c WHERE c.leftCampus = false and " +
            " c.approved = true AND " +
            "lower(c.student.rollNumber) like lower(concat('%', :rollNumber, '%')) " +
            "ORDER BY c.timeOfDeparture")
    List<TripDetail> findAllPendingTripDetailsByRollNumber(@Param("rollNumber") String rollNumber);

    @Query("SELECT c FROM TripDetail c WHERE exists (select c.approved from TripDetail c) " +
            "AND c.reviewer.emailId = :email ORDER BY c.timeOfDeparture ASC ")
    List<TripDetail> findAllReviewedTripDetails(@Param("email") String email);

    @Query("SELECT c FROM TripDetail c WHERE exists (select c.approved from TripDetail c) " +
            "AND c.reviewer.emailId = :email AND " +
            "lower(c.student.rollNumber) like lower(concat('%', :rollNumber, '%')) " +
            "ORDER BY c.timeOfDeparture ASC ")
    List<TripDetail> findAllReviewedTripDetailsByRollNumber(@Param("email") String email,
                                                            @Param("rollNumber") String id);

    List<TripDetail> findTop5ByStudentRollNumberOrderByTimeOfDeparture(String rollNumber);
}
