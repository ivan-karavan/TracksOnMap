package ru.hse.controller;

import ru.hse.newModel.Model;

/**
 * Created by Ivan on 11.03.2016.
 */
public abstract class Command {

    public abstract void execute(Model model);

    public abstract void unexecute(Model model);
}
