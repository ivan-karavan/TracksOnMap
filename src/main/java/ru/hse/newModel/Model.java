package ru.hse.newModel;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ivan on 12.03.2016.
 */
public class Model {
    private GoogleMap map;

    private HashMap<Vertex, GoogleMapPolyline> linesFromVertices;
    private HashMap<Vertex, GoogleMapPolyline> linesToVertices;

    public Model(GoogleMap map) {
        this.map = map;
        linesFromVertices = new HashMap<>();
        linesToVertices = new HashMap<>();
    }

    public void addVertex(Vertex vertex) {
        map.addMarker(vertex);
    }

    public void removeVertex(Vertex vertex) {
        map.removeMarker(vertex);
    }

    public void moveVertex(Vertex vertex, LatLon position) {
        vertex.setPosition(position);

        map.removePolyline(linesFromVertices.get(vertex));
        linesFromVertices.remove(vertex);
        linesToVertices.remove(vertex.getNext());

        map.removePolyline(linesToVertices.get(vertex));
        linesFromVertices.remove(vertex.getPrevious());
        linesToVertices.remove(vertex);

        connectVertices(vertex, vertex.getNext());
        connectVertices(vertex.getPrevious(), vertex);
    }

    public void loadDataFromDB() {

    }

    public void connectVertices(Vertex first, Vertex second) {
        // на будущее
//        if (first.getWindSpeed() > 0) {
//            strokeColor, strokeWigth, strokeOpacity
//        }
        if (!(first == null || second == null)) {
            ArrayList<LatLon> coordinates = new ArrayList<>(2);
            coordinates.add(first.getPosition());
            coordinates.add(second.getPosition());
            GoogleMapPolyline line = new GoogleMapPolyline(coordinates);
            map.addPolyline(line);
            linesFromVertices.put(first, line);
            linesToVertices.put(second, line);
        }
    }

    public void disconnectVertices(Vertex first, Vertex second) {
        map.removePolyline(linesFromVertices.get(first));
        linesFromVertices.remove(first);
        linesToVertices.remove(second);
    }

    public GoogleMap getMap() {
        return map;
    }
}
