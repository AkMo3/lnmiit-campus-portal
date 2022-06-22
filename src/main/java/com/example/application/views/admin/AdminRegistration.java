package com.example.application.views.admin;

import com.example.application.data.entity.HostelAdmin;
import com.example.application.data.registerservice.admin.RegisterAdminService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

public class AdminRegistration extends VerticalLayout {

    private final Label registerLabel = new Label("Registration");
    private final TextField emailId = new TextField("Enter Admin Email");
    private final MultiSelectListBox<String> hostel = new MultiSelectListBox<>();
    private final ComboBox<String> role = new ComboBox<>("Select Admin Role");
    private final TextField accountName = new TextField("Enter Account Name");
    private final PasswordField passwordField = new PasswordField("Enter Password");
    private final Button register = new Button("Register");
    private final Label hostelSelectLabel = new Label("Select Hostels");

    Binder<HostelAdmin> binder = new Binder<>(HostelAdmin.class);
    HostelAdmin hostelAdmin;
    private final RegisterAdminService registerAdminService;

    public AdminRegistration(RegisterAdminService registration) {
        this.registerAdminService = registration;
        setSizeFull();
        increaseFiledWidth();
        this.setAlignItems(Alignment.CENTER);
        addEventListeners();
        assignBinders();
        hostel.setItems(List.of("BH1", "BH2", "BH3", "BH4", "GH"));
        role.setItems(List.of("WARDEN", "CARETAKER"));
        add(registerLabel, emailId, accountName, hostelSelectLabel,
                hostel, role, getHashedPassword(), register);
    }

    private void assignBinders() {
        binder.forField(emailId).asRequired("First name cannot be empty")
                .bind(HostelAdmin::getEmailId, HostelAdmin::setEmailId);
        binder.forField(accountName).asRequired("Last name cannot be empty")
                .bind(HostelAdmin::getAccountName, HostelAdmin::setAccountName);
        binder.forField(hostel)
                .bind(HostelAdmin::getHostel, HostelAdmin::setHostel);
        binder.forField(role).asRequired("Phone Number cannot be empty")
                .bind(HostelAdmin::getRole, HostelAdmin::setRole);
        binder.forField(passwordField).asRequired("Password cannot be empty")
                .bind(HostelAdmin::getHashedPassword, HostelAdmin::setHashedPassword);
    }

    private void addEventListeners() {
        register.addClickListener(event -> validateAndSave());
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {

            Optional<HostelAdmin> registerPerson = registerAdminService.registerStudent(constructPerson());
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

    private PasswordField getHashedPassword() {
        passwordField.setLabel("Password");
        passwordField.setHelperText("A password must be at least 8 characters. It has to have at least one letter and one digit.");
        passwordField.setPattern("^(?=.*[0-9])(?=.*[a-zA-Z]).{8}.*$");
        passwordField.setErrorMessage("Not a valid password");
        return passwordField;
    }

    private void increaseFiledWidth() {
        register.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        emailId.setWidth("20%");
        role.setWidth("20%");
        hostel.setWidth("20%");
        accountName.setWidth("20%");
        passwordField.setWidth("20%");

        registerLabel.getStyle().set("font-weight", "bold");
    }

    private HostelAdmin constructPerson() {
        HostelAdmin newPerson = new HostelAdmin();
        newPerson.setHashedPassword(new BCryptPasswordEncoder().encode(passwordField.getValue()));
        newPerson.setHostel(hostel.getValue());
        newPerson.setAccountName(accountName.getValue());
        newPerson.setEmailId(emailId.getValue());
        newPerson.setRole(role.getValue());
        return newPerson;
    }
}
