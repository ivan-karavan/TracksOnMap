package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Track;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 21.03.2016.
 */
public class ConnectingTracksCommand extends Command {
    /**
     * this track will stay
     */
    private Track first;

    /**
     * this track will be removed
     */
    private Track second;

    private Vertex end;
    private Vertex begin;

    /**
     *
     * @param end vertex from which will be connected tracks
     * @param begin to this vertex will be draw line
     */
    public ConnectingTracksCommand(Vertex end, Vertex begin) {
        this.end = end;
        this.begin = begin;
        first = end.getParentTrack();
        second = begin.getParentTrack();
    }

    @Override
    public void execute(Model model) {
        first.continueBy(second);
        model.removeTrack(second);
        model.connectVertices(end, begin);
        // todo: set new style to new part of first track
    }

    @Override
    public void unexecute(Model model) {
        for (int i = first.getPosition(begin); i < first.size(); i++) {
            second.add(first.getVertices().get(i));
        }
        first.getVertices().subList(0, first.getPosition(begin));
        model.disconnectVertices(end, begin);
        model.addTrack(second);
    }
}
