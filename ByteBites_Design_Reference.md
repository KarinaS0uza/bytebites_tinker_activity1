# ByteBites Reference File

## About This Project
You are building the backend logic for a campus food ordering app called ByteBites
using Java classes and simple algorithms.

## Project Scope
Do not add authentication logic, a database layer, or any features not described 
in the spec.

## Behavioral Instructions
Follow these rules in every suggestion for this project.

**Language and style**
- Write Java only. Never produce code in any other language unless asked.
- Follow every rule in `CS1B_Course_Guidelines.md`. These are course requirements, not style suggestions. When the rules here and that file differ, the guidelines file wins.
- This project is practice for my Java class, so use only concepts taught through Week 3 of `ModuleAandB.md`. Do not use a technique that is not confirmed there. If something outside it seems needed, say so and ask first.
- Allowed concepts:
  - classes, objects, private data, accessors and mutators, constructors (including overloaded, copy and `this(...)`), `this.member`, `static` data and methods, `static final` constants, enums
  - arrays, including `Type[]` and 2-D arrays, with `.length`; linear search, sorting, recursion and binary search
  - `String` methods, `Integer.parseInt()`, `Double.parseDouble()` and `String.valueOf()`
  - `for` loops and `if`/`else`, `boolean` mutators that validate input, `try`/`catch` for `NumberFormatException`
  - `Scanner` or `JOptionPane` for input and `NumberFormat` for output
  - interfaces with `implements`, since they were taught in Week 2R, but only if the spec asks for one
- Store collections in plain arrays, such as `MenuItem[]` with a count, and not in `ArrayList` or other `java.util` collections, because they have not been taught yet.
- Not allowed: inheritance (announced for Week 4 but not yet taught), `ArrayList` and other collections, streams and lambdas, generics, `var`, frameworks (Spring, Lombok), design patterns, annotations, and multithreading.
- Key rules from the course guidelines:
  - indent with 4 spaces and no tabs; explicit types and no `var`; declarations at the top of their block
  - no `while (true)`, labels, `break` or `continue` in loops (`break` only inside a `switch`)
  - descriptive names, with named `UPPER_CASE` constants for sizes, limits and fixed values (for example `MAX_ITEMS`)
  - private fields with accessors and mutators; validate every parameter that sets private data, including constructor parameters
  - every mutator returns `boolean` and never prints; constructors and other data methods don't print either, because the client in `Foothill.main()` handles all input and output
  - JavaDoc beginning with `/**` on every public class and public method, with the class JavaDoc placed after the imports. Unless the class has only static members, its JavaDoc says "One object of this class represents ...". Include an `@author` tag, and do not expose private members.
  - multiplication instead of `Math.pow()` for small powers, and a loop instead of recursion when a loop is clear
  - the main class is named `Foothill`, and a console submission is one source file with the full run pasted at the bottom in a `/* ... */` comment
  - before finishing, test behavior and edge cases by hand. Compiling is not proof that it is done.

**Stay inside the spec**
- Use only the four classes in `bytebites_spec.md`: `Customer`, `Menu`, `MenuItem` and `Order`. Do not add classes such as `OrderItem`, `Restaurant` or `Payment`, even if they would be good design. Mention the idea in one line and wait for approval.
- Give each class only the attributes and behavior the spec states:
  - `Customer`: name, purchase history.
  - `MenuItem`: name, price, category, popularity rating.
  - `Menu`: holds all items and filters them by category. Approved exception: it can also rank them by popularity (`sortByPopularity()`).
  - `Order`: stores the selected items and computes the total cost.
- Do not invent extra fields such as IDs, emails, timestamps, statuses, quantities or delivery details.
- Follow the Project Scope: no authentication logic, no database layer and no features outside the spec.
- If the spec is ambiguous, for example how a customer is "verified", pick the simplest reading, say what you assumed, and ask before building on it.

**Keeping files consistent**
- Treat `bytebites_spec.md` as the source of truth. The diagram (`bytebites_design.mmd`), `plan.md` and any code must agree with it and with each other.
- When one of them changes, update the others in the same step and confirm the relationships and attributes still match.

**How to work**
- Keep changes small: one class or one method at a time, and no unrequested refactors.
- Do not create, delete or overwrite files beyond what was asked. Do not run code or commands that were not requested.
- Briefly explain the reason for each suggestion in plain language, since this is a learning project.
- State what was and wasn't verified. Never claim a diagram renders or code runs unless it was actually checked.
