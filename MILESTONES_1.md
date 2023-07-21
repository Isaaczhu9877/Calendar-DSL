# Informal Summary

1. _Who_ you think would want to use your DSL?

   Office administrators or executive assistants, operating within a larger organization. Or basically anyone who wants to setup complex schedules quickly without having to deal with navigating bloated calendar UIs and endless button clicks

2. _What_ would they use it for?

   They would use the DSL to schedule events and tasks relating to the organization’s employees.

3. _Why_ would it make sense for them to use it instead of, for example, a general purpose programming language?

   An office administrator / executive assistant is not assumed to have sufficient programming knowledge or skills to be able to utilize a general purpose programming language. Hence, this DSL will be designed to be intuitive for someone without a technical background.

4. _What_ are the features that you want to support that add expressivity, as opposed to a general purpose language?
   - A user should be able to create a schedule that contains events
   - A user should be able to create an event
   - Functionality should exist that identifies conflicts between this created event and the schedule
     - An event can be repeating, and have additional attributes/details
     - A user should be able to search for an event within a schedule
     - A user should be able to find all free time slots within a schedule
     - A user should be able to modify an existing schedule
   - Deleting existing events
   - Changing times, descriptions, and other attributes (participants) for an event
   - A user should be able to export their schedule
     - Printing as a pdf, or emailing to someone, etc.
   - Want several key features
     - With regards to loops, we have repeated events
     - For recursion, we can create or edit a task, and then sub-tasks
     - For conditionals, searching for a task on with specific attributes (like on a certain day)

# Responsibilities & Timeline

1. Feature Brainstorming & Design → Sunday, May 22
   - Everyone
2. API Research → Sunday, May 22
   - Aleem, Isaac
3. 1st User Study + Feedback → Tuesday, May 24
  - Rene, Jimmy
4. Lexer/Parser Implementation 1st Iteration (based on User Study 1) → Friday, May 27
   - Aleem, Rene
5. 2st User Study + Feedback → Sunday, May 29
   - Ben, Jimmy
6. Lexer/Parser Implementation 2nd Iteration (based on User Study 2) → Monday, May 30
   - Rene, Aleem
7. Showcase Video → Tuesday, May 31
   - Everyone
8. Due date → June 3

9. Additional Support → Available to provide more help on any of the responsibilities listed above
   - Everyone
