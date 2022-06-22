package com.example.application.views.main;

import com.example.application.views.admin.AdminLogin;
import com.example.application.views.gate.SecurityOfficerLogin;
import com.example.application.views.user.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

import javax.annotation.security.PermitAll;

@Route("")
@PermitAll
public class MainView extends VerticalLayout {
    private final H1 PortalTitle = new H1("LNMIIT CAMPUS PORTAL");
    private final H2 welcomeTitle = new H2("Welcome");
    private final Button studentLogin = new Button(new RouterLink("Login as Student", LoginView.class));
    private final Button hostelLogin = new Button(new RouterLink("Login as Admin", AdminLogin.class));
    private final Button securityLogin = new Button(new RouterLink("Login as Security Officer",
            SecurityOfficerLogin.class));

    public MainView() {
        setSizeFull();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        add(PortalTitle, welcomeTitle, new HorizontalLayout(studentLogin, hostelLogin, securityLogin));
    }
}
