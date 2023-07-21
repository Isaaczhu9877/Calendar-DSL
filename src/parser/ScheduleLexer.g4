lexer grammar ScheduleLexer;

// DEFAULT_MODE -> if empty title/start/end then 1 hour duration starting now, title is 'My Event'
START: 'Hey Scheduler!' | 'hey scheduler!' WS*;
PROGRAM_END: 'Thanks!' | 'thanks!' WS*;
END: 'please!' | 'Please!' WS*;
CREATE_START: 'create event' WS*;
CHANGE_START: 'change event' WS*;
DELETE_START: 'delete event' WS*;
TO: 'to' WS*;
REPLACE: 'replace' WS*;
COMMA: ',' WS*;

// REPEATING FUNCTIONALITY
REPEAT: 'repeat' WS*;
FREQ: ('daily' | 'weekly' | 'monthly' | 'yearly') WS*;
UNTIL: 'until' WS* -> mode(ATTRIBUTE_MODE);

// ATTRIBUTE FUNCTIONALITY
ATTRIBUTE_KEY: ATTRIBUTE_KEYS WS* -> mode(ATTRIBUTE_MODE);
ATTRIBUTE_KEYS: ('title' | 'location' | 'start' | 'end' | 'participants' | 'description' |
'titled' | 'located' | 'starting' | 'ending' | 'described' | 'participating' | 'last modified' | 'organizer');

WS: [\r\n\t ]+ -> channel(HIDDEN);

// ATTRIBUTE_MODE -> enter to write an attribute upon hitting attribute key
mode ATTRIBUTE_MODE;
PHRASE: QUOTATIONS TEXT QUOTATIONS -> mode(DEFAULT_MODE);
QUOTATIONS: '"';
TEXT: [a-zA-Z0-9 ]+ WS*;





