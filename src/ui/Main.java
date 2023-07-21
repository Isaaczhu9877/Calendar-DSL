package ui;

import ast.Program;
import libs.Calendar;
import org.antlr.runtime.MismatchedTokenException;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Token;
import parser.ScheduleLexer;
import parser.ScheduleParser;
import java.io.IOException;


public class Main {
    public static void main(String[] args) throws IOException, MismatchedTokenException {
        ScheduleLexer lexer = new ScheduleLexer(CharStreams.fromFileName("input.txt"));
        for (Token token : lexer.getAllTokens()) {
            System.out.println(token);
        }
        lexer.reset();
        System.out.println("Done tokenizing");

        ScheduleParser parser = new ScheduleParser(lexer);
        Program program = parser.parseProgram();

        Calendar myCal =  new Calendar(Calendar.CALENDAR_FILE_PATH);
        program.evaluate(myCal);
        myCal.exportCalendar();
        System.out.println("Done evaluating");
    }

}
