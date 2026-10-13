# ByteBites Tinker Activity 1

The backend logic for ByteBites, a campus food ordering app, written in Java. It uses four classes and plain arrays only.

## Classes

All four classes are in `Foothill.java`.

| Class | Purpose |
|---|---|
| `Customer` | A user's name and purchase history. A customer is verified once at least one order is in the history. |
| `Menu` | The collection of items. Adds items with no duplicate names, filters by category, and ranks by popularity. |
| `MenuItem` | One food item with a name, price, category and popularity rating. Every field is validated. |
| `Order` | The items a user picked, with the total cost. |

## Files

| File | Contents |
|---|---|
| `Foothill.java` | The four classes and the `main()` demo, with the console transcript at the bottom. |
| `test_bytebites.java` | JUnit 4 tests for the backend classes. |
| `ByteBites/src/ByteBitesGui.java` | A Swing window prototype for browsing the menu and placing orders. |
| `bytebites_design.mmd` | The UML class diagram in Mermaid. |
| `ByteBites_Design_Reference.md` | The rules to follow when working on this project. |
| `ByteBites/` | The Eclipse project. |

## Running it in Eclipse

The Eclipse project is the `ByteBites` folder. `Foothill.java` and `test_bytebites.java` in it are links to the files in the repo root.

1. Right-click `Foothill.java` and choose **Run As → Java Application** to run the demo.
2. Right-click `test_bytebites.java` and choose **Run As → JUnit Test** to run the tests.
3. Right-click `ByteBitesGui.java` and choose **Run As → Java Application** to open the window.

If a file doesn't appear in Eclipse, right-click the `ByteBites` project and choose **Refresh**.

## Viewing the front end (GUI prototype)

`ByteBites/src/ByteBitesGui.java` is a quick Swing window that calls the backend classes. It uses the existing backend methods; the UML diagram describes the four backend classes.

To see it:

1. In Eclipse, right-click the `ByteBites` project and choose **Refresh**.
2. Right-click `ByteBitesGui.java` in `ByteBites/src` and choose **Run As → Java Application**.
3. A window titled "ByteBites" opens. If it doesn't, check the Console tab for errors.

What the window does:

- **Category dropdown:** shows only that category (`filterByCategory()`).
- **Sort by popularity checkbox:** ranks the items (`sortByPopularity()`).
- **Add to order:** adds the selected menu item to the current order and updates the total (`calculateTotal()`).
- **Place order:** records the order for the customer (`placeOrder()`). The status line at the bottom then shows "Verified: yes".
- **Clear order:** starts a new, empty order.
