package ru.hse.model;

import com.vaadin.data.util.ObjectProperty;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Ivan on 11.03.2016.
 */
public class Vertex extends GoogleMapMarker {

    private int windSpeed;
    private Date timeStamp;

    private Track parentTrack = null;

    public int getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(int windSpeed) {
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
        setDraggable(true);
        windSpeed = 0;
        timeStamp = null;
    }

    public Vertex(String caption, LatLon position, String iconUrl) {
        super(caption, position, true, iconUrl);
        windSpeed = 0;
        timeStamp = null;
        setAnimationEnabled(true);
    }

    public Vertex(long id, LatLon position, int windSpeed, Timestamp timeStamp) {
        super(id + "", position, true, Styles.Icon.getNecessaryIcon(windSpeed).value());
        if (windSpeed <= 0) {
            this.windSpeed = 0;
        } else {
            this.windSpeed = windSpeed;
        }
        this.setId(id);
        this.timeStamp = new Date(timeStamp.getTime());
        setAnimationEnabled(false);
    }

    public Vertex(String caption, LatLon position, String iconUrl, int windSpeed) {
        super(caption, position, true, iconUrl);
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

    public void setHide(boolean hide) {
        if (hide) {
            this.setIconUrl(Styles.Icon.HIDDEN.value());
        }
        else {
            this.setIconUrl(Styles.Icon.getNecessaryIcon(windSpeed).value());
        }
    }
}
