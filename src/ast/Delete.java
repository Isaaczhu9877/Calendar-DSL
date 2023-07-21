package ast;

import libs.Action;
import libs.Calendar;
import java.util.HashMap;

public class Delete extends Action {
    HashMap<String, String> selectorAttributes;

    public Delete() {
        this.selectorAttributes = new HashMap<>();
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

    @Override
    public void evaluate(Calendar calendar) {
        calendar.deleteEvent(selectorAttributes);
    }
}
