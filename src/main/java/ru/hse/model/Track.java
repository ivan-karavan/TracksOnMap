package ru.hse.model;

import java.util.ArrayList;

/**
 * Created by Ivan on 18.03.2016.
 */
public class Track {
    private ArrayList<Vertex> vertices;
    private Styles.TrackColor style;

    public Track() {
        vertices = new ArrayList<>();
    }

    public Track(Vertex first) {
        vertices = new ArrayList<>();
        vertices.add(first);
        first.setParentTrack(this);
        style = Styles.TrackColor.next();
    }

    /**
     * inserts vertex before chosen vertex in track
     */
    public void addVertexBefore(Vertex next, Vertex inserted) {
        int position = vertices.indexOf(next);
        vertices.add(position, inserted);
    }

    /**
     * inserts vertex after chosen vertex in track
     */
    public void addVertexAfter(Vertex previous, Vertex inserted) {
        int position = vertices.indexOf(previous);
        if (position == vertices.size()) {
            vertices.add(inserted);
        }
        else {
            vertices.add(position + 1, inserted);
        }
    }

    public void add(Vertex vertex) {
        vertices.add(vertex);
    }

    public void addFirst(Vertex vertex) {
        vertices.add(0, vertex);
    }

    public void addToPosition(int position, Vertex vertex) {
        vertices.add(position, vertex);
    }

    public void removeVertex(Vertex vertex) {
        vertices.remove(vertex);
    }

    // todo: убрать, добавить итератор
    public ArrayList<Vertex> getVertices() {
        return vertices;
    }

    public int getPosition(Vertex vertex) {
        return vertices.indexOf(vertex);
    }

    public Vertex getFirst() {
        return vertices.get(0);
    }

    public Vertex getLast() {
        return vertices.get(vertices.size() - 1);
    }

    public Vertex getVertexBefore(Vertex vertex) {
        int position = vertices.indexOf(vertex);
        if (position == 0) {
            return null;
        }
        return vertices.get(position - 1);
    }

    public Vertex getVertexAfter(Vertex vertex) {
        int position = vertices.indexOf(vertex) + 1;
        if (position == vertices.size()) {
            return null;
        }
        return vertices.get(position);
    }

    public Styles.TrackColor getStyle() {
        return style;
    }

    public int size() {
        return vertices.size();
    }

    public void clear() {
        vertices.clear();
    }

    /**
     * copy vertices from following to this and clear following
     * @param following will be clean
     */
    public void continueBy(Track following) {
        for (Vertex vertex : following.getVertices()) {
            vertices.add(vertex);
            vertex.setParentTrack(this);
        }
        following.clear();
    }
}