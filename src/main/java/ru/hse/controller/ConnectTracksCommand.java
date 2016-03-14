package ru.hse.controller;

import ru.hse.model.Model;
import ru.hse.model.Vertex;

/**
 * Created by Ivan on 13.03.2016.
 */
public class ConnectTracksCommand extends Command {
    private Vertex first = null;
    private Vertex second = null;
    private String direction = "";

    public ConnectTracksCommand(Vertex first, Vertex second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public void execute(Model model) {
        // TODO: fix non removing lines
        if (first.getNext() == null && second.getNext() == null) {
            direction = "next";
            first.setNext(second);
            second.setNext(first);
            model.connectTracksWithOneDirection(first, second, direction);
        }
        else if (first.getPrevious() == null && second.getPrevious() == null) {
            direction = "previous";
            first.setPrevious(second);
            second.setPrevious(first);
            model.connectTracksWithOneDirection(first, second, direction);
        }
        else if (first.getNext() == null && second.getPrevious() == null) {
            direction = "first";
            first.setNext(second);
            second.setPrevious(first);
            model.connectVertices(first, second);
        }
        else {
            first.setPrevious(second);
            second.setNext(first);
            model.connectVertices(second, first);
        }
    }

    @Override
    public void unexecute(Model model) {
        if (direction.equals("next")) {
            model.disconnectTracks(first, second, direction);
            first.setNext(null);
            second.setNext(null);
        }
        else if (direction.equals("previous")) {
            model.disconnectTracks(first, second, direction);
            first.setPrevious(null);
            second.setPrevious(null);
        }
        else if (direction.equals("first")) {
            model.disconnectVertices(first, second);
            first.setNext(null);
            second.setPrevious(null);
        }
        else {
            model.disconnectVertices(second, first);
            first.setPrevious(null);
            second.setNext(null);
        }
    }
}
