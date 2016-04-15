package ru.hse.model;

import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import ru.hse.view.Styles;

import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by Ivan on 11.03.2016.
 */
public class Vertex extends GoogleMapMarker {

    /**
     * speed of wind
     */
    private int wind;
    private Date time;

    private Track parentTrack = null;

    public int getWind() {
        return wind;
    }

    public void setWind(int wind) {
        this.wind = wind;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public double getLat() {
        return getPosition().getLat();
    }

    public void setLat(double lat) {
        getPosition().setLat(lat);
    }

    public double getLon() {
        return getPosition().getLon();
    }

    public void setLon(double lon) {
        getPosition().setLon(lon);
    }

    public long getIdTrack() {
        return parentTrack.getId();
    }

    /**
     * used only for beans
     */
    public void setIdTrack(long id) {
        parentTrack.setId(id);
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
        wind = 0;
        time = null;
    }

    public Vertex(String caption, LatLon position, String iconUrl) {
        super(caption, position, true, iconUrl);
        wind = 0;
        time = null;
        setAnimationEnabled(true);
    }

    public Vertex(long id, LatLon position, int wind, Timestamp time) {
        super(id + "", position, true, Styles.Icon.getNecessaryIcon(wind).value());
        if (wind <= 0) {
            this.wind = 0;
        } else {
            this.wind = wind;
        }
        this.setId(id);
        this.time = new Date(time.getTime());
        setAnimationEnabled(false);
    }

    public Vertex(String caption, LatLon position, String iconUrl, int wind) {
        super(caption, position, true, iconUrl);
        this.wind = wind;
        time = null;
        setAnimationEnabled(false);
    }

    public Vertex(LatLon position, String iconUrl) {
        super("new", position, true, iconUrl);
        wind = 0;
        time = null;
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
            this.setIconUrl(Styles.Icon.getNecessaryIcon(wind).value());
        }
    }
}
