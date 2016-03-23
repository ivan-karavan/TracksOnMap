package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Track;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 19.03.2016.
 */
public class ConnectVertexToTrackCommand extends Command {
    private Vertex vertex;
    private Vertex vertexInTrack;
    private Track track;
    private int cases;

    public ConnectVertexToTrackCommand(Vertex vertex, Vertex vertexInTrack) {
        this.vertex = vertex;
        this.vertexInTrack = vertexInTrack;
        this.track = vertexInTrack.getParentTrack();
    }

    @Override
    public void execute(Model model) {
        // vertex will be first in track
        if (track.getFirst().equals(vertexInTrack)) {
            model.connectVertices(vertex, vertexInTrack);
            track.addFirst(vertex);
            cases = 1;
        }
        // vertex will be last
        else if (track.getLast().equals(vertexInTrack)) {
            model.connectVertices(vertexInTrack, vertex);
            track.add(vertex);
            cases = 2;
        }
        // vertex will connect before vertexInTrack
        else {
            Vertex next = track.getVertexAfter(vertexInTrack);
            model.disconnectVertices(vertexInTrack, next);
            model.connectVertices(vertexInTrack, vertex);
            model.connectVertices(vertex, next);
            track.addVertexAfter(vertexInTrack, vertex);
            cases = 3;
        }
        vertex.setParentTrack(track);
    }

    @Override
    public void unexecute(Model model) {
        switch (cases) {
            case 1:
                model.disconnectVertices(vertex, vertexInTrack);
                break;
            case 2:
                model.disconnectVertices(vertexInTrack, vertex);
                break;
            case 3:
                Vertex next = track.getVertexAfter(vertex);
                model.disconnectVertices(vertexInTrack, vertex);
                model.disconnectVertices(vertex, next);
                model.connectVertices(vertexInTrack, next);
                break;
        }
        track.removeVertex(vertex);
        vertex.setParentTrack(null);
    }
}
