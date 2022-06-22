package com.example.application.views.user;

import com.example.application.data.registerservice.user.RegisterService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("/user/login")
@PageTitle("Login | LNMIIT CAMPUS PORTAL")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm login = new LoginForm();
	private final Registration registration;
	private final Button registerButton = new Button("Register");
	private final Button mainView = new Button(new RouterLink("Go Main Page",
			MainView.class));

	public LoginView(RegisterService registerService){
		addClassName("login-view");
		setSizeFull();
		setAlignItems(Alignment.CENTER); 
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("/user/login");
		registration = new Registration(registerService);
		registration.setVisible(false);
		registerButton.addClickListener( e -> {
			login.setVisible(!login.isVisible());
			registration.setVisible(!registration.isVisible());
			registerButton.setText(registerButton.getText().equals("Register") ? "Login" : "Register");
		});

		add(new H1("LNMIIT CAMPUS PORTAL"), new H2("STUDENT LOGIN"),
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