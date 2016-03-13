package ru.hse.oldModel;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;

/**
 * Created by Ivan on 15.02.2016.
 */
public class Vertice extends GoogleMapMarker {

    private double windSpeed;
    private Vertice firstNeighbour;
    private Vertice secondNeighbour;

    private GoogleMapPolyline lineToFirstNeighbour;
    private GoogleMapPolyline lineToSecondNeighbour;


    public Vertice() {
        super();
        windSpeed = 0;
        firstNeighbour = null;
        secondNeighbour = null;
    }

    public Vertice(String caption, LatLon position, boolean draggable, String iconUrl) {
        super(caption, position, draggable, iconUrl);
        windSpeed = 0;
        firstNeighbour = null;//new Vertice("first", new LatLon(61, 20), true, null);
        secondNeighbour = null; //new Vertice("second", new LatLon(61, 23), true, null);
    }

//    public Vertice(String caption, LatLon position, Vertice firstNeighbour) {
//        super(caption, position, true, null);
//        this.firstNeighbour = firstNeighbour;
//    }

    public Vertice(String caption, LatLon position, Vertice firstNeighbour, Vertice secondNeighbour) {
        super(caption, position, true, null);
        this.firstNeighbour = firstNeighbour;
        this.secondNeighbour = secondNeighbour;
    }

    public Vertice getFirstNeighbour() {
        return firstNeighbour;
    }

    public Vertice getSecondNeighbour() {
        return secondNeighbour;
    }

    public GoogleMapPolyline getLineToSecondNeighbour() {
        return lineToSecondNeighbour;
    }

    public GoogleMapPolyline getLineToFirstNeighbour() {
        return lineToFirstNeighbour;
    }

    public void setLineToFirstNeighbour(GoogleMapPolyline lineToFirstNeighbour) {
        this.lineToFirstNeighbour = lineToFirstNeighbour;
    }

    public void setLineToSecondNeighbour(GoogleMapPolyline lineToSecondNeighbour) {
        this.lineToSecondNeighbour = lineToSecondNeighbour;
    }

    public void setFirstNeighbour(Vertice firstNeighbour) {
        this.firstNeighbour = firstNeighbour;
    }

    public void setSecondNeighbour(Vertice secondNeighbour) {
        this.secondNeighbour = secondNeighbour;
    }
}
