package ru.hse.model;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.tapio.googlemaps.client.overlays.GoogleMapPolyline;
import ru.hse.view.Styles;

import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Created by Ivan on 12.03.2016.
 */
public class Model {
    private GoogleMap map;

    private Map<Vertex, GoogleMapPolyline> linesFromVertices;
    private Map<Vertex, GoogleMapPolyline> linesToVertices;
    private BeanItemContainer<Vertex> container;

    private Set<Track> tracks;

    public Model(GoogleMap map) {
        this.map = map;
        linesFromVertices = new HashMap<>();
        linesToVertices = new HashMap<>();
        tracks = new HashSet<>();
        container = new BeanItemContainer<>(Vertex.class);
    }

    public void addTrack(Track track) {
        ArrayList<Vertex> vertices = track.getVertices();
        addVertex(track.getFirst());
        int i = 1;
        while (i < vertices.size()) {
            addVertex(vertices.get(i));
            connectVertices(vertices.get(i - 1), vertices.get(i));
            i++;
        }
        tracks.add(track);
    }

    public void registerTrack(Track track) {
        tracks.add(track);
    }

    /**
     * Adds new track with one vertex
     *
     * @param vertex first vertex in track
     */
    public void addNewTrack(Vertex vertex) {
        Track track = new Track(vertex);
        addTrack(track);
    }

    /**
     * Does not remove full track from the map, only from {@code tracks}
     *
     * @param track empty track
     */
    public void unregisterTrack(Track track) {
        tracks.remove(track);
    }

    public void redrawTrack(Track track) {
        track.getVertices().stream().forEach(vertex -> {
            disconnectVertices(vertex, vertex.getParentTrack().getVertexAfter(vertex));
            connectVertices(vertex, vertex.getParentTrack().getVertexAfter(vertex));
        });
    }

    public void addVertex(Vertex vertex) {
        map.addMarker(vertex);
        container.addItem(vertex);
    }

    public void removeVertex(Vertex vertex) {
        map.removeMarker(vertex);
        container.removeItem(vertex);
    }

    public void moveVertex(Vertex vertex, LatLon position) {
        disconnectVertices(vertex, vertex.getParentTrack().getVertexAfter(vertex));
        disconnectVertices(vertex.getParentTrack().getVertexBefore(vertex), vertex);

        vertex.setPosition(position);

        connectVertices(vertex, vertex.getParentTrack().getVertexAfter(vertex));
        connectVertices(vertex.getParentTrack().getVertexBefore(vertex), vertex);
    }

    public void loadDataFromDB(Date from, Date to) {
        try {
            Timestamp timeFrom = new Timestamp(from.getTime());
            Timestamp timeTo = new Timestamp(to.getTime());

            Class.forName("org.postgresql.Driver");
            String url = "jdbc:postgresql://localhost:5432/tracksonmap";

            Connection conn = DriverManager.getConnection(url, "postgres", "1");

            Statement s = conn.createStatement();
            ResultSet r = s.executeQuery("select id_track, tracks.id_vertex, lat, lon, iso_time, windspeed, pos from tracks\n" +
                    "inner join vertices on tracks.id_vertex = vertices.id_vertex\n" +
                    "where iso_time BETWEEN \'" + timeFrom +"\' AND \'" + timeTo +"\' \n" +
                    "order by id_track,iso_time");


            long currentTrackId = -1;
            Track currentTrack = null;
            while (r.next()) {
                if (r.getLong(1) != currentTrackId) {
                    if (currentTrack != null) {
                        if (currentTrack.size() != 0) {
                            addTrack(currentTrack);
                        }
                    }
                    currentTrackId = r.getLong(1);
                    currentTrack = new Track(r.getLong(1));
                }
                if (unique(currentTrack)) {
                    currentTrack.add(new Vertex(r.getLong(2), new LatLon(r.getDouble(3), r.getDouble(4)),
                            r.getInt(6), r.getTimestamp(5)));
                }
            }
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GoogleMapPolyline polylineFactory(List<LatLon> coordinates) {
        return new GoogleMapPolyline(coordinates, Styles.TrackColor.next().value(), 1.0D, 1);
    }

    public GoogleMapPolyline polylineFactory(List<LatLon> coordinates, Styles.TrackColor style) {
        return new GoogleMapPolyline(coordinates, style.value(), 1.0D, 2);
    }

    /**
     * Draw line on the map between first and second and save this line.
     * Order is important
     *
     * @param first  start of the line
     * @param second end of the line
     */
    public void connectVertices(Vertex first, Vertex second) {
        if (!(first == null || second == null)) {
            List<LatLon> coordinates = Arrays.asList(first.getPosition(), second.getPosition());
            GoogleMapPolyline line = polylineFactory(coordinates, first.getParentTrack().getStyle());
            map.addPolyline(line);
            linesFromVertices.put(first, line);
            linesToVertices.put(second, line);
        }
    }

    /**
     * Remove line on the map between first and second.
     * Order is important
     *
     * @param first  start of the line
     * @param second end of the line
     */
    public void disconnectVertices(Vertex first, Vertex second) {
        if (!(first == null || second == null)) {
            map.removePolyline(linesFromVertices.get(first));
            linesFromVertices.remove(first);
            linesToVertices.remove(second);
        }
    }

    public BeanItemContainer<Vertex> getContainer() {
        return container;
    }

    private Track getTrackWithId(long id) {
        return tracks.stream().filter(a -> a.getId() == id).findFirst().get();
    }

    private boolean unique(Track track) {
        if (tracks.stream().filter(a -> a.getId() == track.getId()).findFirst().isPresent()) {
            return false;
        }
        return true;
    }

    // helps update icons instantly
    private Vertex hidded = new Vertex("", new LatLon(83, 66), Styles.Icon.HIDDEN.value(), -1);
    public void hideMarkers(boolean hide) {
        map.getMarkers().stream().forEach(marker -> {
            Vertex v = (Vertex) marker;
            if (hide) {
                v.hide(true);
            } else {
                v.hide(false);
            }
        });
        // helps update icons instantly
        if (hide) {
            map.addMarker(hidded);
        }
        else {
            map.removeMarker(hidded);
        }
    }
}
