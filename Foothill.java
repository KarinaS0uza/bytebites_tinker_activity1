/*
 * ByteBites backend: summary of the four classes (see bytebites_design.mmd)
 *
 * Customer - one app user. Holds a name and an array of past Orders (purchase
 *            history). verifyUser() is true if there is at least one order.
 * Menu     - the full collection of MenuItems, stored in an array. Adds items
 *            and filters them by category such as "Drinks" or "Desserts".
 * MenuItem - one food item with a name, price, category and popularity
 *            rating. Every field is validated by its mutator.
 * Order    - one transaction. Holds the MenuItems a user picked, in an array,
 *            and computes the total cost.
 *
 * Relationships: a Customer places many Orders and browses a Menu; a Menu
 * contains MenuItems; an Order includes MenuItems.
 */
