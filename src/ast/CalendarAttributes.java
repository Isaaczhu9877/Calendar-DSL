package ast;

import org.stringtemplate.v4.ST;

import java.lang.reflect.Field;

public class CalendarAttributes {
    public static final String TITLE  = "title";
    public static final String DESCRIPTION  = "description";
    public static final String START_DATE  = "startDate";
    public static final String END_DATE  = "endDate";
    public static final String PARTICIPANTS  = "participants";
    public static final String LAST_MODIFIED  = "lastModified";
    public static final String LOCATED  = "located";
    public static final String LOCATION  = "location";
    public static final String ORGANIZER  = "organizer";
    public static final String REPEAT  = "repeat";
    public static final String FREQUENCY  = "frequency";
    public static final String DURATION  = "duration";
    public static final String UNTIL  = "until";


    public static String[] getAttributes() {
        Field[] fields = CalendarAttributes.class.getDeclaredFields();
        String res[] = new String[fields.length];
        try {
            for (int i = 0; i < fields.length; i++) {
                res[i] = (String) fields[i].get(CalendarAttributes.class);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        return res;
    }
}