package ru.hse.controller;

import com.vaadin.tapio.googlemaps.client.LatLon;
import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 12.03.2016.
 */
public class MoveVertexCommand extends Command {
    private Vertex vertex;
    private LatLon oldPosition;
    private LatLon currentPosition;

    public MoveVertexCommand(Vertex vertex, LatLon oldPosition) {
        this.vertex = vertex;
        this.oldPosition = oldPosition;
        this.currentPosition = vertex.getPosition();
    }

    @Override
    public void execute(Model model) {
        model.moveVertex(vertex, currentPosition);
    }

    @Override
    public void unexecute(Model model) {
        model.moveVertex(vertex, oldPosition);
    }
}
