package ast;

import libs.Action;
import libs.Calendar;
import libs.Node;

import java.util.List;

public class Program extends Node {
    private final List<Action> actions;

    public Program(List<Action> actions) {
        this.actions = actions;
    }

    public List<Action> getActions() {
        return actions;
    }

    @Override
    public void evaluate(Calendar calendar) {
        for (Action action: actions) {
            action.evaluate(calendar);
        }
    }
}
