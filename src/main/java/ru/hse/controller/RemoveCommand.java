package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Track;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 21.03.2016.
 */
public class RemoveCommand extends Command {
    private Vertex vertex;
    private Track track;
    private int cases;
    private int position;

    public RemoveCommand(Vertex vertex, Track track) {
        this.vertex = vertex;
        this.track = track;
    }

    @Override
    public void execute(Model model) {
        if (track.getVertexBefore(vertex) == null) {
            model.disconnectVertices(vertex, track.getVertexAfter(vertex));
            cases = 1;
        }
        else if (track.getVertexAfter(vertex) == null) {
            model.disconnectVertices(track.getVertexBefore(vertex), vertex);
            cases = 2;
        }
        else {
            model.disconnectVertices(track.getVertexBefore(vertex), vertex);
            model.disconnectVertices(vertex, track.getVertexAfter(vertex));
            model.connectVertices(track.getVertexBefore(vertex), track.getVertexAfter(vertex));
            position = track.getPosition(vertex);
            cases = 3;
        }
        track.removeVertex(vertex);
        if (track.size() == 0) {
            model.removeEmptyTrack(track);
        }
        vertex.setParentTrack(null);
        model.removeVertex(vertex);
    }

    @Override
    public void unexecute(Model model) {
        if (track.getVertices().size() == 0) {
            track.addFirst(vertex);
            model.addTrack(track);
        }
        else {
            model.addVertex(vertex);
            vertex.setParentTrack(track);
            switch (cases) {
                case 1:
                    track.addFirst(vertex);
                    model.connectVertices(vertex, track.getFirst());
                    break;
                case 2:
                    track.add(vertex);
                    model.connectVertices(track.getLast(), vertex);
                    break;
                case 3:
                    track.addToPosition(position, vertex);
                    model.disconnectVertices(track.getVertexBefore(vertex), track.getVertexAfter(vertex));
                    model.connectVertices(track.getVertexBefore(vertex), vertex);
                    model.connectVertices(vertex, track.getVertexAfter(vertex));
                    break;
            }
        }
    }
}
