package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 11.03.2016.
 */
public class RemoveVertexCommand extends Command {
    private Vertex vertex;

    public RemoveVertexCommand(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public void execute(Model model) {
        model.disconnectVertices(vertex, vertex.getNext());
        model.disconnectVertices(vertex.getPrevious(), vertex);
        model.removeVertex(vertex);
    }

    @Override
    public void unexecute(Model model) {
        model.addVertex(vertex);
        model.connectVertices(vertex, vertex.getNext());
        model.connectVertices(vertex.getPrevious(), vertex);
    }
}
