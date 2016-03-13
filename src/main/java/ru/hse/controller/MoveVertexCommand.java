package ru.hse.controller;

import com.vaadin.tapio.googlemaps.client.LatLon;
import ru.hse.newModel.Model;
import ru.hse.newModel.Vertex;

/**
 * Created by Ivan on 12.03.2016.
 */
public class MoveVertexCommand extends Command {
    private Vertex vertex;
    private LatLon oldPosition;

    public MoveVertexCommand(Vertex vertex, LatLon oldPosition) {
        this.vertex = vertex;
        this.oldPosition = oldPosition;
    }

    @Override
    public void execute(Model model) {
        model.moveVertex(vertex, vertex.getPosition());
    }

    @Override
    public void unexecute(Model model) {
        model.moveVertex(vertex, oldPosition);
    }
}
