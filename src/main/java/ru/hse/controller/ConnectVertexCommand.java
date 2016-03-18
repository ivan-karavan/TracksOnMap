package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 12.03.2016.
 */
public class ConnectVertexCommand extends Command {
    private Vertex from = null;
    /**
     * should be alone
     */
    private Vertex to = null;

    /**
     * Vertex "to" should be alone
     */
    public ConnectVertexCommand(Vertex first, Vertex second) {
        // Vertex alone
        if (second.getNext() != null || second.getPrevious() != null) {
            this.from = second;
            this.to = first;
        }
        else {
            this.from = first;
            this.to = second;
        }
    }

    @Override
    public void execute(Model model) {
        if (from.getNext() != null) {
            model.disconnectVertices(from, from.getNext());
            model.connectVertices(from, to);
            model.connectVertices(to, from.getNext());

            to.setNext(from.getNext());
            from.getNext().setPrevious(to);
            from.setNext(to);
            to.setPrevious(from);
        }
        else {
            model.connectVertices(from, to);
            from.setNext(to);
            to.setPrevious(from);
        }
    }

    @Override
    public void unexecute(Model model) {
        if (to.getNext() != null) {
            model.disconnectVertices(to, to.getNext());
            model.disconnectVertices(from, to);
            model.connectVertices(from, to.getNext());

            from.setNext(to.getNext());
            from.getNext().setPrevious(from);
            to.setNext(null);
            to.setPrevious(null);
        }
        else {
            model.disconnectVertices(from, to);
            from.setNext(null);
            to.setPrevious(null);
        }
    }
}
