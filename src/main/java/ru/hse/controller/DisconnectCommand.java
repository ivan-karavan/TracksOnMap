package ru.hse.controller;

import ru.hse.newModel.Model;
import ru.hse.newModel.Vertex;

/**
 * Created by Ivan on 12.03.2016.
 */
public class DisconnectCommand extends Command {
    private Vertex from = null;
    /**
     * this vertex will be alone
     */
    private Vertex to = null;

    public DisconnectCommand(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
        // TODO: if from.next != to, but from.previous == to
    }

    @Override
    public void execute(Model model) {
        if (to.getNext() != null) {
            model.disconnectVertices(from, to);
            model.disconnectVertices(to, to.getNext());
            model.connectVertices(from, to.getNext());

            from.setNext(to.getNext());
            to.setNext(null);
        }
        else {
            model.disconnectVertices(from, to);
            from.setNext(null);
        }
    }

    @Override
    public void unexecute(Model model) {
        if (from.getNext() != null) {
            model.disconnectVertices(from, from.getNext());
            model.connectVertices(to, from.getNext());
            model.connectVertices(from, to);

            to.setNext(from.getNext());
            from.setNext(to);
        }
        else {
            model.connectVertices(from, to);
            from.setNext(to);
        }
    }
}
