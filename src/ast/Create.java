package ast;

import libs.Action;
import libs.Calendar;
import libs.Utils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Create extends Action {
    HashMap<String, String> eventAttributes;
    Utils utilsHelper;

    public Create() {
        utilsHelper = new Utils();
        List<String> defaultKeys = utilsHelper.getDefaultKeys();
        List<String> defaultValues = utilsHelper.getDefaultValues();
        eventAttributes = new HashMap<>();
        for (int i = 0; i < defaultKeys.size(); i++) {
            eventAttributes.put(defaultKeys.get(i), defaultValues.get(i));
        }
    }

    public void setAttribute(String attribute, String val) {
        switch (attribute) {
            case "title", "titled" -> eventAttributes.put("title", val);
            case "description" -> eventAttributes.put("description", val);
            case "start", "starting" -> eventAttributes.put("startDate", val);
            case "end", "ending" -> eventAttributes.put("endDate", val);
            case "participants", "participating" -> eventAttributes.put("participants", val);
            case "located", "location" -> eventAttributes.put("location", val);
            case "organizer" -> eventAttributes.put("organizer", val);
            case "repeat" -> eventAttributes.put("repeat", val);
            case "frequency" -> eventAttributes.put("frequency", val);
            case "until" -> eventAttributes.put("until", val);
            default -> {
            }
        }

    }

    @Override
    public void evaluate(Calendar calendar) {
        calendar.createEvent(this.eventAttributes);
    }

    public void clone(Create obj) {
        for (Map.Entry<String, String> entry: obj.eventAttributes.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // Update values in currentMap
            eventAttributes.put(key, value);
        }
    }
}
