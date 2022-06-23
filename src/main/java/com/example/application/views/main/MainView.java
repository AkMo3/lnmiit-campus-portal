package com.example.application.views.main;

import com.example.application.utils.AnimationService;
import com.example.application.views.admin.AdminLogin;
import com.example.application.views.gate.SecurityOfficerLogin;
import com.example.application.views.user.LoginView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.vaadin.pekkam.Canvas;

@Route("")
@JavaScript("./src/js/index.js")
@CssImport(value = "styles/index-page.css")
public class MainView extends VerticalLayout {
    private final H1 PortalTitle = new H1("LNMIIT CAMPUS PORTAL");
    private final H2 welcomeTitle = new H2("Welcome");
    private final Button studentLogin = new Button(new RouterLink("Login as Student", LoginView.class));
    private final Button hostelLogin = new Button(new RouterLink("Login as Admin", AdminLogin.class));
    private final Button securityLogin = new Button(new RouterLink("Login as Security Officer",
            SecurityOfficerLogin.class));

    public MainView() {
        addClassName("black-background");
        addClassName("index-page");
        setSizeFull();
        setOpacity();
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        PortalTitle.getStyle().set("color", "white");
        welcomeTitle.getStyle().set("color", "white");
        VerticalLayout onTopCanvas = new VerticalLayout();
        onTopCanvas.addClassName("on-top-layout");
        onTopCanvas.addClassName("black-background");

        FlexLayout layout = new FlexLayout();
        layout.add(studentLogin, hostelLogin, securityLogin);
        layout.setFlexDirection(FlexLayout.FlexDirection.ROW);

        onTopCanvas.add(PortalTitle, welcomeTitle, layout);
        onTopCanvas.setAlignItems(Alignment.CENTER);
        onTopCanvas.setJustifyContentMode(JustifyContentMode.CENTER);
        Canvas canvas = new AnimationService().getStrikeBackground();
        add(canvas, onTopCanvas);
    }

    private void setOpacity() {
        PortalTitle.addClassName("opacity-one");
        welcomeTitle.addClassName("opacity-one");
        securityLogin.addClassName("opacity-one");
        studentLogin.addClassName("opacity-one");
        hostelLogin.addClassName("opacity-one");
    }
}
