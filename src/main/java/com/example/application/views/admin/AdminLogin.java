package com.example.application.views.admin;

import com.example.application.data.registerservice.admin.RegisterAdminService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("/admin/login")
public class AdminLogin extends VerticalLayout implements BeforeEnterObserver {

    private final LoginForm login = new LoginForm();
    private final AdminRegistration registration;
    private final Button registerButton = new Button("Register");
    private final Button mainView = new Button(new RouterLink("Go Main Page",
            MainView.class));

    public AdminLogin(RegisterAdminService registerService){
        addClassName("login-view");
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        login.setAction("/admin/login");
        registration = new AdminRegistration(registerService);
        registration.setVisible(false);
        registerButton.addClickListener( e -> {
            login.setVisible(!login.isVisible());
            registration.setVisible(!registration.isVisible());
            registerButton.setText(registerButton.getText().equals("Register") ? "Login" : "Register");
        });

        add(new H1("TRANSPORT INFORMATION PORTAL"), new H2("ADMIN LOGIN"),
                login, registration, registerButton, mainView);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        // inform the user about an authentication error
        if(beforeEnterEvent.getLocation()
                .getQueryParameters()
                .getParameters()
                .containsKey("error")) {
            login.setError(true);
        }
    }
}
