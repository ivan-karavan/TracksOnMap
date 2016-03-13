package ru.hse.view;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.events.MapClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerClickListener;
import com.vaadin.tapio.googlemaps.client.events.MarkerDragListener;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
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

    private Vertex lastClickedVertex = null;
    private Vertex previousClickedVertex = null;

    private CssLayout rootLayout;
    private VerticalLayout fullContent;
    private HorizontalLayout bottomRowOfButtons;
    private HorizontalLayout topRowOfButtons;
    private Button connectVertices;
    private Button disconnectVertices;
    private Button undo;
    private Button redo;
    private Button loadData;
    private Button removeVertex;

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

        map.setCenter(new LatLon(60, 20));
        map.setZoom(10);
        map.setSizeFull();
        fullContent.addComponent(model.getMap());
        fullContent.setExpandRatio(map, 1.0f);


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


        connectVertices = new Button("Connect Vertices", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (!(lastClickedVertex == null || previousClickedVertex == null)) {
                    // TODO: if any vertice alone - connectVerticesCommand
                    // TODO: otherwise - connectTracksCommand
                    Command connectVertices = new ConnectVertexCommand(lastClickedVertex, previousClickedVertex);
                    controller.handle(connectVertices);

                    lastClickedVertex = previousClickedVertex = null;
                }
            }
        });
        bottomRowOfButtons.addComponent(connectVertices);


        disconnectVertices = new Button("Disconnect Vertices", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (!(lastClickedVertex == null || previousClickedVertex == null)) {
                    //Command disconnectVertices = new DisconnectCommand(lastClickedVertex, previousClickedVertex);
                    Command disconnectVertices = new DisconnectCommand(lastClickedVertex);
                    controller.handle(disconnectVertices);

                    lastClickedVertex = previousClickedVertex = null;
                }
            }
        });
        bottomRowOfButtons.addComponent(disconnectVertices);


        removeVertex = new Button("Remove Vertex", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if (lastClickedVertex != null) {
                    Command removeVertex = new RemoveVertexCommand(lastClickedVertex);
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
                Vertex vertex = new Vertex("new", latLon, null);
                Command addVertex = new AddVertexCommand(vertex);
                controller.handle(addVertex);

                previousClickedVertex = lastClickedVertex;
                lastClickedVertex = vertex;
            }
        });
    }
}
