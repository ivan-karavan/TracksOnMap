package ru.hse.model;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

/**
 * Created by Ivan on 11.03.2016.
 */
public class Vertex extends GoogleMapMarker {

    private double windSpeed;
    private Object timeStamp;

    private Track parentTrack = null;

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

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Track getParentTrack() {
        return parentTrack;
    }

    public void setParentTrack(Track parentTrack) {
        this.parentTrack = parentTrack;
    }

    public int getPositionInTrack() {
        return parentTrack.getPosition(this);
    }

    public static Vertex VertexFactory(LatLon position) {
        Vertex vertex = new Vertex("new vertex", position, Styles.Icon.LOWSPEED.value());
        vertex.setCaption(vertex.getId() + "");
        return vertex;
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
        setAnimationEnabled(true);
    }

    public Vertex(String caption, LatLon position, String iconUrl, double windSpeed) {
        super(caption, position, true, iconUrl);
        next = null;
        previous = null;
        this.windSpeed = windSpeed;
        timeStamp = null;
    }

    public Vertex(LatLon position, String iconUrl) {
        super("new", position, true, iconUrl);
        windSpeed = 0;
        timeStamp = null;
        setCaption(getId() + "");
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) {
            return true;
        } else if(obj == null) {
            return false;
        } else if(this.getClass() != obj.getClass()) {
            return false;
        } else {
            Vertex other = (Vertex) obj;
            return this.getId() == other.getId();
        }
    }

    public void setVisible(boolean isVisible) {
        if (isVisible) {
            //this.setIconUrl(currentIcon);
        }
        else {
            //this.setIconUrl(Styles.Icon.HIDDEN);
        }
    }
}
