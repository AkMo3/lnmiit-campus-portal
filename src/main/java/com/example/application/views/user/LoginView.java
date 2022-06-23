package com.example.application.views.user;

import com.example.application.data.registerservice.user.RegisterService;
import com.example.application.utils.AnimationService;
import com.example.application.views.main.MainView;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.dom.ThemeList;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import org.vaadin.pekkam.Canvas;

@Route("/user/login")
@PageTitle("Login | LNMIIT CAMPUS PORTAL")
@JsModule("./src/js/index.js")
@CssImport(value = "styles/home-page.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

	private final LoginForm login = new LoginForm();
	private final Registration registration;
	private final Button registerButton = new Button("Register");
	private final Button mainView = new Button(new RouterLink("Go Main Page",
			MainView.class));

	public LoginView(RegisterService registerService){
		addClassName("login-view");
		addClassName("black-background");
		setSizeFull();
		setAlignItems(Alignment.CENTER); 
		setJustifyContentMode(JustifyContentMode.CENTER);

		login.setAction("/user/login");
		setDarkTheme(login);
		registration = new Registration(registerService);
		registration.setVisible(false);
		registerButton.addClickListener( e -> {
			login.setVisible(!login.isVisible());
			registration.setVisible(!registration.isVisible());
			registerButton.setText(registerButton.getText().equals("Register") ? "Login" : "Register");
		});

		Canvas canvas = new AnimationService().getStrikeBackground();
		setSpacing(false);
		VerticalLayout topLayer = new VerticalLayout(login, registration, new HorizontalLayout(registerButton, mainView));
		topLayer.addClassName("on-top-layout");
		topLayer.setJustifyContentMode(JustifyContentMode.CENTER);
		topLayer.setAlignItems(Alignment.CENTER);
		add(canvas, topLayer);
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

	private void setDarkTheme(LoginForm form) {
		ThemeList themeList = form.getElement().getThemeList(); // (1)

		if (themeList.contains(Lumo.DARK)) {
			themeList.remove(Lumo.DARK);
		} else {
			themeList.add(Lumo.DARK);
		}
	}
}