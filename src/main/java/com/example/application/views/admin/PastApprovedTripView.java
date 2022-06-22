package com.example.application.views.admin;

import com.example.application.data.entity.HostelAdmin;
import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.HostelAdminRepository;
import com.example.application.data.repository.TripDetailRepository;
import com.example.application.service.TripDetailService;
import com.example.application.security.SecurityService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Route(value = "/admin/history", layout = MainLayout.class)
public class PastApprovedTripView extends VerticalLayout {

    private Grid<TripDetail> grid = new Grid<>(TripDetail.class);
    private final TripDetailService service;
    private final TripDetailRepository tripDetailRepository;
    private TextField filterText = new TextField();
    private HostelAdmin currenLoggedAdmin;
    private final SecurityService securityService;
    private final HostelAdminRepository hostelAdminRepository;

    private static final DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern("h.m a");

    public PastApprovedTripView(TripDetailService service,
                                TripDetailRepository tripDetailRepository,
                                SecurityService securityService,
                                HostelAdminRepository hostelAdminRepository) {
        this.service = service;
        this.tripDetailRepository = tripDetailRepository;
        this.securityService = securityService;
        this.hostelAdminRepository = hostelAdminRepository;
        addClassName("list-view");
        setSizeFull();
        configureGrid();
        setCurrentUser();

        add(getPageHeader(), getToolbar(), grid);
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

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by RollNumber...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());
        HorizontalLayout toolbar = new HorizontalLayout(filterText);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(service.findAllApprovedPagesForAdmin(currenLoggedAdmin.getEmailId(),
                filterText.getValue()));
    }

    private HorizontalLayout getPageHeader() {
        Label label = new Label("Past Trip Details");
        label.addClassName("page-header-label");
        HorizontalLayout header = new HorizontalLayout(label);
        header.addClassName("page-header");

        Button currentRequests = new Button(new RouterLink("Current Requests",
                CurrentTripView.class));
        header.add(currentRequests);
        return header;
    }

    private void setCurrentUser() {
        UserDetails user = securityService.getAuthenticatedUser();
        if (user == null) {
            currenLoggedAdmin = null;
            return;
        }
        String emailId = user.getUsername();
        Optional<HostelAdmin> personOptional = hostelAdminRepository.findHostelAdminByEmailId(emailId);
        currenLoggedAdmin = personOptional.orElse(null);
    }
}
