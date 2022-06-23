package com.example.application.views.user;

import com.example.application.data.entity.HostelAdmin;
import com.example.application.data.entity.Student;
import com.example.application.data.entity.TripDetail;
import com.example.application.data.repository.HostelAdminRepository;
import com.example.application.data.repository.StudentRepository;
import com.example.application.data.repository.TripDetailRepository;
import com.example.application.security.SecurityService;
import com.example.application.views.MainLayout;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.shared.Registration;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;
import java.util.Optional;

@Route(value = "/user/register")
public class TripDetailForm extends VerticalLayout {

    private final Label studentName = new Label();
    private final Label studentRollNumber = new Label();
    private final TextField toLocation = new TextField("Enter Destination");
    private final DateTimePicker timeOfDeparture = new DateTimePicker("Select date and time of departure");
    private final DateTimePicker timeOfArrival = new DateTimePicker("Select date and time of arrival");
    private final TextField purposeOfVisit = new TextField("Enter the purpose of visit");

    private final Button save = new Button("Save");
    private final Button close = new Button(new RouterLink("Go Back", HomePage.class));
    Binder<TripDetail> binder = new BeanValidationBinder<>(TripDetail.class);

    private TripDetail tripDetail = new TripDetail();
    private Student currentLoggedUser;
    private HostelAdmin currentLoggedAdmin;

    private final Button approve = new Button("Approve");
    private final Button decline = new Button("Decline");

    private final HorizontalLayout buttonPanel;
    private final TripDetailRepository tripDetailRepository;
    private final SecurityService securityService;
    private final StudentRepository studentRepository;
    private final HostelAdminRepository adminRepository;

    public TripDetailForm(TripDetailRepository tripDetailRepository, SecurityService securityService,
                          StudentRepository studentRepository,
                          HostelAdminRepository adminRepository) {
        this.tripDetailRepository = tripDetailRepository;
        this.securityService = securityService;
        this.studentRepository = studentRepository;
        this.adminRepository = adminRepository;

        setCurrentUser();
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.START);

        binder.bindInstanceFields(this);

        timeOfDeparture.setStep(Duration.ofMinutes(15));
        timeOfArrival.setStep(Duration.ofMinutes(15));
        buttonPanel = createButtonsLayout();
        H2 createTripLabel = new H2("Enter new trip details");
        add(createTripLabel, studentName, studentRollNumber, toLocation, timeOfDeparture,
                timeOfArrival, purposeOfVisit, buttonPanel);
    }

    public void updateForm(TripDetail newTripDetail, boolean setWindow) {
        this.tripDetail = newTripDetail;
        binder.readBean(tripDetail);
        this.removeAll();
        if (setWindow) {
            setStyle();
            studentName.setText(tripDetail.getStudent().getName());
            studentRollNumber.setText(tripDetail.getStudent().getRollNumber());
            add(studentName, studentRollNumber, timeOfDeparture, timeOfArrival,
                    toLocation, purposeOfVisit);
            setButtonPanel();
        }
        else {
            studentName.setText(currentLoggedUser.getName());
            studentRollNumber.setText(currentLoggedUser.getRollNumber());
            tripDetail.setApproved(false);
            add(studentName, studentRollNumber, timeOfDeparture, timeOfArrival,
                    toLocation, purposeOfVisit);
        }
    }

    private void setStyle() {
        addClassName("contact-form");
        timeOfDeparture.setReadOnly(true);
        timeOfArrival.setReadOnly(true);
        toLocation.setReadOnly(true);
        purposeOfVisit.setReadOnly(true);
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        save.addClickListener(event -> validateAndSave());
        binder.addStatusChangeListener(e -> save.setEnabled(binder.isValid()));

        return new HorizontalLayout(save, close);
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {
            try {
                tripDetail.setStudent(currentLoggedUser);
                binder.writeBean(tripDetail);
                tripDetailRepository.save(tripDetail);
                add(new H3("Trip Register Succefully"));
            }
            catch (ValidationException e) {

            }
        }
    }

    public void setButtonPanel() {
        approve.addClickListener((e) -> {
            tripDetail.setApproved(true);
            tripDetail.setReviewPending(false);
            setCurrentHostelAdmin();
            tripDetail.setReviewer(currentLoggedAdmin);
            fireEvent(new ApproveEvent(this, tripDetail));
        });
        approve.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        decline.addClickListener((e) -> {
            tripDetail.setApproved(false);
            tripDetail.setReviewPending(false);
            setCurrentHostelAdmin();
            tripDetail.setReviewer(currentLoggedAdmin);
            fireEvent(new DeclineEvent(this, tripDetail));
        });
        decline.addThemeVariants(ButtonVariant.LUMO_ERROR);
        add(new HorizontalLayout(approve, decline));
    }

    // Events
    public static abstract class TripDetailFormEvent extends ComponentEvent<TripDetailForm> {
        private final TripDetail tripDetail;

        protected TripDetailFormEvent(TripDetailForm source, TripDetail tripDetail) {
            super(source, false);
            this.tripDetail = tripDetail;
        }

        public TripDetail getTripDetail() {
            return tripDetail;
        }
    }

    public static class ApproveEvent extends TripDetailFormEvent {
        ApproveEvent(TripDetailForm source, TripDetail tripDetail) {
            super(source, tripDetail);
        }
    }

    public static class DeclineEvent extends TripDetailFormEvent {
        DeclineEvent(TripDetailForm source, TripDetail contact) {
            super(source, contact);
        }
    }

    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener) {
        return getEventBus().addListener(eventType, listener);
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

    private void setCurrentHostelAdmin() {
        UserDetails user = securityService.getAuthenticatedUser();
        if (user == null) {
            currentLoggedUser = null;
            return;
        }
        String rollNumber = user.getUsername();
        Optional<HostelAdmin> personOptional = adminRepository
                .findHostelAdminByEmailId(rollNumber);
        currentLoggedAdmin = personOptional.orElse(null);
    }
}
