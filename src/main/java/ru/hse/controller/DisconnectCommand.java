package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 12.03.2016.
 */
public class DisconnectCommand extends Command {
    private Vertex from = null;
    /**
     * this vertex will be alone
     */
    private Vertex to = null;

    private Vertex vertex = null;
    private Vertex next = null;
    private Vertex previous = null;

    public DisconnectCommand(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    public DisconnectCommand(Vertex vertex) {
        this.vertex = vertex;
        next = vertex.getNext();
        previous = vertex.getPrevious();
    }

    @Override
    public void execute(Model model) {
        if (next != null) {
            model.disconnectVertices(vertex, next);
            next.setPrevious(vertex.getPrevious());
        }
        if (previous != null) {
            model.disconnectVertices(previous, vertex);
            previous.setNext(vertex.getNext());
        }
        vertex.setNext(null);
        vertex.setPrevious(null);
    }

    @Override
    public void unexecute(Model model) {
        if (next != null && previous != null) {
            model.disconnectVertices(previous, next);
        }
        if (next != null) {
            model.connectVertices(vertex, next);
            next.setPrevious(vertex);
            vertex.setNext(next);
        }
        if (previous != null) {
            model.connectVertices(previous, vertex);
            previous.setNext(vertex);
            vertex.setPrevious(previous);
        }
    }
}
