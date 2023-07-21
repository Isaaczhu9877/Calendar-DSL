package libs;

import ast.CalendarAttributes;
import org.json.simple.JSONObject;

import java.lang.reflect.Field;
import java.util.HashMap;

public class CalendarEvent {
    String title;
    String description;
    String startDate;
    String endDate;
    String participants;
    String lastModified;
    String organizer;
    String repeat;
    String frequency;
    String location;
    String until;

    public void setEventAttribute(String attr, String val) {
        switch (attr) {
            case "title" -> this.title = val;
            case "description" -> this.description = val;
            case "startDate" -> this.startDate = val;
            case "endDate" -> this.endDate = val;
            case "location" -> this.location = val;
            case "participants" -> this.participants = val;
            case "lastModified" -> this.lastModified = val;
            case "organizer" -> this.organizer = val;
            case "repeat" -> this.repeat = val;
            case "frequency" -> this.frequency = val;
            case "until" -> this.until = val;
            default -> {
            } // TODO something for default
        }
    }

    public String getEventAttribute(String attr) {
        return switch (attr) {
            case "title" -> this.title;
            case "startDate" -> this.startDate;
            case "endDate" -> this.endDate;
            case "location" -> this.location;
            case "participants" -> this.participants;
            case "description" -> this.description;
            case "lastModified" -> this.lastModified;
            case "organizer" -> this.organizer;
            case "repeat" -> this.repeat;
            case "frequency" -> this.frequency;
            case "until" -> this.until;
            default -> "";
        };
    }

    public static CalendarEvent fromJSON(Object obj) {
        CalendarEvent event = new CalendarEvent();
        try {
            JSONObject eventJsonObj = (JSONObject) obj;

            // get each field from json object and set them to the CalendarEvent obj.
            for (Field declaredField : CalendarAttributes.class.getDeclaredFields()) {
                String jsonKey = (String) declaredField.get(CalendarAttributes.class);
                String value = (String) eventJsonObj.get(jsonKey);
//                    event.getClass().getDeclaredField(jsonKey).set(event, value);
                event.setEventAttribute(jsonKey, value);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return event;
    }

    public JSONObject toJSON() {
        JSONObject newEvent = new JSONObject();

        try {
            for (String key : CalendarAttributes.getAttributes()) {
                newEvent.put(key, this.getEventAttribute(key));
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return newEvent;
    }

    // Converts event fields to single obj string
    public String stringifyEvent() {
        HashMap<String, String> eventOutputMap = new HashMap<>();
        eventOutputMap.put("title", this.title);
        eventOutputMap.put("startDate", this.startDate);
        eventOutputMap.put("endDate", this.endDate);
        eventOutputMap.put("location", this.location);
        eventOutputMap.put("participants", this.participants);
        eventOutputMap.put("description", this.description);
        eventOutputMap.put("lastModified", this.lastModified);
        eventOutputMap.put("organizer", this.organizer);
        eventOutputMap.put("repeat", this.repeat);
        eventOutputMap.put("frequency", this.frequency);
        eventOutputMap.put("until", this.until);
        return eventOutputMap.toString();
    }

    @Override
    public String toString() {
        return "CalendarEvent{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", participants='" + participants + '\'' +
                ", lastModified='" + lastModified + '\'' +
                ", organizer='" + organizer + '\'' +
                ", repeat='" + repeat + '\'' +
                ", frequency='" + frequency + '\'' +
                ", location='" + location + '\'' +
                ", until='" + until + '\'' +
                '}';
    }

    public void clone(CalendarEvent event) {

        this.title = event.title;
        this.startDate = event.startDate;
        this.endDate = event.endDate;
        this.location = event.location;
        this.participants = event.participants;
        this.description = event.description;
        this.lastModified = event.lastModified;
        this.organizer = event.organizer;
        this.repeat = event.repeat;
        this.frequency = event.frequency;
        this.until = event.until;
    }

//        public boolean equals(CalendarEvent obj1, CalendarEvent obj2) {
//            if (obj1.title != null && !obj1.title.equals(obj2.title))
//                return false;
//            if (obj1.description != null && !obj1.description.equals(obj2.description))
//                return false;
//            if (obj1.startDate != null && !obj1.startDate.equals(obj2.startDate))
//                return false;
//            if (obj1.endDate != null && !obj1.endDate.equals(obj2.endDate))
//                return false;
//            if (obj1.location != null && !obj1.location.equals(obj2.location))
//                return false;
//            if (obj1.participants != null && !obj1.participants.equals(obj2.participants))
//                return false;
//            if (obj1.lastModified != null && !obj1.lastModified.equals(obj2.lastModified))
//                return false;
//            return obj1.organizer == null || obj1.organizer.equals(obj2.organizer);
//        }
}
