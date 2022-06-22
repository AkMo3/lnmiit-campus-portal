package com.example.application.views.user;

import com.example.application.data.entity.Student;
import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.TripDetailRepository;
import com.example.application.security.SecurityService;
import com.example.application.service.StudentService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.board.Board;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@CssImport("META-INF/resources/frontend/styles/homepage.css")

@Route(value = "/user/home", layout = MainLayout.class)
@PageTitle("Home")
public class HomePage extends VerticalLayout {

    private final StudentService studentService;
    private final SecurityService securityService;
    private final TripDetailRepository tripDetailRepository;

    private Optional<Student> student;

    public HomePage(StudentService studentService, SecurityService securityService,
                    TripDetailRepository tripDetailRepository) {
        this.securityService = securityService;
        this.studentService = studentService;
        this.tripDetailRepository = tripDetailRepository;

        this.addClassName("home-page");

        H2 greeting = new H2("Good Evening");
        greeting.addClassName("greeting-button");
        UserDetails authenticatedUser = securityService.getAuthenticatedUser();

        if (authenticatedUser == null) {
            add(new H1("Please Login First"));
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);
            return;
        }
        student = studentService.findStudentByRollNumber(authenticatedUser.getUsername());
        H3 studentName = new H3(student.orElseThrow().getName());
        studentName.addClassName("student-name-button");

        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(new Button(new RouterLink("Register Request", TripDetailForm.class)));
        horizontalLayout.add(new Button(new RouterLink("Past Trip Details Request",
                PastOutpassRequestView.class)));

        add(getUpcomingTripBoard(), horizontalLayout);
        setSizeFull();
        this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
    }

    private Board getUpcomingTripBoard() {
        Optional<TripDetail> upcomingTripOptional = tripDetailRepository
                .findTopByStudentRollNumber(student.orElseThrow().getRollNumber());
        if (upcomingTripOptional.isEmpty()) return null;
        TripDetail trip = upcomingTripOptional.get();
        Board upcomingTripBoard = new Board();
        upcomingTripBoard.addClassName("upcoming-trip-board");
        upcomingTripBoard.addRow(new Label("Date: "
                + trip.getTimeOfDeparture().getDayOfWeek()
                + ", " + trip.getTimeOfDeparture().toLocalDate().toString()));
        upcomingTripBoard.addRow(new Label("Purpose of visit: " + trip.getPurposeOfVisit()));
        upcomingTripBoard.addRow(new Label("Time: " + trip.getTimeOfDeparture()
                .toLocalTime().toString() + " to "
                + trip.getTimeOfArrival().toLocalTime().toString()));
        upcomingTripBoard.addRow(new Label("Approval Status: " + trip.getApprovalStatus()));
        return upcomingTripBoard;
    }
}
