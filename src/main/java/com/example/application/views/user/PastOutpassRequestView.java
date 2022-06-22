package com.example.application.views.user;

import com.example.application.data.entity.Student;
import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.StudentRepository;
import com.example.application.service.TripDetailService;
import com.example.application.security.SecurityService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Route(value = "/user/view", layout = MainLayout.class)
public class PastOutpassRequestView extends VerticalLayout {

    private Grid<TripDetail> grid = new Grid<>(TripDetail.class);
    private final TripDetailService service;
    private final SecurityService securityService;
    private final StudentRepository studentRepository;
    private Student currentLoggedUser;

    private static final DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern("h.m a");

    public PastOutpassRequestView(TripDetailService service,
                                  SecurityService securityService, StudentRepository studentRepository) {
        this.service = service;
        this.securityService = securityService;
        this.studentRepository = studentRepository;
        addClassName("list-view");
        setSizeFull();
        setCurrentUser();
        configureGrid();

        Button backButton = new Button(new RouterLink("Go Back", HomePage.class));

        add(getPageHeader(), backButton, grid);
        updateList();
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(tripDetail -> tripDetail.getStudent().getName())
                .setHeader("Student Name");
        grid.addColumn(tripDetail -> tripDetail.getStudent().getRollNumber())
                .setHeader("Student RollNumber");
        grid.addColumn(TripDetail::getToLocation).setHeader("To Location");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfDeparture().toLocalDate().toString())
                        .setHeader("Date of Departute");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfDeparture().toLocalTime()
                .format(dateTimeFormatter)).setHeader("Time of Departute");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfArrival().toLocalDate().toString())
                .setHeader("Date of Arrival");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfArrival().toLocalTime()
                .format(dateTimeFormatter)).setHeader("Time of Arrival");
        grid.addColumn(TripDetail::getApprovalStatus).setHeader("Approval Status");
        grid.addColumn(TripDetail::getReviewerName).setHeader("Reviewer");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
    }

    private void updateList() {
        grid.setItems(service.findAllByRollNumber(currentLoggedUser.getRollNumber()));
    }

    private HorizontalLayout getPageHeader() {
        Label label = new Label("Past Trip Details");
        label.addClassName("page-header-label");
        HorizontalLayout header = new HorizontalLayout(label);
        header.addClassName("page-header");
        return header;
    }

    private void setCurrentUser() {
        UserDetails user = securityService.getAuthenticatedUser();
        if (user == null) {
            currentLoggedUser = null;
            return;
        }
        String rollNumber = user.getUsername();
        Optional<Student> personOptional = studentRepository.findByRollNumber(rollNumber);
        currentLoggedUser = personOptional.orElse(null);
    }
}
