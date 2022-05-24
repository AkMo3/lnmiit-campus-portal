package com.example.application.views.list;

import com.example.application.data.entity.TripDetail;
import com.example.application.data.service.CrmService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import org.springframework.stereotype.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.context.annotation.Scope;

import javax.annotation.security.PermitAll;

@Component
@Scope("prototype")
@Route(value="", layout = MainLayout.class)
@PageTitle("Contacts | Vaadin CRM")
@PermitAll
public class ListView extends VerticalLayout {
    Grid<TripDetail> grid = new Grid<>(TripDetail.class);
    TextField filterText = new TextField();
    TripDetailForm form;
    CrmService service;

    public ListView(CrmService service) {
        this.service = service;
        addClassName("list-view");
        setSizeFull();
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
                editContact(event.getValue()));
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
        form = new TripDetailForm(service.findAllPlaces());
        form.setWidth("25em");
        form.addListener(TripDetailForm.SaveEvent.class, this::saveContact);
        form.addListener(TripDetailForm.DeleteEvent.class, this::deleteContact);
        form.addListener(TripDetailForm.CloseEvent.class, e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns();
        grid.addColumn(tripDetail -> tripDetail.getToLocation().getName()).setHeader("To Location");
        grid.addColumn(tripDetail -> tripDetail.getTimeOfDeparture().toString()).setHeader("Date And Time of Departure");
        grid.addColumn(TripDetail::getOccupancyLeft).setHeader("Occupancy Left");
        grid.addColumn(tripDetail -> tripDetail.getTripCreator().getName()).setHeader("Trip Admin");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.asSingleSelect().addValueChangeListener(event ->
                editContact(event.getValue()));
    }

    public void editContact(TripDetail tripDetail) {
        if (tripDetail == null) {
            closeEditor();
        } else {
            form.setTripDetails(tripDetail);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void saveContact(TripDetailForm.SaveEvent event) {
        service.saveContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void deleteContact(TripDetailForm.DeleteEvent event) {
        service.deleteContact(event.getContact());
        updateList();
        closeEditor();
    }

    private void closeEditor() {
        form.setTripDetails(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addTripDetails() {
        grid.asSingleSelect().clear();
        editContact(new TripDetail());
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Filter by name...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        Button addContactButton = new Button("Add Trip");
//        addContactButton.addClickListener();

        HorizontalLayout toolbar = new HorizontalLayout(filterText, addContactButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void updateList() {
        grid.setItems(service.findAllContacts(filterText.getValue()));
    }

    private HorizontalLayout getPageHeader() {
        Label label = new Label("LNMIIT Transport Information");
        label.addClassName("page-header-label");
        HorizontalLayout header = new HorizontalLayout(label);
        header.addClassName("page-header");
        return header;
    }
}
