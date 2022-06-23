package com.example.application.views.user;

import com.example.application.component.TripDetailComponent;
import com.example.application.data.entity.Student;
import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.TripDetailRepository;
import com.example.application.security.SecurityService;
import com.example.application.service.StudentService;
import com.example.application.utils.AnimationService;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.userdetails.UserDetails;
import org.vaadin.pekkam.Canvas;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@CssImport(value = "styles/home-page.css", themeFor = "home-page")

@Route(value = "/user/home")
@PageTitle("Home")
public class HomePage extends VerticalLayout {

    private final StudentService studentService;
    private final SecurityService securityService;
    private final TripDetailRepository tripDetailRepository;
    private List<TripDetail> top5Trips;

    private Optional<Student> student;

    public HomePage(StudentService studentService, SecurityService securityService,
                    TripDetailRepository tripDetailRepository) {
        this.securityService = securityService;
        this.studentService = studentService;
        this.tripDetailRepository = tripDetailRepository;

        this.addClassName("home-page");

        if (!isStudentSuccessful()) {return;}
        else {
            Canvas canvas = new AnimationService().getStrikeBackground();

            top5Trips = tripDetailRepository
                    .findTop5ByStudentRollNumberOrderByTimeOfDeparture(student.orElseThrow().getRollNumber());


            VerticalLayout greetingComponent = getGreetingComponent();
            if (greetingComponent.hasClassName("no-login-greeting")) {
                add(greetingComponent);
                return;
            }

            HorizontalLayout tripBoards = new HorizontalLayout(getUpcomingTripBoard(), getOngoingTripBoard(),
                    getLastTripBoard());
            tripBoards.setSizeFull();
            tripBoards.setHeight(200, Unit.PIXELS);

            VerticalLayout layout = new VerticalLayout(greetingComponent, getRegisterNewTripComponent(), tripBoards);
            layout.addClassName("on-top-layout");
            layout.getStyle().set("top", "10%");
            add(canvas, layout);
            setSizeFull();
            setSpacing(false);
            this.setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        }
    }

    private VerticalLayout getUpcomingTripBoard() {

        Optional<TripDetail> upcomingTripOptional = getNextUpcomingTrip();
        if (upcomingTripOptional.isEmpty()) return null;
        TripDetail trip = upcomingTripOptional.get();
        Label upcomingTripLabel = new Label("Upcoming Trip");
        upcomingTripLabel.addClassName("new-trip-header");
        VerticalLayout board = getTripBoardWithClass(trip);
        board.addClassName("upcoming-trip-board");
        return new VerticalLayout(upcomingTripLabel, board);
    }

    private VerticalLayout getOngoingTripBoard() {

        Optional<TripDetail> upcomingTripOptional = getCurrentTrip();
        if (upcomingTripOptional.isEmpty()) return null;
        TripDetail trip = upcomingTripOptional.get();
        Label upcomingTripLabel = new Label("Ongoing Trip");
        upcomingTripLabel.addClassName("new-trip-header");
        VerticalLayout board = getTripBoardWithClass(trip);
        board.addClassName("ongoing-trip-board");
        return new VerticalLayout(upcomingTripLabel, board);
    }

    private VerticalLayout getLastTripBoard() {

        Optional<TripDetail> upcomingTripOptional = getLastTrip();
        if (upcomingTripOptional.isEmpty()) return null;
        TripDetail trip = upcomingTripOptional.get();
        Label upcomingTripLabel = new Label("Last Trip");
        upcomingTripLabel.addClassName("new-trip-header");
        VerticalLayout board = getTripBoardWithClass(trip);
        board.addClassName("last-trip-board");
        return new VerticalLayout(upcomingTripLabel, board);
    }

    private VerticalLayout getRegisterNewTripComponent() {

        Label newTripLabel = new Label("Ready for a new trip?");
        newTripLabel.addClassName("new-trip-label");
        RouterLink routerLink = new RouterLink("Create a new pass", TripDetailForm.class);
        routerLink.getStyle().set("color", "white");
        Button registerButton = new Button(routerLink);
        registerButton.addClassName("new-trip-button");
        return new VerticalLayout(newTripLabel, registerButton);
    }

    private VerticalLayout getGreetingComponent() {
        Label greeting = new Label("Good Evening");
        greeting.addClassName("greeting-button");
        UserDetails authenticatedUser = securityService.getAuthenticatedUser();

        if (authenticatedUser == null) {
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);
            VerticalLayout layout = new VerticalLayout(new H1("Please Login First"));
            layout.setClassName("no-login-greeting");
            return layout;
        }

        student = studentService.findStudentByRollNumber(authenticatedUser.getUsername());
        Label studentName = new Label(student.orElseThrow().getName());
        studentName.addClassName("student-name-button");
        return new VerticalLayout(greeting, studentName);
    }

    private Optional<TripDetail> getNextUpcomingTrip() {
        LocalDateTime now = LocalDateTime.now();
        TripDetail upcomingTrip = null;

        for (TripDetail trip: top5Trips) {
            if (trip.getTimeOfDeparture().isAfter(now)) upcomingTrip = trip;
        }

        return Optional.ofNullable(upcomingTrip);
    }

    private Optional<TripDetail> getCurrentTrip() {
        LocalDateTime now = LocalDateTime.now();
        TripDetail upcomingTrip = null;

        for (TripDetail trip: top5Trips) {
            if (trip.getTimeOfDeparture().isBefore(now) &&
            trip.getTimeOfArrival().isAfter(now)) upcomingTrip = trip;
        }

        return Optional.ofNullable(upcomingTrip);
    }

    private Optional<TripDetail> getLastTrip() {
        LocalDateTime now = LocalDateTime.now();
        TripDetail upcomingTrip = null;

        for (TripDetail trip: top5Trips) {
            if (trip.getTimeOfDeparture().isAfter(now)) {
                upcomingTrip = trip;
                break;
            }
        }

        return Optional.ofNullable(upcomingTrip);
    }

    private boolean isStudentSuccessful() {
        UserDetails authenticatedUser = securityService.getAuthenticatedUser();
        if (authenticatedUser == null) {
            add(new H1("Please Login First"));
            setAlignItems(Alignment.CENTER);
            setJustifyContentMode(JustifyContentMode.CENTER);
            return false;
        }
        else {
            student = studentService.findStudentByRollNumber(authenticatedUser.getUsername());
            return true;
        }
    }

    private VerticalLayout getTripBoardWithClass(TripDetail tripDetail) {
        VerticalLayout layout = new TripDetailComponent(tripDetail);
        layout.addClassName("trip-board");
        return layout;
    }
}
