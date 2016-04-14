package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 11.03.2016.
 */
public class AddVertexCommand extends Command {
    private Vertex vertex;

    public AddVertexCommand(Vertex vertex) {
        this.vertex = vertex;
    }

    @Override
    public void execute(Model model) {
        model.addNewTrack(vertex);
    }

    @Override
    public void unexecute(Model model) {
        model.removeEmptyTrack(vertex.getParentTrack());
        model.removeVertex(vertex);
    }
}
