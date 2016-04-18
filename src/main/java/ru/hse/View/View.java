package ru.hse.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.ui.*;
import ru.hse.controller.*;
import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 12.03.2016.
 */
public class View{
    private Model model;
    private Controller controller;
    private GoogleMap map;

    private ClickedVertices clicked;

    private CssLayout rootLayout;
    private VerticalLayout fullContent;
    private HorizontalLayout bottomRowOfButtons;
    private HorizontalLayout topRowOfButtons;
    private Button connectVertices;
    private Button disconnectVertex;
    private Button undo;
    private Button redo;
    private Button loadData;
    private Button removeVertex;
    private Button setWindSpeed;
    private Button saveChanges;
    private Button setTime;
    private CheckBox hideIcons;
    private TextField windSpeedTextField;
    private Table table;
    private PopupDateField fromDateField;
    private PopupDateField toDateField;
    private PopupDateField dateField;
    private BeanItemContainer<Vertex> container;

    public View() {
        map = new GoogleMap(null, null, null);
        model = new Model(map);
        controller = new Controller(model);
        clicked = new ClickedVertices(map);
    }

    public CssLayout getRootLayout() {
        return rootLayout;
    }

    public void initComponents() {
        rootLayout = new CssLayout();
        rootLayout.setSizeFull();

        fullContent = new VerticalLayout();
        fullContent.setSizeFull();
        rootLayout.addComponent(fullContent);

        topRowOfButtons = new HorizontalLayout();
        topRowOfButtons.setHeight("37px");
        fullContent.addComponent(topRowOfButtons);

        map.setCenter(new LatLon(55.75, 37.61));
        map.setZoom(10);
        map.setSizeFull();

        HorizontalLayout tableAndMap = new HorizontalLayout();
        tableAndMap.setSizeFull();


        container = model.getContainer();
        container.removeContainerProperty("animationEnabled");
        container.removeContainerProperty("caption");
        container.removeContainerProperty("draggable");
        container.removeContainerProperty("iconUrl");
        container.removeContainerProperty("optimized");
        container.removeContainerProperty("parentTrack");
        container.removeContainerProperty("positionInTrack");
        container.removeContainerProperty("position");

        table = new Table();
        table.setContainerDataSource(container);
        table.setSizeFull();
//        table.setTableFieldFactory((TableFieldFactory) (container1, itemId, propertyId, uiContext) -> {
//            Field field;
//            return 0;
//        });
//        table.setEditable(true);


        tableAndMap.addComponent(table);
        tableAndMap.addComponent(map);
        tableAndMap.setExpandRatio(table, 1);
        tableAndMap.setExpandRatio(map, 2.5f);

        fullContent.addComponent(tableAndMap);


        bottomRowOfButtons = new HorizontalLayout();
        bottomRowOfButtons.setHeight("37px");
        fullContent.addComponent(bottomRowOfButtons);
        fullContent.setComponentAlignment(bottomRowOfButtons, Alignment.BOTTOM_LEFT);

        fullContent.setExpandRatio(topRowOfButtons, 1);
        fullContent.setExpandRatio(tableAndMap, 15.0f);
        fullContent.setExpandRatio(bottomRowOfButtons, 1);
    }

    public void initListeners() {
        undo = new Button("undo");
        undo.setIcon(FontAwesome.UNDO);
        undo.addClickListener((Button.ClickListener) clickEvent -> {
            controller.undo();
            clicked.toNull();
        });
        topRowOfButtons.addComponent(undo);


        redo = new Button("redo");
        //redo.setIcon(FontAwesome)
        redo.addClickListener((Button.ClickListener) clickEvent -> {
            controller.redo();
            clicked.toNull();
        });
        topRowOfButtons.addComponent(redo);


        hideIcons = new CheckBox("Hide icons");
        hideIcons.setValue(false);
        hideIcons.addValueChangeListener(valueChangeEvent -> model.hideMarkers(hideIcons.getValue()));
        topRowOfButtons.addComponent(hideIcons);
        topRowOfButtons.setComponentAlignment(hideIcons, Alignment.MIDDLE_CENTER);

        /**
         * if any vertice alone - connectVerticesCommand
         * otherwise - connectTracksCommand
         */
        connectVertices = new Button("Connect Vertices", (Button.ClickListener) clickEvent -> {
            if (clicked.getLastClicked() != null && clicked.getPreviousClicked() != null) {
                if (clicked.getLastClicked().getParentTrack() != clicked.getPreviousClicked().getParentTrack()) {
                    Command connectVertices;
                    if (clicked.anyAlone()) {
                        connectVertices = new ConnectVertexToTrackCommand(clicked.getPreviousClicked(),
                                clicked.getLastClicked());
                    } else {
                        connectVertices = new ConnectingTracksCommand(clicked.getPreviousClicked(),
                                clicked.getLastClicked());
                    }
                    controller.handle(connectVertices);

                    clicked.toNull();
                }
            }
        });
        bottomRowOfButtons.addComponent(connectVertices);


        disconnectVertex = new Button("Disconnect Vertex", (Button.ClickListener) clickEvent -> {
            if (clicked.notNull()) {
                Command disconnectVertex1 = new DisconnectFromTrackCommand(clicked.getLastClicked(),
                        clicked.getLastClicked().getParentTrack());
                controller.handle(disconnectVertex1);

                clicked.toNull();
            }
        });
        bottomRowOfButtons.addComponent(disconnectVertex);


        removeVertex = new Button("Remove Vertex", (Button.ClickListener) clickEvent -> {
            if (clicked.notNull()) {
                Command removeVertex1 = new RemoveCommand(clicked.getLastClicked(),
                        clicked.getLastClicked().getParentTrack());
                controller.handle(removeVertex1);

                clicked.toNull();
            }
        });
        bottomRowOfButtons.addComponent(removeVertex);


        fromDateField = new PopupDateField();
        fromDateField.setWidth("120px");
        bottomRowOfButtons.addComponent(fromDateField);
        toDateField = new PopupDateField();
        toDateField.setWidth("120px");
        bottomRowOfButtons.addComponent(toDateField);


        loadData = new Button("Load Data", (Button.ClickListener) event -> {
            if (fromDateField.getValue() != null || toDateField.getValue() != null) {
                Command loaddata = new LoadDataCommand(fromDateField.getValue(), toDateField.getValue());
                controller.handle(loaddata);

                clicked.toNull();
            }
        });
        bottomRowOfButtons.addComponent(loadData);


        map.addMarkerClickListener((MarkerClickListener) googleMapMarker -> {
            clicked.setLastClicked(googleMapMarker);
            windSpeedTextField.setValue("" + clicked.getLastClicked().getWind());
            table.setCurrentPageFirstItemId(googleMapMarker);

            if (clicked.getLastClicked().getTime() != null) {
                dateField.setValue(clicked.getLastClicked().getTime());
            }
            else {
                dateField.clear();
            }
            // todo open only one infowindow for each marker
            if (clicked.equal()) {
                map.openInfoWindow(new GoogleMapInfoWindow(
                        "<table>" +
                                "  <tr>" +
                                "    <td>LatLon = " + clicked.getLastClicked().getLat() + " " +
                                clicked.getLastClicked().getLon() + "</td>" +
                                "  </tr>" +
                                "  <tr>" +
                                "    <td>Windspeed = " + clicked.getLastClicked().getWind() + "</td>" +
                                "  </tr>" +
                                "  <tr>" +
                                "    <td>Date = " +
                                (clicked.getLastClicked().getTime() == null ? "unknown" :
                                        clicked.getLastClicked().getTime()) + "</td>" +
                                "  </tr>" +
                                "</table>"
                        , googleMapMarker));
                clicked.toNull();
            }
        });


        map.addMarkerDragListener((MarkerDragListener) (googleMapMarker, latLon) -> {
            Command moveVertex = new MoveVertexCommand((Vertex)googleMapMarker, latLon);
            controller.handle(moveVertex);
        });


        map.addMapClickListener((MapClickListener) latLon -> {
            Vertex vertex = Vertex.VertexFactory(latLon);
            Command addVertex = new AddVertexCommand(vertex);
            controller.handle(addVertex);

            clicked.setLastClicked(vertex);
        });


        windSpeedTextField = new TextField();
        windSpeedTextField.setWidth("50px");
        bottomRowOfButtons.addComponent(windSpeedTextField);
        setWindSpeed = new Button("Set windspeed", (Button.ClickListener) event -> {
            if (clicked.notNull()) {
                if (windSpeedTextField.getValue() != null) {
                    try {
                        int speed = Integer.parseInt(windSpeedTextField.getValue());
                        clicked.getLastClicked().setWind(speed);
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        bottomRowOfButtons.addComponent(setWindSpeed);


        dateField = new PopupDateField();
        dateField.setWidth("120px");
        bottomRowOfButtons.addComponent(dateField);

        setTime = new Button("Set Date", event -> {
            if (clicked.notNull()) {
                clicked.getLastClicked().setTime(dateField.getValue());
            }
        });
        bottomRowOfButtons.addComponent(setTime);


        saveChanges = new Button("Save session", (Button.ClickListener) clickEvent -> {

        });
        bottomRowOfButtons.addComponent(saveChanges);


        table.addItemClickListener((ItemClickEvent.ItemClickListener) event -> {
            Vertex clicked = (Vertex)event.getItemId();
            map.setCenter(clicked.getPosition());
        });
    }
}

