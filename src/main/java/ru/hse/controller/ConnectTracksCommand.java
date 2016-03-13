package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 13.03.2016.
 */
public class ConnectTracksCommand extends Command {
    private Vertex from = null;
    private Vertex to = null;

    public ConnectTracksCommand(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public void execute(Model model) {

    }

    @Override
    public void unexecute(Model model) {

    }
}
