package parser;

import libs.Action;
import libs.Tokens;
import ast.*;
import org.antlr.runtime.MismatchedTokenException;

import java.util.ArrayList;
import java.util.List;


public class ScheduleParser {
    private final Tokens tokens;

    public ScheduleParser(ScheduleLexer lexer) {
        this.tokens = new Tokens(lexer);
    }

    public Program parseProgram() throws MismatchedTokenException {
        // Expect our first token
        tokens.expectNext(ScheduleLexer.START);
        List<Action> actions = new ArrayList<>(); // instantiate our list of actions
        // while we aren't at program end
        while (!tokens.check(ScheduleLexer.PROGRAM_END)) {
            // token check -> go into create
            if (tokens.checkNext(ScheduleLexer.CREATE_START)) {
                actions.addAll(parseCreate());
            } else if (tokens.checkNext(ScheduleLexer.CHANGE_START)) {
                actions.add(parseChange());
            } else if (tokens.checkNext(ScheduleLexer.DELETE_START)) {
                actions.add(parseDelete());
            } else if (tokens.checkNext(ScheduleLexer.WS)) {
                // Do nothing
            } else {
                throw new MismatchedTokenException();
            }
        }
        return new Program(actions);
    }

    public ArrayList<Create> parseCreate() throws MismatchedTokenException {

        Create originalCreate = new Create();
        ArrayList<String> startDates = new ArrayList<>();
        ArrayList<String> endDates = new ArrayList<>();
        populateAttributes(originalCreate, startDates, endDates);
        ArrayList<Create> objectRequests = generateCreateRequests(originalCreate, startDates, endDates);
        return objectRequests;
    }

    public String stripQuotations(String val) {
        return val.replaceAll("^\"|\"$", "");
    }

    public Change parseChange() throws MismatchedTokenException {

        Change obj = new Change();
        while (!tokens.check(ScheduleLexer.END) && !tokens.check(ScheduleLexer.REPLACE)) {

            if (tokens.check(ScheduleLexer.ATTRIBUTE_KEY)) {
                String tokenKey = tokens.getNext().getText().strip();
                String tokenValue = this.stripQuotations(tokens.getNext().getText().strip());
                // -> at this point we are at a comma
                obj.addSelector(tokenKey, tokenValue);
                tokens.checkNext(ScheduleLexer.COMMA); // skip comma
            }
        }
        tokens.expectNext(ScheduleLexer.REPLACE);

        while (!tokens.check(ScheduleLexer.END)) {
            if (tokens.check(ScheduleLexer.ATTRIBUTE_KEY)) {
                String tokenKey = tokens.getNext().getText().strip();
                String tokenValue = this.stripQuotations(tokens.getNext().getText().strip());
                // -> at this point we are at a comma
                obj.addChanged(tokenKey, tokenValue);
                tokens.checkNext(ScheduleLexer.COMMA); // skip comma
            }
        }

        tokens.expectNext(ScheduleLexer.END);
        return obj;
    }

    public Delete parseDelete() throws MismatchedTokenException {
        Delete obj = new Delete();

        while (!tokens.check(ScheduleLexer.END)) {
            if (tokens.check(ScheduleLexer.ATTRIBUTE_KEY)) {
                String tokenKey = tokens.getNext().getText().strip();
                String tokenValue = this.stripQuotations(tokens.getNext().getText().strip());
                // -> at this point we are at a comma
                obj.addSelector(tokenKey, tokenValue);
                tokens.checkNext(ScheduleLexer.COMMA); // skip comma
            }
        }
        tokens.expectNext(ScheduleLexer.END);
        return obj;
    }

    private void populateAttributes(Create originalCreate, ArrayList<String> startDates, ArrayList<String> endDates) {

        while (!tokens.check(ScheduleLexer.END) && !tokens.check(ScheduleLexer.REPEAT)) {

            if (tokens.check(ScheduleLexer.ATTRIBUTE_KEY)) {
                String tokenKey = tokens.getNext().getText().strip();
                String tokenValue = this.stripQuotations(tokens.getNext().getText().strip());
                // -> at this point we are at a comma
                if (tokenKey.equals("start") || tokenKey.equals("starting")) {
                    startDates.add(tokenValue);
                }
                if (tokenKey.equals("end") || tokenKey.equals("ending")) {
                    endDates.add(tokenValue);
                }
                originalCreate.setAttribute(tokenKey, tokenValue);

                tokens.checkNext(ScheduleLexer.COMMA); // skip comma
            }
        }

        // check for use of standard recurring pattern
        if (tokens.checkNext(ScheduleLexer.REPEAT)) {
            String frequency = this.stripQuotations(tokens.getNext().getText().strip());
            originalCreate.setAttribute("repeat", "true");
            originalCreate.setAttribute("frequency", frequency);

            if (tokens.checkNext(ScheduleLexer.UNTIL)) {
                String untilValue = this.stripQuotations(tokens.getNext().getText().strip());
                originalCreate.setAttribute("until", untilValue);
            }
        }
    }

    private ArrayList<Create> generateCreateRequests(Create originalCreate, ArrayList<String> startDates, ArrayList<String> endDates) throws MismatchedTokenException {
        ArrayList<Create> createRequests = new ArrayList<>();
        int numStartDates = startDates.size();
        int numEndDates = endDates.size();
        if (numStartDates != numEndDates) {
            // TODO: this exception type will have to be reviewed and possibly revised
            throw new MismatchedTokenException();
        }
        createRequests.add(originalCreate);
        // this allows for user defined recurring patterns
        for (int i = (numStartDates - 2); i >= 0; i--) {
            // Create a copy of the object, with the same key-value entries
            Create recurringObj = new Create(); // This is our new Value
            recurringObj.clone(originalCreate); // Our clone will update all hashMap values existing in recurringObj with vals from obj
            recurringObj.setAttribute("start", startDates.get(i));
            recurringObj.setAttribute("end", endDates.get(i));
            createRequests.add(recurringObj);
        }
        tokens.expectNext(ScheduleLexer.END);
        return createRequests;
    }
}
