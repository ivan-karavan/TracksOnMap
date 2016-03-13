package ru.hse.controller;

import ru.hse.newModel.Model;
import ru.hse.newModel.Vertex;

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
        model.addVertex(vertex);
    }

    @Override
    public void unexecute(Model model) {
        model.removeVertex(vertex);
    }
}
