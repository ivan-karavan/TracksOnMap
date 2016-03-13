package ru.hse.newModel;

import com.google.gwt.maps.client.services.Time;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import com.vaadin.ui.Calendar;

/**
 * Created by Ivan on 11.03.2016.
 */
public class Vertex extends GoogleMapMarker {

    private double windSpeed;
    private Object timeStamp;

    /**
     * neighbour of this vertex
     * if not null exists line from this to next
     */
    private Vertex next;

    private Vertex previous;

    public void setNext(Vertex next) {
        this.next = next;
    }

    public Vertex getNext() {
        return next;
    }

    public Vertex getPrevious() {
        return previous;
    }

    public void setPrevious(Vertex previous) {
        this.previous = previous;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public Vertex() {
        super();
        next = null;
        previous = null;
        windSpeed = 0;
        timeStamp = null;
    }

    public Vertex(String caption, LatLon position, String iconUrl) {
        super(caption, position, true, iconUrl);
        next = null;
        previous = null;
        windSpeed = 0;
        timeStamp = null;
    }

    public Vertex(String caption, LatLon position, String iconUrl, double windSpeed) {
        super(caption, position, true, iconUrl);
        next = null;
        previous = null;
        this.windSpeed = windSpeed;
        timeStamp = null;
    }

    public Vertex(String caption, LatLon position, String iconUrl, Vertex next) {
        super(caption, position, true, iconUrl);
        this.next = next;
        this.windSpeed = 0;
        timeStamp = null;
    }
}
