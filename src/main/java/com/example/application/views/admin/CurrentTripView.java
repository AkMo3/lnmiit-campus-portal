package com.example.application.views.admin;

import com.example.application.data.entity.HostelAdmin;
import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.HostelAdminRepository;
import com.example.application.data.repository.StudentRepository;
import com.example.application.data.repository.TripDetailRepository;
import com.example.application.service.TripDetailService;
import com.example.application.security.SecurityService;
import com.example.application.views.MainLayout;
import com.example.application.views.user.TripDetailForm;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.annotation.security.PermitAll;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;

@Component
@Scope("prototype")
@Route(value="/admin/request", layout = MainLayout.class)
@PageTitle("Current Trips | TRANSPORT INFORMATION PORTAL")
@PermitAll
public class CurrentTripView extends VerticalLayout {
    private Grid<TripDetail> grid = new Grid<>(TripDetail.class);
    private TextField filterText = new TextField();
    private TripDetailForm form;
    private final TripDetailService service;
    private final SecurityService securityService;
    private HostelAdmin currentAuthenticatedUser;
    private final StudentRepository studentRepository;
    private final TripDetailRepository tripDetailRepository;
    private final HostelAdminRepository hostelAdminRepository;
    private Set<String> hostelAllowed;

    private static final DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern("h.m a");

    public CurrentTripView(TripDetailService service,
                           SecurityService securityService, StudentRepository studentRepository,
                           TripDetailRepository tripDetailRepository,
                           HostelAdminRepository hostelAdminRepository) {
        this.service = service;
        this.tripDetailRepository = tripDetailRepository;
        this.securityService = securityService;
        this.studentRepository = studentRepository;
        this.hostelAdminRepository = hostelAdminRepository;
        addClassName("list-view");
        setSizeFull();
        setCurrentUser();
        configureGrid();
        configureForm();

        FlexLayout content = new FlexLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setFlexShrink(0, form);
        content.addClassNames("content", "gap-m");
        content.setSizeFull();

        add(getPageHeader(), getToolbar(), getContent());
        updateList();
        closeEditor();
        grid.asSingleSelect().addValueChangeListener(event ->
                editTrip(event.getValue()));
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new TripDetailForm(tripDetailRepository, securityService,
                studentRepository, hostelAdminRepository);
        form.setWidth("30%");
        form.addListener(TripDetailForm.ApproveEvent.class, this::approveTrip);
        form.addListener(TripDetailForm.DeclineEvent.class, this::declineTrip);
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(tripDetail -> tripDetail.getStudent().getName()).setHeader("Student Name");
        grid.addColumn(tripDetail -> tripDetail.getStudent().getRollNumber()).setHeader("Student RollNumber");
        grid.addColumn(TripDetail::getToLocation).setHeader("To Location");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfDeparture().toLocalDate().toString())
                .setHeader("Date of Departute");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfDeparture().toLocalTime()
                .format(dateTimeFormatter)).setHeader("Time of Departute");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfArrival().toLocalDate().toString())
                .setHeader("Date of Arrival");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfArrival().toLocalTime()
                .format(dateTimeFormatter)).setHeader("Time of Arrival");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event -> editTrip(event.getValue()));
    }

    public void editTrip(TripDetail tripDetail) {
        if (tripDetail == null) {
            closeEditor();
        } else {
            form.updateForm(tripDetail, true);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void approveTrip(TripDetailForm.ApproveEvent event) {
        service.saveTrip(event.getTripDetail());
        updateList();
        closeEditor();
    }

    private void declineTrip(TripDetailForm.DeclineEvent event) {
        service.deleteTrip(event.getTripDetail());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setButtonPanel();
        form.setVisible(false);
        removeClassName("editing");
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
        grid.setItems(service.findAllByHostelAndRollNumberAndReviewPendingAndCurrentRole
                (hostelAllowed, filterText.getValue(), currentAuthenticatedUser.getRole()));
    }

    private HorizontalLayout getPageHeader() {
        Label label = new Label("LNMIIT Transport Information");
        label.addClassName("page-header-label");
        HorizontalLayout header = new HorizontalLayout(label);
        header.addClassName("page-header");

        Button pastTripRouter = new Button(new RouterLink("View Past Requests",
                PastApprovedTripView.class));
        header.add(pastTripRouter);
        return header;
    }

    private void setCurrentUser() {
        UserDetails user = securityService.getAuthenticatedUser();
        if (user == null) {
            currentAuthenticatedUser = null;
            return;
        }
        String emailId = user.getUsername();
        Optional<HostelAdmin> personOptional = hostelAdminRepository.findHostelAdminByEmailId(emailId);
        currentAuthenticatedUser = personOptional.orElse(null);
        if (currentAuthenticatedUser != null) hostelAllowed = currentAuthenticatedUser.getHostel();
        else hostelAllowed = Set.of();
    }
}
