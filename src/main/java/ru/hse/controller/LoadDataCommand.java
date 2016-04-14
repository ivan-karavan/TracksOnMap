package ru.hse.controller;

import ru.hse.model.Model;

import java.sql.Date;

/**
 * Created by Ivan on 10.04.2016.
 */
public class LoadDataCommand extends Command {
    java.util.Date fromDate;
    java.util.Date toDate;

    public LoadDataCommand(java.util.Date fromDate, java.util.Date toDate) {
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    @Override
    public void execute(Model model) {
        model.loadDataFromDB(fromDate, toDate);
    }

    @Override
    public void unexecute(Model model) {
        // do nothing
    }
}
