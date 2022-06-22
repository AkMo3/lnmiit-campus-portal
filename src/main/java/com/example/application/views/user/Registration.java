package com.example.application.views.user;

import com.example.application.data.entity.Student;
import com.example.application.data.registerservice.user.RegisterService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

public class Registration extends VerticalLayout {
    private final Label RegisterLabel = new Label("Register");
    private final TextField firstName = new TextField("Enter First Name");
    private final TextField lastName = new TextField("Enter Last Name");
    private final TextField rollNumber = new TextField("Enter Roll Number");
    private final TextField phoneNumber = new TextField("Enter Phone Number");
    private final ComboBox<String> hostel = new ComboBox<String>("Enter Hostel");
    private final PasswordField hashedPassword = new PasswordField("Enter Password");

    private final Button register = new Button("Register");

    private final RegisterService registerService;

    Binder<Student> binder = new Binder<>(Student.class);

    public Registration(RegisterService registerService) {
        this.registerService = registerService;
        hostel.setItems(List.of("BH1", "BH2", "BH3", "BH4", "GH"));
        setSizeFull();
        increaseFiledWidth();
        this.setAlignItems(Alignment.CENTER);
        addEventListeners();
        assignBinders();
        add(RegisterLabel, firstName, lastName, rollNumber, phoneNumber, hostel, getHashedPassword(), getButtonPanel());
    }

    private void assignBinders() {
        binder.forField(firstName).asRequired("First name cannot be empty")
                .bind(Student::getFirstName, Student::setFirstName);
        binder.forField(lastName).asRequired("Last name cannot be empty")
                .bind(Student::getLastName, Student::setLastName);
        binder.forField(rollNumber).asRequired("Roll Number cannot be empty")
                .bind(Student::getRollNumber, Student::setRollNumber);
        binder.forField(phoneNumber).asRequired("Phone Number cannot be empty")
                .bind(Student::getPhoneNumber, Student::setPhoneNumber);
        binder.forField(hashedPassword).asRequired("Password cannot be empty")
                .bind(Student::getHashedPassword, Student::setHashedPassword);
        binder.forField(hostel).asRequired("Hostel is required")
                .bind(Student::getHostel, Student::setHostel);
    }

    private HorizontalLayout getButtonPanel() {
        HorizontalLayout buttonPanel = new HorizontalLayout(register);
        buttonPanel.setWidth("20%");
        return buttonPanel;
    }

    private PasswordField getHashedPassword() {
        hashedPassword.setLabel("Password");
        hashedPassword.setHelperText("A password must be at least 8 characters. It has to have at least one letter and one digit.");
        hashedPassword.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        hashedPassword.setErrorMessage("Not a valid password");
        return hashedPassword;
    }

    private void increaseFiledWidth() {
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        hostel.setWidth("20%");
        firstName.setWidth("20%");
        lastName.setWidth("20%");
        rollNumber.setWidth("20%");
        phoneNumber.setWidth("20%");
        hashedPassword.setWidth("20%");

        RegisterLabel.getStyle().set("font-weight", "bold");
    }

    private void addEventListeners() {
        register.addClickListener(event -> validateAndSave());
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {

            Optional<Student> registerPerson = registerService.registerStudent(constructPerson());
            this.getChildren()
                    .filter(e -> e.getId().isPresent() && e.getId().get().equals("register-status"))
                    .forEach(this::remove);
            if (registerPerson.isEmpty()) {
                Label errorLabel = new Label("User already exists");
                errorLabel.setId("register-status");
                add(errorLabel);
            } else {
                Label successLabel = new Label("User added successfully");
                successLabel.setId("register-status");
                add(successLabel);
            }
        }
    }

    private Student constructPerson() {
        Student newPerson = new Student();
        newPerson.setHashedPassword(new BCryptPasswordEncoder().encode(hashedPassword.getValue()));
        newPerson.setFirstName(firstName.getValue());
        newPerson.setLastName(lastName.getValue());
        newPerson.setPhoneNumber(phoneNumber.getValue());
        newPerson.setRollNumber(rollNumber.getValue());
        newPerson.setHostel(hostel.getValue());
        return newPerson;
    }
}
