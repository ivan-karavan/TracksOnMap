package ru.hse.view;

import com.vaadin.data.util.HierarchicalContainer;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapInfoWindow;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.*;
import ru.hse.controller.*;
import ru.hse.model.Model;
import ru.hse.model.Styles;
import ru.hse.model.Vertex;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.Random;

/**
 * Created by Ivan on 12.03.2016.
 */
public class View{
    private Model model;
    private Controller controller;
    private GoogleMap map;

    private Vertex lastClickedVertex = null;
    private Vertex previousClickedVertex = null;

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
    private CheckBox hideIcons;
    private TextField windSpeedTextField;
    private TreeTable table;
    private PopupDateField fromDateField;
    private PopupDateField toDateField;

    public View() {
        map = new GoogleMap(null, null, null);
        model = new Model(map);
        controller = new Controller(model);
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

        //
        HorizontalLayout tableAndMap = new HorizontalLayout();
        tableAndMap.setSizeFull();

        table = new TreeTable();
        table.setSizeFull();
        table.setSelectable(true);

        table.addContainerProperty("Name", String .class, 0);
        table.addContainerProperty("ID", Long.class, 0);
        table.setColumnWidth("ID", 22);
        table.addContainerProperty("Lat", Double.class, 0.0);
        table.setColumnWidth("Lat", 27);
        table.addContainerProperty("Lon", Double.class, 0.0);
        table.setColumnWidth("Lon", 32);
        table.addContainerProperty("WindSpeed", Double.class, 0.0);
        table.setColumnWidth("WindSpeed", 70);
        table.addContainerProperty("Date", Date.class, new Date());

        // filling
        //updateTable();

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

    private void doSomethingWithTable() {
//        table.removeAllItems();
//        for (long trackId = 0L; trackId < 5L; trackId++) {
//            Object trackItem = table.addItem(new Object[] {"Track " + trackId, trackId,  1.1, 1.2, 1.3, new Date()}, null);
//            for (long vertexId = 0L; vertexId < 5L; vertexId++) {
//                Object vertexItem = table.addItem(new Object[] {"Vertex " + vertexId, vertexId,  2.3, 2.4, 2.5, new Date()}, null);
//                table.setParent(vertexItem, trackItem);
//                table.setChildrenAllowed(vertexItem, false);
//                table.setCollapsed(vertexItem, false);
//            }
//        }
        Object newItemId = table.addItem();

    }

    private void createSmallCollection() {

    }

    public void initListeners() {
        undo = new Button("undo");
        //undo.setIcon
        undo.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                controller.undo();
                lastClickedVertex = previousClickedVertex = null;
            }
        });
        topRowOfButtons.addComponent(undo);


        redo = new Button("redo");
        //redo.setIcon
        redo.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                controller.redo();
            }
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
        connectVertices = new Button("Connect Vertices", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (!(lastClickedVertex == null || previousClickedVertex == null)) {
                    Command connectVertices;
                    if (lastClickedVertex.getParentTrack().size() == 1 || previousClickedVertex.getParentTrack().size() == 1) {
                        connectVertices = new ConnectVertexToTrackCommand(previousClickedVertex, lastClickedVertex);
                    }
                    else {
                        connectVertices = new ConnectingTracksCommand(previousClickedVertex, lastClickedVertex);
                    }
                    controller.handle(connectVertices);

                    lastClickedVertex = previousClickedVertex = null;
                }
            }
        });
        bottomRowOfButtons.addComponent(connectVertices);


        disconnectVertex = new Button("Disconnect Vertex", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (lastClickedVertex != null) {
                    Command disconnectVertex = new DisconnectFromTrackCommand(lastClickedVertex, lastClickedVertex.getParentTrack());
                    controller.handle(disconnectVertex);

                    lastClickedVertex = previousClickedVertex = null;
                }
            }
        });
        bottomRowOfButtons.addComponent(disconnectVertex);


        removeVertex = new Button("Remove Vertex", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (lastClickedVertex != null) {
                    Command removeVertex = new RemoveCommand(lastClickedVertex, lastClickedVertex.getParentTrack());
                    controller.handle(removeVertex);

                    lastClickedVertex = previousClickedVertex = null;
                }
            }
        });
        bottomRowOfButtons.addComponent(removeVertex);


        loadData = new Button("Load Data", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (fromDateField.getValue() != null || toDateField.getValue() != null) {
                    Command loaddata = new LoadDataCommand(fromDateField.getValue(), toDateField.getValue());
                    controller.handle(loaddata);
                }
            }
        });
        bottomRowOfButtons.addComponent(loadData);


        fromDateField = new PopupDateField();
        bottomRowOfButtons.addComponent(fromDateField);
        toDateField = new PopupDateField();
        bottomRowOfButtons.addComponent(toDateField);


        map.addMarkerClickListener(new MarkerClickListener() {
            @Override
            public void markerClicked(GoogleMapMarker googleMapMarker) {
                previousClickedVertex = lastClickedVertex;
                lastClickedVertex = (Vertex) googleMapMarker;
                // todo open only one infowindow for each marker
                if (lastClickedVertex == previousClickedVertex) {
                    map.openInfoWindow(new GoogleMapInfoWindow("Windspeed = " + lastClickedVertex.getWindSpeed() +
                            " trackId = " + lastClickedVertex.getParentTrack().getId()
                            , googleMapMarker));
                    windSpeedTextField.setValue("" + lastClickedVertex.getWindSpeed());
                }
            }
        });


        map.addMarkerDragListener(new MarkerDragListener() {
            @Override
            public void markerDragged(GoogleMapMarker googleMapMarker, LatLon latLon) {
                Command moveVertex = new MoveVertexCommand((Vertex)googleMapMarker, latLon);
                controller.handle(moveVertex);
            }
        });


        map.addMapClickListener(new MapClickListener() {
            @Override
            public void mapClicked(LatLon latLon) {
                Vertex vertex = Vertex.VertexFactory(latLon);
                Command addVertex = new AddVertexCommand(vertex);
                controller.handle(addVertex);

                previousClickedVertex = lastClickedVertex;
                lastClickedVertex = vertex;
            }
        });


        windSpeedTextField = new TextField();
        windSpeedTextField.setWidth("50px");
        bottomRowOfButtons.addComponent(windSpeedTextField);
        setWindSpeed = new Button("Set windspeed", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                if (lastClickedVertex != null) {
                    if (windSpeedTextField.getValue() != null) {
                        lastClickedVertex.setWindSpeed(Integer.parseInt(windSpeedTextField.getValue()));
                    }
                }
            }
        });
        //setWindSpeed.setHeight("50px");
        bottomRowOfButtons.addComponent(setWindSpeed);

        //map.getMarkers().stream().forEach(x->{Vertex y = (Vertex)x; y.setVisible(false);});
    }
}

