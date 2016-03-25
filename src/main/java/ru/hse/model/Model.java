package ru.hse.model;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Ivan on 12.03.2016.
 */
public class Model {
    private GoogleMap map;

    private HashMap<Vertex, GoogleMapPolyline> linesFromVertices;
    private HashMap<Vertex, GoogleMapPolyline> linesToVertices;

    private HashSet<Track> tracks;

    public Model(GoogleMap map) {
        this.map = map;
        linesFromVertices = new HashMap<>();
        linesToVertices = new HashMap<>();
        tracks = new HashSet<>();
    }

    public void addTrack(Track track) {
        ArrayList<Vertex> vertices = track.getVertices();
        map.addMarker(track.getFirst());
        int i = 1;
        while (i < vertices.size()) {
            map.addMarker(vertices.get(i));
            connectVertices(vertices.get(i - 1), vertices.get(i));
        }
        tracks.add(track);
    }

    /**
     * Adds new track with one vertex
     * @param vertex first vertex in track
     */
    public void addNewTrack(Vertex vertex) {
        Track track = new Track(vertex);
        addTrack(track);
    }

    /**
     * Does not remove full track from the map, only from {@code tracks}
     * @param track empty track
     */
    public void removeTrack(Track track) {
        tracks.remove(track);
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

//        connectVertices(vertex, vertex.getNext());
//        connectVertices(vertex.getPrevious(), vertex);
        connectVertices(vertex, vertex.getParentTrack().getVertexAfter(vertex));
        connectVertices(vertex.getParentTrack().getVertexBefore(vertex), vertex);
    }

    public void loadDataFromDB() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public GoogleMapPolyline polylineFactory(ArrayList<LatLon> coordinates) {
        return new GoogleMapPolyline(coordinates, Styles.TrackColor.next().value(), 1.0D , 1);
    }

    public GoogleMapPolyline polylineFactory(ArrayList<LatLon> coordinates, Styles.TrackColor style) {
        return new GoogleMapPolyline(coordinates, style.value(), 1.0D , 2);
    }

    /**
     * Draw line on the map between first and second and save this line.
     * Order is important
     * @param first start of the line
     * @param second end of the line
     */
    public void connectVertices(Vertex first, Vertex second) {
        if (!(first == null || second == null)) {
            ArrayList<LatLon> coordinates = new ArrayList<>(2);
            coordinates.add(first.getPosition());
            coordinates.add(second.getPosition());
            GoogleMapPolyline line = polylineFactory(coordinates, first.getParentTrack().getStyle());
            //GoogleMapPolyline line = polylineFactory(coordinates);
            map.addPolyline(line);
            linesFromVertices.put(first, line);
            linesToVertices.put(second, line);
        }
    }

    /**
     * Remove line on the map between first and second.
     * Order is important
     * @param first start of the line
     * @param second end of the line
     */
    public void disconnectVertices(Vertex first, Vertex second) {
        map.removePolyline(linesFromVertices.get(first));
        linesFromVertices.remove(first);
        linesToVertices.remove(second);
    }

    public GoogleMap getMap() {
        return map;
    }

    /**
     * при загрузке треков будет ошибка, если сохранять в бд направление
     */
    public void connectTracksWithOneDirection(Vertex first, Vertex second, String direction) {
        ArrayList<LatLon> coordinates = new ArrayList<>(2);
        coordinates.add(first.getPosition());
        coordinates.add(second.getPosition());
        GoogleMapPolyline line = new GoogleMapPolyline(coordinates);
        map.addPolyline(line);
        if (direction.equals("next")) {
            linesFromVertices.put(first, line);
            linesFromVertices.put(second, line);
        }
        else {
            linesToVertices.put(first, line);
            linesToVertices.put(second, line);
        }
    }

    public void disconnectTracks(Vertex first, Vertex second, String direction) {
        if (direction.equals("next")) {
            map.removePolyline(linesFromVertices.get(first));
            linesFromVertices.remove(first);
            linesFromVertices.remove(second);
        }
        else {
            map.removePolyline(linesToVertices.get(first));
            linesToVertices.remove(first);
            linesToVertices.remove(second);
        }
    }
}
