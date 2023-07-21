package ast;

import libs.Action;
import libs.Calendar;
import java.util.HashMap;

public class Change extends Action {
    HashMap<String, String> selectorAttributes;
    HashMap<String, String> changedAttributes;

    public Change() {
        this.selectorAttributes = new HashMap<>();
        this.changedAttributes = new HashMap<>();
    }

    public void addSelector(String key, String val) {
        switch (key) {
            case "title", "titled" -> selectorAttributes.put("title", val);
            case "description" -> selectorAttributes.put("description", val);
            case "start", "starting" -> selectorAttributes.put("startDate", val);
            case "end", "ending" -> selectorAttributes.put("endDate", val);
            case "participants", "participating" -> selectorAttributes.put("participants", val);
            case "located", "location" -> selectorAttributes.put("location", val);
            case "organizer" -> selectorAttributes.put("organizer", val);
            case "repeat" -> selectorAttributes.put("repeat", val);
            case "frequency" -> selectorAttributes.put("frequency", val);
            case "until" -> selectorAttributes.put("until", val);
            default -> {
            }
        }
    }

    public void addChanged(String key, String val) {
            switch (key) {
                case "title", "titled" -> changedAttributes.put("title", val);
                case "description" -> changedAttributes.put("description", val);
                case "start", "starting" -> changedAttributes.put("startDate", val);
                case "end", "ending" -> changedAttributes.put("endDate", val);
                case "participants", "participating" -> changedAttributes.put("participants", val);
                case "located", "location" -> changedAttributes.put("location", val);
                case "organizer" -> changedAttributes.put("organizer", val);
                case "repeat" -> changedAttributes.put("repeat", val);
                case "frequency" -> changedAttributes.put("frequency", val);
                case "until" -> changedAttributes.put("until", val);
                default -> {
                }
            }
    }

    @Override
    public void evaluate(Calendar calendar) {
        calendar.changeEvent(selectorAttributes, changedAttributes);

    }
}
