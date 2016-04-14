package ru.hse.model;

import java.util.ArrayList;

/**
 * Created by Ivan on 18.03.2016.
 */
public class Track {
    private ArrayList<Vertex> vertices;
    private Styles.TrackColor style;
    private long id;
    private static long idCounter = 0;

    public Track() {
        id = idCounter;
        idCounter++;
        vertices = new ArrayList<>();
        style = Styles.TrackColor.next();
    }

    public Track(Vertex first) {
        id = idCounter;
        idCounter++;
        vertices = new ArrayList<>();
        vertices.add(first);
        first.setParentTrack(this);
        style = Styles.TrackColor.next();
    }

    public Track(long id) {
        this.id = id;
        vertices = new ArrayList<>();
        style = Styles.TrackColor.next();
    }

    public static Track TrackFactory(long id,Model model) {
        return new Track(id);
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
        vertex.setParentTrack(this);
    }

    public void addFirst(Vertex vertex) {
        vertices.add(0, vertex);
        vertex.setParentTrack(this);
    }

    public void addToPosition(int position, Vertex vertex) {
        vertices.add(position, vertex);
        vertex.setParentTrack(this);
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
        if (position <= 0) {
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

    public Vertex getByIndex(int index) {
        return vertices.get(index);
    }

    public Styles.TrackColor getStyle() {
        return style;
    }

    public long getId() {
        return id;
    }

    public int size() {
        return vertices.size();
    }

    public void clear() {
        vertices.clear();
    }

    /**
     * copy vertices from following to this and clear following
     * @param following will be cleaned
     */
    public void continueBy(Track following) {
        following.getVertices().stream().forEach(vertex -> {vertices.add(vertex); vertex.setParentTrack(this);});
        following.clear();
    }

    public void subList(int fromIndex, int toIndex) {
        ArrayList<Vertex> subList = new ArrayList<>(toIndex + 10);
        for (int i = fromIndex; i <= toIndex; i++) {
            subList.add(vertices.get(i));
        }
        vertices = subList;
    }

    public void setStyle(Styles.TrackColor style) {
        this.style = style;
    }
}