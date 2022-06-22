package com.example.application.views.gate;

import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.TripDetailRepository;
import com.example.application.service.TripDetailService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.Route;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Route(value = "/security/out", layout = MainLayout.class)
public class GateOutView extends VerticalLayout {
    private Grid<TripDetail> grid = new Grid<>(TripDetail.class);
    private final TripDetailService service;
    private final TripDetailRepository tripDetailRepository;
    private TextField filterText = new TextField();

    private static final DateTimeFormatter dateTimeFormatter
            = DateTimeFormatter.ofPattern("h.m a");

    public GateOutView(TripDetailService service, TripDetailRepository tripDetailRepository) {
        this.service = service;
        this.tripDetailRepository = tripDetailRepository;
        addClassName("list-view");
        setSizeFull();
        configureGrid();

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
        grid.addComponentColumn(tripDetail -> {
            Button button = new Button("Student Out");
            button.addClickListener(e -> {
                tripDetail.setLeftCampus(true);
                tripDetail.setActualOutTime(LocalDateTime.now());
                tripDetailRepository.save(tripDetail);
                updateList();
            });
            button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            return button;
        }).setHeader("Action");
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
        grid.setItems(service.findAllPendingTrips(filterText.getValue()));
    }

    private HorizontalLayout getPageHeader() {
        Label label = new Label("Past Trip Details");
        label.addClassName("page-header-label");
        HorizontalLayout header = new HorizontalLayout(label);
        header.addClassName("page-header");
        return header;
    }
}
