package com.example.application.views.gate;

import com.example.application.data.entity.SecurityOfficer;
import com.example.application.data.registerservice.securityofficer.SecurityOfficerService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.data.binder.Binder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

public class SecurityOfficerRegistration extends VerticalLayout {
    private final Label registerLabel = new Label("Registration");
    private final EmailField emailId = new EmailField("Enter email");
    private final PasswordField passwordField = new PasswordField("Enter Password");
    private final Button register = new Button("Register");

    Binder<SecurityOfficer> binder = new Binder<>(SecurityOfficer.class);
    SecurityOfficer securityOfficer;
    private final SecurityOfficerService securityOfficerService;

    public SecurityOfficerRegistration(SecurityOfficerService registration) {
        this.securityOfficerService = registration;
        setSizeFull();
        increaseFiledWidth();
        this.setAlignItems(FlexComponent.Alignment.CENTER);
        addEventListeners();
        assignBinders();
        add(registerLabel, emailId, getHashedPassword(), register);
    }

    private void assignBinders() {
        binder.forField(emailId).asRequired("Email cannot be empty")
                        .bind(SecurityOfficer::getEmailId, SecurityOfficer::setEmailId);
        binder.forField(passwordField).asRequired("Password cannot be empty")
                .bind(SecurityOfficer::getHashedPassword, SecurityOfficer::setHashedPassword);
    }

    private void addEventListeners() {
        register.addClickListener(event -> validateAndSave());
    }

    private void validateAndSave() {
        if (binder.validate().isOk()) {

            Optional<SecurityOfficer> registerPerson =
                    securityOfficerService.registerSecurityOfficer(constructPerson());
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
        passwordField.setWidth("20%");

        registerLabel.getStyle().set("font-weight", "bold");
    }

    private SecurityOfficer constructPerson() {
        SecurityOfficer newPerson = new SecurityOfficer();
        newPerson.setHashedPassword(new BCryptPasswordEncoder().encode(passwordField.getValue()));
        newPerson.setEmailId(emailId.getValue());
        return newPerson;
    }
}
