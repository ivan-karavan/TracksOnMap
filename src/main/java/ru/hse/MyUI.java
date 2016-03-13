package ru.hse;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import ru.hse.view.View;

/**
 * Start class
 */
@Theme("mytheme")
@Widgetset("ru.hse.MyAppWidgetset")
public class MyUI extends UI {

    private final String apiKey = "";

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        View application = new View();
        application.initComponents();
        this.setContent(application.getRootLayout());
        application.initListeners();
    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
