package ru.hse.controller;

import ru.hse.newModel.Model;
import ru.hse.newModel.Vertex;

/**
 * Created by Ivan on 12.03.2016.
 */
public class ConnectCommand extends Command {
    private Vertex from = null;
    private Vertex to = null;

    public ConnectCommand(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(Model model) {
        //from = oldModel.getCurrentVertex();
        //to = oldModel.getPreviousClickedVertex();

        if (from.getNext() != null) {
            model.disconnectVertices(from, from.getNext());
            model.connectVertices(from, to);
            model.connectVertices(to, from.getNext());

            to.setNext(from.getNext());
            from.setNext(to);
        }
        else {
            model.connectVertices(from, to);
            from.setNext(to);
        }
    }

    @Override
    public void unexecute(Model model) {
        if (to.getNext() != null) {
            model.disconnectVertices(to, to.getNext());
            model.disconnectVertices(from, to);
            model.connectVertices(from, to.getNext());

            from.setNext(to.getNext());
            to.setNext(null);
        }
        else {
            model.disconnectVertices(from, to);
            from.setNext(null);
        }
    }
}
