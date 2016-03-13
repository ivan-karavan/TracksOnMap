package ru.hse.controller;

import ru.hse.newModel.Model;

import java.util.ArrayList;

/**
 * Created by Ivan on 11.03.2016.
 */
public class Controller {
    private ArrayList<Command> undoRedoList = new ArrayList<>();
    private Model model;
    private int pointer = -1;

    public Controller(Model model) {
        this.model = model;
    }

    public void handle(Command command) {
        pointer++;
        undoRedoList.add(pointer, command);
        command.execute(model);

        cleanTail();
    }

    public void undo() {
        if (pointer > -1) {
            undoRedoList.get(pointer).unexecute(model);
            pointer--;
        }
    }

    public void redo() {
        if (pointer < undoRedoList.size() - 1) {
            pointer++;
            undoRedoList.get(pointer).execute(model);
        }
    }

    private void cleanTail() {
        for (int i = pointer + 1; i < undoRedoList.size(); i++) {
            undoRedoList.remove(i);
        }
    }
}
