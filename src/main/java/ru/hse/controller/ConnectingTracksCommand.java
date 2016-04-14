package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Track;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 21.03.2016.
 */
public class ConnectingTracksCommand extends Command {
    /**
     * track of the {@code end}
     */
    private Track first;

    /**
     * track of the {@code begin}
     */
    private Track second;

    /**
     * end of the track to be connected
     * this vertex will be connected with begin of the second track
     */
    private Vertex end;
    /**
     * begin of the second track to be connected
     * will be before {@code end}
     */
    private Vertex begin;

    private boolean correctness;

    /**
     * params can be only ends of tracks
     * @param end vertex from which will be connected tracks
     * @param begin to this vertex will be draw line
     */
    public ConnectingTracksCommand(Vertex end, Vertex begin) {
        if (end.getParentTrack().getLast().equals(end) && begin.getParentTrack().getFirst().equals(begin)) {
            this.end = end;
            this.begin = begin;
            first = this.end.getParentTrack();
            second = this.begin.getParentTrack();
            correctness = true;
        }
        else if (end.getParentTrack().getFirst().equals(end) && begin.getParentTrack().getLast().equals(begin)) {
            this.end = begin;
            this.begin = end;
            first = this.end.getParentTrack();
            second = this.begin.getParentTrack();
            correctness = true;
        }
        else {
            correctness = false;
        }
    }

    @Override
    public void execute(Model model) {
        if (correctness) {
            first.continueBy(second);
            model.removeEmptyTrack(second);
            model.redrawTrack(first);
        }
    }

    @Override
    public void unexecute(Model model) {
        if (correctness) {
            model.disconnectVertices(end, begin);
            for (int i = first.getPosition(begin); i < first.size(); i++) {
                second.add(first.getByIndex(i));
            }
            first.subList(0, first.getPosition(end));
            model.redrawTrack(first);
            model.registerTrack(second);
            model.redrawTrack(second);
        }
    }
}
