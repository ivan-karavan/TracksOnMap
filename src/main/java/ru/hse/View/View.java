package ru.hse.view;

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
    private TextField windSpeedTextField;

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
//        HorizontalLayout tableAndMap = new HorizontalLayout();
//        tableAndMap.setSizeFull();
//        //GridLayout table = new GridLayout(3, 10);
//        Table table = new Table("Tracks");
//        //table.setVisibleColumns("time", "windspeed");
//        //table.setColumnHeaders("Time", "WindSpeed");
//        fullContent.addComponent(tableAndMap);
//        //tableAndMap.addComponent(table);
//        tableAndMap.addComponent(model.getMap());
//        tableAndMap.setExpandRatio(map, 1.0f);

        fullContent.addComponent(model.getMap());
        fullContent.setExpandRatio(map, 1.0f);
        //

        bottomRowOfButtons = new HorizontalLayout();
        bottomRowOfButtons.setHeight("37px");
        fullContent.addComponent(bottomRowOfButtons);
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
                        lastClickedVertex.setWindSpeed(Double.parseDouble(windSpeedTextField.getValue()));
                    }
                }
            }
        });
        setWindSpeed.setHeight("30px");
        bottomRowOfButtons.addComponent(setWindSpeed);


        //map.getMarkers().stream().forEach(x->{Vertex y = (Vertex)x; y.setVisible(false);});
    }
}
