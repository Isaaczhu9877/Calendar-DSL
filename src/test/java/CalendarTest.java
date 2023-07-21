package test.java;

import ast.CalendarAttributes;
import libs.Calendar;
import libs.CalendarEvent;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

public class CalendarTest {
    @Test
    public void testCalendarImportAndExport() {
        System.out.println("Inside testCalendar()");
        String testFilePath = "src/disk/calendartest" + new Date().getTime() + ".json";
        Calendar cal = new Calendar();
        CalendarEvent event1 = new CalendarEvent();
        event1.setEventAttribute(CalendarAttributes.TITLE, "export test 1");
        event1.setEventAttribute(CalendarAttributes.DESCRIPTION, "export test desc 1");
        event1.setEventAttribute(CalendarAttributes.START_DATE, "2022 12 01 0800");
        event1.setEventAttribute(CalendarAttributes.END_DATE, "2022 12 01 1000");
        cal.getCalendarEvents().add(event1);
        CalendarEvent event2 = new CalendarEvent();
        event2.setEventAttribute(CalendarAttributes.TITLE, "export test 2");
        event2.setEventAttribute(CalendarAttributes.DESCRIPTION, "export test desc 2");
        cal.getCalendarEvents().add(event2);

        cal.saveToDisk(testFilePath);
        Calendar cal2 = new Calendar(testFilePath);
        assertEquals(2, cal2.getCalendarEvents().size());
    }
}
