package ru.hse.view;

import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapMarker;
import ru.hse.model.Model;
import ru.hse.model.Vertex;
import ru.hse.view.Styles;

/**
 * Created by Ivan on 18.04.2016.
 */
public class ClickedVertices {
    private GoogleMap map;
    private Vertex lastClicked;
    private Vertex previousClicked;

    private Vertex helper;
    private boolean helperOnMap;

    public ClickedVertices(GoogleMap map) {
        this.map = map;
        helper = new Vertex("", new LatLon(84, 65), Styles.Icon.HIDDEN.value(), -1);
        helperOnMap = false;
        lastClicked = null;
        previousClicked = null;
    }

    public void setLastClicked(GoogleMapMarker newLast) {
        if (previousClicked != null) {
            previousClicked.updateIconToCorresponding();
        }
        previousClicked = lastClicked;
        lastClicked = (Vertex)newLast;
        lastClicked.setIconUrl(Styles.Icon.CLICKED.value());

        updateMarkersOnMap();
    }

    public Vertex getLastClicked() {
        return lastClicked;
    }

    public Vertex getPreviousClicked() {
        return previousClicked;
    }

    public boolean notNull() {
        return lastClicked != null;
    }

    public boolean anyAlone() {
        return lastClicked.getParentTrack().size() == 1 || previousClicked.getParentTrack().size() == 1;
    }

    public boolean equal() {
        return lastClicked == previousClicked;
    }

    public void toNull() {
        if (lastClicked != null) {
            lastClicked.updateIconToCorresponding();
        }
        if (previousClicked != null) {
            previousClicked.updateIconToCorresponding();
        }
        lastClicked = previousClicked = null;
        updateMarkersOnMap();
    }

    private void updateMarkersOnMap() {
        if (helperOnMap) {
            map.removeMarker(helper);
        }
        else {
            map.addMarker(helper);
        }
        helperOnMap = !helperOnMap;
    }
}
