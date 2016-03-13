package ru.hse;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;
import ru.hse.View.View;

/**
 *
 */
@Theme("mytheme")
@Widgetset("ru.hse.MyAppWidgetset")
public class MyUI extends UI {

    //private GoogleMap googleMap;
    //private GoogleMapMarker marker = new GoogleMapMarker(
    //        "marker", new LatLon(60, 22), true, null);
    //"https://cdn2.iconfinder.com/data/icons/flat-style-svg-icons-part-1/512/location_marker_pin-128.png");
    //private GoogleMapInfoWindow infoWindow = new GoogleMapInfoWindow("something", marker);
    private final String apiKey = "";

//    private Vertice[] markers = new Vertice[3];
//
//    /**
//     * should be null after each ended action
//     */
//    private Vertice lastClickedMarker;
//    private Vertice lastAddedMarker;
//
//    /**
//     * OR id of vertice, id of polyline FROM this vertice // todo
//     */
//    private Map<Vertice, GoogleMapPolyline> allTracks = new HashMap<>();
//
//    private HashMap<Pair, GoogleMapPolyline> tracks = new HashMap<>();
//
//    private CssLayout rootLayout;
//    private VerticalLayout fullContent;
//    private HorizontalLayout rowOfButtons;
//    private Button connectVertices;
//    private Button disconnectVertices;
//    private Button removeVertex;
//    private Button undo;
//    private Button redo;

//    private void initLayoutsAndComponents() {
//        rootLayout = new CssLayout();
//        rootLayout.setSizeFull();
//        this.setContent(rootLayout);
//
//        fullContent = new VerticalLayout();
//        fullContent.setSizeFull();
//        rootLayout.addComponent(fullContent);
//    }

    @Override
    protected void init(VaadinRequest vaadinRequest) {
//
//        CssLayout rootLayout = new CssLayout();
//        rootLayout.setSizeFull();
//        setContent(rootLayout);
//
//        VerticalLayout mapContent = new VerticalLayout();
//        mapContent.setSizeFull();
//        rootLayout.addComponent(mapContent);
//
//        Panel console = new Panel();
//        console.setHeight("50px");
//        CssLayout consoleLayout = new CssLayout();
//        console.setContent(consoleLayout);
//        mapContent.addComponent(console);
//
//        googleMap = new GoogleMap(null, null, null);
//        googleMap.setCenter(new LatLon(62, 22));
//        googleMap.setZoom(10);
//        googleMap.setSizeFull();
//
//        //marker.setAnimationEnabled(false);
//        //googleMap.addMarker(marker);
//
//        mapContent.addComponent(googleMap);
//        mapContent.setExpandRatio(googleMap, 1.0f);
//
//        HorizontalLayout buttonLayout = new HorizontalLayout();
//        buttonLayout.setHeight("40px");
//        mapContent.addComponent(buttonLayout);
//
//        // TODO start
//        markers[0] = new Vertice("marker1", new LatLon(61, 22), null, markers[1]);
//        markers[1] = new Vertice("marker2", new LatLon(62, 22), markers[0], markers[2]);
//        markers[0].setSecondNeighbour(markers[1]);
//        markers[2] = new Vertice("marker3", new LatLon(63, 23), markers[1], null);
//        markers[1].setSecondNeighbour(markers[2]);
//        drawLine(markers[0], markers[1]);
//        drawLine(markers[1], markers[2]);
//
//        googleMap.addMarker(markers[0]);
//        googleMap.addMarker(markers[1]);
//        googleMap.addMarker(markers[2]);
//
//
//        googleMap.addMarkerDragListener(new MarkerDragListener() {
//            @Override
//            public void markerDragged(GoogleMapMarker draggedMarker, LatLon oldPosition) {
//                consoleLayout.addComponent(new Label("Marker is moved"));
//                try {
//                    Vertice movedMarker = (Vertice) draggedMarker;
//                    if (movedMarker.getFirstNeighbour() != null) {
//                        // todo hashmap(pair(old vert,vert neigh)-line), delete fields line
////                        Pair pair = new Pair(movedMarker, movedMarker.getFirstNeighbour());
////                        googleMap.removePolyline(tracks.get(pair));
////                        tracks.remove(pair);
//
//
//                        //
//                        googleMap.removePolyline(movedMarker.getLineToFirstNeighbour());
//                        ArrayList<LatLon> pair = new ArrayList<LatLon>(2);
//                        pair.add(movedMarker.getPosition());
//                        pair.add(movedMarker.getFirstNeighbour().getPosition());
//                        GoogleMapPolyline lineToFirst = new GoogleMapPolyline(pair);
//                        movedMarker.setLineToFirstNeighbour(lineToFirst);
//                        googleMap.addPolyline(lineToFirst);
//                    }
//                    if (movedMarker.getSecondNeighbour() != null) {
//                        googleMap.removePolyline(movedMarker.getLineToSecondNeighbour());
//                        ArrayList<LatLon> pair = new ArrayList<LatLon>(2);
//                        pair.add(movedMarker.getPosition());
//                        pair.add(movedMarker.getSecondNeighbour().getPosition());
//                        GoogleMapPolyline lineToSecond = new GoogleMapPolyline(pair);
//                        movedMarker.setLineToSecondNeighbour(lineToSecond);
//                        googleMap.addPolyline(lineToSecond);
//                    }
//                } catch (Exception e) {
//                    consoleLayout.addComponent(new Label("cant cast to Vertice"));
//                }
//            }
//        });
//
//        googleMap.addMarkerClickListener(new MarkerClickListener() {
//            @Override
//            public void markerClicked(GoogleMapMarker googleMapMarker) {
//                lastClickedMarker = (Vertice) googleMapMarker;
//            }
//        });
//
//        /**
//         * enabled if marker is added. Then other marker must be clicked
//         */
//        Button connectVertices = new Button("ConnectVertices", new Button.ClickListener() {
//            @Override
//            public void buttonClick(ClickEvent clickEvent) {
//                if (lastClickedMarker == null) {
//                    consoleLayout.addComponent(new Label("click a marker for joining"));
//                } else {
//                    lastAddedMarker.setSecondNeighbour(lastClickedMarker.getSecondNeighbour());
//                    lastAddedMarker.setFirstNeighbour(lastClickedMarker);
//                    lastAddedMarker.getSecondNeighbour().setFirstNeighbour(lastAddedMarker);
//                    googleMap.removePolyline(lastClickedMarker.getLineToSecondNeighbour());
//                    lastClickedMarker.setSecondNeighbour(lastAddedMarker);
//                    drawLine(lastClickedMarker, lastAddedMarker);
//                    // hashmap.add line
//                    drawLine(lastAddedMarker, lastAddedMarker.getSecondNeighbour());
//                    // hashmap add line
//                    // TODO disable button
//                }
//            }
//        });
//        buttonLayout.addComponent(connectVertices);
//        connectVertices.setEnabled(false);
//
//        /**
//         * remove any marker
//         */
//        Button removeVertice = new Button("RemoveVertice",
//                new Button.ClickListener() {
//                    @Override
//                    public void buttonClick(ClickEvent clickEvent) {
//                        if (lastClickedMarker == null) {
//                            consoleLayout.addComponent(new Label("No one marker is clicked"));
//                        }
//                        else {
//                            // TODO if null neighbours
//                            if (lastClickedMarker.getFirstNeighbour() == null && lastClickedMarker.getSecondNeighbour() == null) {
//                                googleMap.removeMarker(lastClickedMarker);
//                            } else {
//                                lastClickedMarker.getFirstNeighbour().setSecondNeighbour(lastClickedMarker.getSecondNeighbour());
//                                //delete tracks in map
//                                drawLine(lastClickedMarker.getFirstNeighbour(), lastAddedMarker.getSecondNeighbour());
//                                googleMap.removeMarker(lastClickedMarker);
//                            }
//                        }
//                    }
//                });
//        buttonLayout.addComponent(removeVertice);
//
//        /**
//         * disconnect vertice from the track
//         */
//        Button disconnectVertices = new Button("DisconnectVertices",
//                new Button.ClickListener() {
//                    @Override
//                    public void buttonClick(ClickEvent clickEvent) {
//                        if (lastClickedMarker == null) {
//                            consoleLayout.addComponent(new Label("No one marker is clicked"));
//                        }
//                        else {
//                            // todo if null neighbours
//                            lastClickedMarker.getFirstNeighbour().setSecondNeighbour(lastClickedMarker.getSecondNeighbour());
//                            //delete tracks in map
//                            drawLine(lastClickedMarker.getFirstNeighbour(), lastAddedMarker.getSecondNeighbour());
//                        }
//                    }
//                });
//        buttonLayout.addComponent(disconnectVertices);
//
//
//
//        /**
//         * adding a new marker
//         */
//        googleMap.addMapClickListener(new MapClickListener() {
//            @Override
//            public void mapClicked(LatLon latLon) {
//                Vertice newMarker = new Vertice("new marker", latLon, true, null);
//                googleMap.addMarker(newMarker);
//                lastAddedMarker = newMarker;
//                consoleLayout.addComponent(new Label("Marker added"));
//                connectVertices.setEnabled(true);
//            }
//        });

        View application = new View();
        application.initComponents();
        this.setContent(application.getRootLayout());
        application.initListeners();
    }

//    public void drawLine(Vertice first, Vertice second) {
//        List<LatLon> coordinates = Arrays.asList(first.getPosition(), second.getPosition());
//        GoogleMapPolyline line = new GoogleMapPolyline(coordinates);
//        first.setLineToSecondNeighbour(line);
//        second.setLineToFirstNeighbour(line);
//        googleMap.addPolyline(line);
//        tracks.put(new Pair(first, second), line);
//    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
