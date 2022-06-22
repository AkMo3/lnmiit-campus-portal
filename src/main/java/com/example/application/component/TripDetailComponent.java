package com.example.application.component;

import com.example.application.data.entity.TripDetail;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class TripDetailComponent extends VerticalLayout {
    private Label date;
    private Label purpose;
    private Label time;
    private Label approvalStatus;

    public TripDetailComponent(TripDetail tripDetail) {
        this.addClassName("upcoming-trip-board");
        date = new Label("Date: "
                + tripDetail.getTimeOfDeparture().getDayOfWeek()
                + ", " + tripDetail.getTimeOfDeparture().toLocalDate().toString());
        purpose = new Label("Purpose of visit: " + tripDetail.getPurposeOfVisit());
        time = new Label("Time: " + tripDetail.getTimeOfDeparture()
                .toLocalTime().toString() + " to "
                + tripDetail.getTimeOfArrival().toLocalTime().toString());
        approvalStatus = new Label("Approval Status: " + tripDetail.getApprovalStatus());
        add(date, purpose, time, approvalStatus);
    }

}
