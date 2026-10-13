/*
 * ByteBites backend: summary of the four classes (see bytebites_design.mmd)
 *
 * Customer - one app user. Holds a name and an array of past Orders (purchase
 *            history). verifyUser() is true if there is at least one order.
 * Menu     - the full collection of MenuItems, stored in an array. Adds items
 *            and filters them by category such as "Drinks" or "Desserts", or
 *            ranks them by popularity.
 * MenuItem - one food item with a name, price, category and popularity
 *            rating. Every field is validated by its mutator.
 * Order    - one transaction. Holds the MenuItems a user picked, in an array,
 *            and computes the total cost.
 *
 * Relationships: a Customer places many Orders and browses a Menu; a Menu
 * contains MenuItems; an Order includes MenuItems.
 */

import java.text.*;
import java.util.*;

/**
 * One object of this class represents one ByteBites app user. Orders are
 * recorded in the customer's purchase history as they are placed, and the
 * customer counts as verified once that history holds at least one order. A
 * customer can also browse a menu that is passed in, without keeping it.
 *
 * @author Karina Souza
 */
class Customer
{
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 40;
    public static final String DEFAULT_NAME = "Unnamed Customer";
    public static final int MAX_ORDERS = 100;

    private String name;
    private Order[] purchaseHistory;
    private int orderCount;

    /**
     * Creates a customer with no past orders. A rejected name is replaced by
     * the default name.
     *
     * @param name
     *            the customer's name
     */
    public Customer(String name)
    {
        this.purchaseHistory = new Order[MAX_ORDERS];
        this.orderCount = 0;
        if (!setName(name))
            this.name = DEFAULT_NAME;
    }

    /**
     * Returns the customer's name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the customer's name.
     *
     * @param name
     *            the new name, which must have a valid length and must not
     *            start with a space
     * @return true if the name was set, false if it was rejected
     */
    public boolean setName(String name)
    {
        if (name == null || name.length() < MIN_NAME_LENGTH
                || name.length() > MAX_NAME_LENGTH || name.charAt(0) == ' ')
            return false;

        this.name = name;
        return true;
    }

    /**
     * Returns how many orders are in the purchase history.
     *
     * @return the number of orders
     */
    public int getOrderCount()
    {
        return orderCount;
    }

    /**
     * Returns the order at a position in the purchase history.
     *
     * @param index
     *            the position of the order, starting at 0
     * @return the order, or null if the position does not hold an order
     */
    public Order getOrder(int index)
    {
        if (index < 0 || index >= orderCount)
            return null;

        return purchaseHistory[index];
    }

    /**
     * Records an order in the purchase history. An order is rejected if it has
     * no items or is already in the history.
     *
     * @param order
     *            the order to record, which must not be null
     * @return true if the order was recorded, false if it was rejected or the
     *         history is full
     */
    public boolean placeOrder(Order order)
    {
        boolean alreadyPlaced;
        int orderIndex;

        if (order == null || order.getItemCount() == 0
                || orderCount >= MAX_ORDERS)
            return false;

        alreadyPlaced = false;
        for (orderIndex = 0; orderIndex < orderCount; orderIndex++)
            if (purchaseHistory[orderIndex] == order)
                alreadyPlaced = true;

        if (alreadyPlaced)
            return false;

        purchaseHistory[orderCount] = order;
        orderCount++;
        return true;
    }

    /**
     * Finds the items in one category of a menu.
     *
     * @param menu
     *            the menu to browse, which must not be null
     * @param category
     *            the category to look for, such as "Drinks"
     * @return the matching items, or an array with no items if nothing matches
     *         or the menu or category is null
     */
    public MenuItem[] browseMenu(Menu menu, String category)
    {
        if (menu == null)
            return new MenuItem[0];

        return menu.filterByCategory(category);
    }

    /**
     * Checks whether the customer counts as a real user. A customer is verified
     * once at least one order is in the purchase history.
     *
     * @return true if the customer has placed at least one order
     */
    public boolean verifyUser()
    {
        return orderCount > 0;
    }
}

/**
 * One object of this class represents the full collection of items ByteBites
 * sells. Items are added one at a time, with no two items sharing a name, and
 * the menu can then return the items that belong to one category, or all of its
 * items ranked by popularity. The menu keeps its own items and does not change
 * them.
 *
 * @author Karina Souza
 */
class Menu
{
    public static final int MAX_ITEMS = 50;

    private MenuItem[] items;
    private int itemCount;

    /**
     * Creates an empty menu.
     */
    public Menu()
    {
        this.items = new MenuItem[MAX_ITEMS];
        this.itemCount = 0;
    }

    /**
     * Returns how many items are on the menu.
     *
     * @return the number of items
     */
    public int getItemCount()
    {
        return itemCount;
    }

    /**
     * Returns the item at a position on the menu.
     *
     * @param index
     *            the position of the item, starting at 0
     * @return the item, or null if the position does not hold an item
     */
    public MenuItem getItem(int index)
    {
        if (index < 0 || index >= itemCount)
            return null;

        return items[index];
    }

    /**
     * Adds an item to the menu. An item is rejected if another item on the menu
     * already has the same name, ignoring capitalization.
     *
     * @param item
     *            the item to add, which must not be null
     * @return true if the item was added, false if it was rejected or the menu
     *         is full
     */
    public boolean addItem(MenuItem item)
    {
        boolean alreadyListed;
        int itemIndex;

        if (item == null || itemCount >= MAX_ITEMS)
            return false;

        alreadyListed = false;
        for (itemIndex = 0; itemIndex < itemCount; itemIndex++)
            if (items[itemIndex].getName().equalsIgnoreCase(item.getName()))
                alreadyListed = true;

        if (alreadyListed)
            return false;

        items[itemCount] = item;
        itemCount++;
        return true;
    }

    /**
     * Finds the items that belong to a category. Capitalization is ignored, but
     * spaces are not, so "Drinks " does not match "Drinks". The matching items
     * are the menu's own objects, not copies, and they appear in the same order
     * as on the menu.
     *
     * @param category
     *            the category to look for, such as "Drinks"
     * @return the matching items, or an array with no items if nothing matches,
     *         the menu is empty, or the category is null
     */
    public MenuItem[] filterByCategory(String category)
    {
        int itemIndex;
        int matchCount;
        int matchIndex;
        MenuItem[] matchingItems;

        if (category == null)
            return new MenuItem[0];

        matchCount = 0;
        for (itemIndex = 0; itemIndex < itemCount; itemIndex++)
            if (items[itemIndex].getCategory().equalsIgnoreCase(category))
                matchCount++;

        matchingItems = new MenuItem[matchCount];
        matchIndex = 0;
        for (itemIndex = 0; itemIndex < itemCount; itemIndex++)
            if (items[itemIndex].getCategory().equalsIgnoreCase(category))
            {
                matchingItems[matchIndex] = items[itemIndex];
                matchIndex++;
            }

        return matchingItems;
    }

    /**
     * Ranks the items on the menu from the highest popularity rating to the
     * lowest. Items with the same rating stay in the order they were added. The
     * menu itself is not reordered, and the returned items are the menu's own
     * objects, not copies.
     *
     * @return a new array of the menu's items in popularity order, or an array
     *         with no items if the menu is empty
     */
    public MenuItem[] sortByPopularity()
    {
        int itemIndex;
        int passIndex;
        MenuItem[] sortedItems;

        sortedItems = new MenuItem[itemCount];
        for (itemIndex = 0; itemIndex < itemCount; itemIndex++)
            sortedItems[itemIndex] = items[itemIndex];

        for (passIndex = 0; passIndex < sortedItems.length; passIndex++)
            if (!floatLowestRatingToEnd(sortedItems,
                    sortedItems.length - 1 - passIndex))
                return sortedItems;

        return sortedItems;
    }

    /**
     * Makes one pass over the first part of an array, swapping neighbors that
     * are out of order so the lowest rating in that part moves to its end.
     *
     * @param data
     *            the items to reorder, none of which may be null
     * @param end
     *            the last position that the pass compares, which must be less
     *            than the length of the array
     * @return true if any items were swapped, false if the part was in order
     */
    private static boolean floatLowestRatingToEnd(MenuItem[] data, int end)
    {
        boolean changed;
        int itemIndex;
        MenuItem temp;

        changed = false;
        for (itemIndex = 0; itemIndex < end; itemIndex++)
            if (data[itemIndex].getPopularityRating() < data[itemIndex + 1]
                    .getPopularityRating())
            {
                temp = data[itemIndex];
                data[itemIndex] = data[itemIndex + 1];
                data[itemIndex + 1] = temp;
                changed = true;
            }
        return changed;
    }
}

/**
 * One object of this class represents one food item on the menu. Every mutator
 * validates its parameter and reports success or failure, and the constructor
 * calls those mutators so a new object never holds invalid data.
 *
 * @author Karina Souza
 */
class MenuItem
{
    public static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 40;
    public static final int MIN_CATEGORY_LENGTH = 1;
    public static final int MAX_CATEGORY_LENGTH = 20;
    public static final double MIN_PRICE = 0.00;
    public static final double MAX_PRICE = 100.00;
    public static final double MIN_POPULARITY_RATING = 0.0;
    public static final double MAX_POPULARITY_RATING = 5.0;

    public static final String DEFAULT_NAME = "Unnamed Item";
    public static final double DEFAULT_PRICE = 0.00;
    public static final String DEFAULT_CATEGORY = "Uncategorized";
    public static final double DEFAULT_POPULARITY_RATING = 0.0;

    private String name;
    private double price;
    private String category;
    private double popularityRating;

    /**
     * Creates a menu item. Any parameter that is rejected is replaced by its
     * default value.
     *
     * @param name
     *            the item's name
     * @param price
     *            the item's price in dollars
     * @param category
     *            the item's category, such as "Drinks"
     * @param popularityRating
     *            the item's popularity rating
     */
    public MenuItem(String name, double price, String category,
            double popularityRating)
    {
        if (!setName(name))
            this.name = DEFAULT_NAME;
        if (!setPrice(price))
            this.price = DEFAULT_PRICE;
        if (!setCategory(category))
            this.category = DEFAULT_CATEGORY;
        if (!setPopularityRating(popularityRating))
            this.popularityRating = DEFAULT_POPULARITY_RATING;
    }

    /**
     * Returns the item's name.
     *
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * Returns the item's price.
     *
     * @return the price in dollars
     */
    public double getPrice()
    {
        return price;
    }

    /**
     * Returns the item's category.
     *
     * @return the category
     */
    public String getCategory()
    {
        return category;
    }

    /**
     * Returns the item's popularity rating.
     *
     * @return the popularity rating
     */
    public double getPopularityRating()
    {
        return popularityRating;
    }

    /**
     * Sets the item's name.
     *
     * @param name
     *            the new name, which must have a valid length and must not
     *            start with a space
     * @return true if the name was set, false if it was rejected
     */
    public boolean setName(String name)
    {
        if (name == null || name.length() < MIN_NAME_LENGTH
                || name.length() > MAX_NAME_LENGTH || name.charAt(0) == ' ')
            return false;

        this.name = name;
        return true;
    }

    /**
     * Sets the item's price.
     *
     * @param price
     *            the new price in dollars, which must be within the allowed
     *            range
     * @return true if the price was set, false if it was rejected
     */
    public boolean setPrice(double price)
    {
        if (price >= MIN_PRICE && price <= MAX_PRICE)
        {
            this.price = price;
            return true;
        }
        return false;
    }

    /**
     * Sets the item's category.
     *
     * @param category
     *            the new category, which must have a valid length and must not
     *            start with a space
     * @return true if the category was set, false if it was rejected
     */
    public boolean setCategory(String category)
    {
        if (category == null || category.length() < MIN_CATEGORY_LENGTH
                || category.length() > MAX_CATEGORY_LENGTH
                || category.charAt(0) == ' ')
            return false;

        this.category = category;
        return true;
    }

    /**
     * Sets the item's popularity rating.
     *
     * @param popularityRating
     *            the new rating, which must be within the allowed range
     * @return true if the rating was set, false if it was rejected
     */
    public boolean setPopularityRating(double popularityRating)
    {
        if (popularityRating >= MIN_POPULARITY_RATING
                && popularityRating <= MAX_POPULARITY_RATING)
        {
            this.popularityRating = popularityRating;
            return true;
        }
        return false;
    }
}

/**
 * One object of this class represents one transaction, the items a user picked.
 * Items are added one at a time, and the total cost is computed from whatever
 * has been added so far. The order holds the same MenuItem objects the menu
 * holds, not copies of them.
 *
 * @author Karina Souza
 */
class Order
{
    public static final int MAX_ITEMS = 50;

    private MenuItem[] items;
    private int itemCount;

    /**
     * Creates an empty order.
     */
    public Order()
    {
        this.items = new MenuItem[MAX_ITEMS];
        this.itemCount = 0;
    }

    /**
     * Returns how many items have been added to the order.
     *
     * @return the number of items
     */
    public int getItemCount()
    {
        return itemCount;
    }

    /**
     * Returns the item at a position in the order.
     *
     * @param index
     *            the position of the item, starting at 0
     * @return the item, or null if the position does not hold an item
     */
    public MenuItem getItem(int index)
    {
        if (index < 0 || index >= itemCount)
            return null;

        return items[index];
    }

    /**
     * Adds an item to the order. The same item may be added more than once.
     *
     * @param item
     *            the item to add, which must not be null
     * @return true if the item was added, false if it was rejected or the order
     *         is full
     */
    public boolean addItem(MenuItem item)
    {
        if (item == null || itemCount >= MAX_ITEMS)
            return false;

        items[itemCount] = item;
        itemCount++;
        return true;
    }

    /**
     * Computes the total cost of the items in the order, using each item's
     * current price. An item added more than once is counted each time. The
     * total is not rounded, so the client should format it for display.
     *
     * @return the total in dollars, which is 0 for an empty order
     */
    public double calculateTotal()
    {
        double totalCost;
        int itemIndex;

        totalCost = 0.0;
        for (itemIndex = 0; itemIndex < itemCount; itemIndex++)
            totalCost += items[itemIndex].getPrice();

        return totalCost;
    }
}

/**
 * The client class for ByteBites. It creates the objects, runs the demo, and
 * does all the input and output.
 *
 * @author Karina Souza
 */
public class Foothill
{
    /**
     * Runs a small ByteBites scenario, followed by additional menu, order,
     * customer, and invalid-input checks.
     *
     * @param args
     *            not used
     */
    public static void main(String[] args)
    {
        int itemIndex;
        NumberFormat money;
        Menu menu;
        MenuItem burger;
        MenuItem soda;
        MenuItem cake;
        MenuItem tea;
        MenuItem fries;
        MenuItem sundae;
        MenuItem defaultItem;
        MenuItem[] found;
        Order order;
        Order emptyOrder;
        Customer customer;
        Customer unnamedCustomer;

        runSmallScenario();
        System.out.println("\n=== Additional method checks ===");

        money = NumberFormat.getCurrencyInstance(Locale.US);

        System.out.println("=== Building the menu ===");
        menu = new Menu();
        burger = new MenuItem("Spicy Burger", 8.50, "Entrees", 4.7);
        soda = new MenuItem("Large Soda", 2.75, "Drinks", 4.2);
        cake = new MenuItem("Chocolate Cake", 4.25, "Desserts", 4.5);
        tea = new MenuItem("Iced Tea", 3.00, "Drinks", 4.7);
        fries = new MenuItem("Curly Fries", 3.50, "Sides", 3.9);
        sundae = new MenuItem("Vanilla Sundae", 3.95, "Desserts", 4.1);
        System.out.println("add burger: " + menu.addItem(burger));
        System.out.println("add soda: " + menu.addItem(soda));
        System.out.println("add cake: " + menu.addItem(cake));
        System.out.println("add tea: " + menu.addItem(tea));
        System.out.println("add fries: " + menu.addItem(fries));
        System.out.println("add sundae: " + menu.addItem(sundae));
        System.out.println(
                "add same item again (expect false): " + menu.addItem(burger));
        System.out.println(
                "add same name, other capitals (expect false): " + menu.addItem(
                        new MenuItem("SPICY BURGER", 9.00, "Entrees", 4.0)));
        System.out.println("add null (expect false): " + menu.addItem(null));
        System.out.println("Whole menu in the order added: "
                + menu.getItemCount() + " item(s)");
        for (itemIndex = 0; itemIndex < menu.getItemCount(); itemIndex++)
            System.out.println("    " + menu.getItem(itemIndex).getName());

        System.out.println();
        System.out.println("=== Filtering by category ===");
        found = menu.filterByCategory("drinks");
        showItems("Category \"drinks\" (capitals ignored)", found, money);
        found = menu.filterByCategory("Desserts");
        showItems("Category \"Desserts\"", found, money);
        found = menu.filterByCategory("Salads");
        showItems("Category \"Salads\" (none expected)", found, money);
        found = menu.filterByCategory(null);
        showItems("Category null (none expected)", found, money);

        System.out.println();
        System.out.println("=== Sorting by popularity ===");
        showItems("Highest to lowest (Spicy Burger stays before Iced Tea)",
                menu.sortByPopularity(), money);
        System.out.println("Menu order unchanged, first item is still: "
                + menu.getItem(0).getName());

        System.out.println();
        System.out.println("=== Invalid data ===");
        defaultItem = new MenuItem("", -1.0, "", 9.9);
        System.out.println(
                "Bad constructor values became: " + defaultItem.getName() + ", "
                        + money.format(defaultItem.getPrice()) + ", "
                        + defaultItem.getCategory() + ", rating "
                        + defaultItem.getPopularityRating());
        System.out
                .println("setPrice(-5) (expect false): " + burger.setPrice(-5));
        System.out.println("setPrice(Double.NaN) (expect false): "
                + burger.setPrice(Double.NaN));
        System.out.println("setPopularityRating(9) (expect false): "
                + burger.setPopularityRating(9));
        System.out.println("setName(\" Burger\") starts with space (expect "
                + "false): " + burger.setName(" Burger"));
        System.out.println(
                "setPrice(9.25) (expect true): " + burger.setPrice(9.25));
        System.out
                .println("menu.getItem(99) (expect null): " + menu.getItem(99));
        // The valid setPrice(9.25) test changed the burger's price, so restore
        // the original 8.50 before the order totals below.
        burger.setPrice(8.50);

        System.out.println();
        System.out.println("=== Computing an order total ===");
        order = new Order();
        emptyOrder = new Order();
        System.out.println("Empty order total: "
                + money.format(emptyOrder.calculateTotal()));
        System.out.println("add burger: " + order.addItem(burger));
        System.out.println("add soda: " + order.addItem(soda));
        System.out.println("add cake: " + order.addItem(cake));
        System.out.println("add null (expect false): " + order.addItem(null));
        System.out.println("Items in order: " + order.getItemCount());
        System.out.println("Total (expect $15.50): "
                + money.format(order.calculateTotal()));
        System.out.println("add soda a second time: " + order.addItem(soda));
        System.out.println("Total with a second soda (expect $18.25): "
                + money.format(order.calculateTotal()));

        System.out.println();
        System.out.println("=== Customer ===");
        customer = new Customer("Karina Souza");
        unnamedCustomer = new Customer("");
        System.out.println("Name: " + customer.getName());
        System.out.println("Bad name became: " + unnamedCustomer.getName());
        System.out.println("Verified before any order (expect false): "
                + customer.verifyUser());
        System.out.println("place empty order (expect false): "
                + customer.placeOrder(emptyOrder));
        System.out.println("place null order (expect false): "
                + customer.placeOrder(null));
        System.out.println("place real order (expect true): "
                + customer.placeOrder(order));
        System.out.println("place same order again (expect false): "
                + customer.placeOrder(order));
        System.out.println("Orders in history: " + customer.getOrderCount());
        System.out.println("Verified after an order (expect true): "
                + customer.verifyUser());
        System.out
                .println("getOrder(5) (expect null): " + customer.getOrder(5));
        showItems("Customer browses \"Drinks\"",
                customer.browseMenu(menu, "Drinks"), money);
        showItems("Customer browses a null menu (none expected)",
                customer.browseMenu(null, "Drinks"), money);
    }

    /**
     * Displays a title followed by each item in an array.
     *
     * @param title
     *            the heading to show
     * @param list
     *            the items to show, which may have no items
     * @param money
     *            the formatter used to show prices
     */
    private static void showItems(String title, MenuItem[] list,
            NumberFormat money)
    {
        int itemIndex;

        System.out.println(title + ": " + list.length + " item(s)");
        for (itemIndex = 0; itemIndex < list.length; itemIndex++)
            System.out.println("    " + list[itemIndex].getName() + " | "
                    + list[itemIndex].getCategory() + " | "
                    + money.format(list[itemIndex].getPrice()) + " | rating "
                    + list[itemIndex].getPopularityRating());
    }

    /**
     * Inspects sample objects before building, sorting, and filtering a menu,
     * then computes and places an order.
     */
    private static void runSmallScenario()
    {
        int itemIndex;
        NumberFormat money = NumberFormat.getCurrencyInstance(Locale.US);
        MenuItem burger = new MenuItem("Spicy Burger", 8.50, "Entrees", 4.7);
        MenuItem soda = new MenuItem("Large Soda", 2.75, "Drinks", 4.2);
        MenuItem cake = new MenuItem("Chocolate Cake", 4.25, "Desserts", 4.5);
        MenuItem tea = new MenuItem("Iced Tea", 3.00, "Drinks", 4.7);
        MenuItem[] sampleItems = {burger, soda, cake, tea};
        MenuItem[] sortedItems;
        Menu menu = new Menu();
        Order order = new Order();
        Customer customer = new Customer("Karina Souza");

        // First inspect the objects before adding anything.
        showItems("Sample menu items", sampleItems, money);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Menu items before adding: " + menu.getItemCount());
        System.out
                .println("Order items before adding: " + order.getItemCount());
        System.out.println(
                "Empty order total: " + money.format(order.calculateTotal()));
        System.out.println(
                "Customer verified before ordering: " + customer.verifyUser());

        System.out.println("\nAdding items to the menu:");
        for (itemIndex = 0; itemIndex < sampleItems.length; itemIndex++)
            System.out.println("    " + sampleItems[itemIndex].getName()
                    + " added: " + menu.addItem(sampleItems[itemIndex]));
        System.out.println("Duplicate burger added (expect false): "
                + menu.addItem(burger));
        System.out.println("Menu item count: " + menu.getItemCount());

        System.out.println();
        sortedItems = menu.sortByPopularity();
        showItems("Menu sorted by popularity", sortedItems, money);
        System.out.println("Original menu order after sorting:");
        for (itemIndex = 0; itemIndex < menu.getItemCount(); itemIndex++)
            System.out.println("    " + menu.getItem(itemIndex).getName());

        System.out.println();
        showItems("Drinks (case ignored)", menu.filterByCategory("drinks"),
                money);
        showItems("Desserts", menu.filterByCategory("Desserts"), money);
        showItems("Salads (no matches expected)",
                menu.filterByCategory("Salads"), money);
        showItems("Customer browses Drinks",
                customer.browseMenu(menu, "Drinks"), money);

        System.out.println("\nAdding items to the order:");
        System.out.println("    Burger added: " + order.addItem(burger));
        System.out.println("    Soda added: " + order.addItem(soda));
        System.out.println("    Cake added: " + order.addItem(cake));
        System.out.println("Order item count: " + order.getItemCount());
        System.out.println("Order total (expect $15.50): "
                + money.format(order.calculateTotal()));
        System.out.println("Order placed: " + customer.placeOrder(order));
        System.out.println("Customer order count: " + customer.getOrderCount());
        System.out.println(
                "Customer verified after ordering: " + customer.verifyUser());
    }
}

/*
Sample menu items: 4 item(s)
    Spicy Burger | Entrees | $8.50 | rating 4.7
    Large Soda | Drinks | $2.75 | rating 4.2
    Chocolate Cake | Desserts | $4.25 | rating 4.5
    Iced Tea | Drinks | $3.00 | rating 4.7
Customer: Karina Souza
Menu items before adding: 0
Order items before adding: 0
Empty order total: $0.00
Customer verified before ordering: false

Adding items to the menu:
    Spicy Burger added: true
    Large Soda added: true
    Chocolate Cake added: true
    Iced Tea added: true
Duplicate burger added (expect false): false
Menu item count: 4

Menu sorted by popularity: 4 item(s)
    Spicy Burger | Entrees | $8.50 | rating 4.7
    Iced Tea | Drinks | $3.00 | rating 4.7
    Chocolate Cake | Desserts | $4.25 | rating 4.5
    Large Soda | Drinks | $2.75 | rating 4.2
Original menu order after sorting:
    Spicy Burger
    Large Soda
    Chocolate Cake
    Iced Tea

Drinks (case ignored): 2 item(s)
    Large Soda | Drinks | $2.75 | rating 4.2
    Iced Tea | Drinks | $3.00 | rating 4.7
Desserts: 1 item(s)
    Chocolate Cake | Desserts | $4.25 | rating 4.5
Salads (no matches expected): 0 item(s)
Customer browses Drinks: 2 item(s)
    Large Soda | Drinks | $2.75 | rating 4.2
    Iced Tea | Drinks | $3.00 | rating 4.7

Adding items to the order:
    Burger added: true
    Soda added: true
    Cake added: true
Order item count: 3
Order total (expect $15.50): $15.50
Order placed: true
Customer order count: 1
Customer verified after ordering: true

=== Additional method checks ===
=== Building the menu ===
add burger: true
add soda: true
add cake: true
add tea: true
add fries: true
add sundae: true
add same item again (expect false): false
add same name, other capitals (expect false): false
add null (expect false): false
Whole menu in the order added: 6 item(s)
    Spicy Burger
    Large Soda
    Chocolate Cake
    Iced Tea
    Curly Fries
    Vanilla Sundae

=== Filtering by category ===
Category "drinks" (capitals ignored): 2 item(s)
    Large Soda | Drinks | $2.75 | rating 4.2
    Iced Tea | Drinks | $3.00 | rating 4.7
Category "Desserts": 2 item(s)
    Chocolate Cake | Desserts | $4.25 | rating 4.5
    Vanilla Sundae | Desserts | $3.95 | rating 4.1
Category "Salads" (none expected): 0 item(s)
Category null (none expected): 0 item(s)

=== Sorting by popularity ===
Highest to lowest (Spicy Burger stays before Iced Tea): 6 item(s)
    Spicy Burger | Entrees | $8.50 | rating 4.7
    Iced Tea | Drinks | $3.00 | rating 4.7
    Chocolate Cake | Desserts | $4.25 | rating 4.5
    Large Soda | Drinks | $2.75 | rating 4.2
    Vanilla Sundae | Desserts | $3.95 | rating 4.1
    Curly Fries | Sides | $3.50 | rating 3.9
Menu order unchanged, first item is still: Spicy Burger

=== Invalid data ===
Bad constructor values became: Unnamed Item, $0.00, Uncategorized, rating 0.0
setPrice(-5) (expect false): false
setPrice(Double.NaN) (expect false): false
setPopularityRating(9) (expect false): false
setName(" Burger") starts with space (expect false): false
setPrice(9.25) (expect true): true
menu.getItem(99) (expect null): null

=== Computing an order total ===
Empty order total: $0.00
add burger: true
add soda: true
add cake: true
add null (expect false): false
Items in order: 3
Total (expect $15.50): $15.50
add soda a second time: true
Total with a second soda (expect $18.25): $18.25

=== Customer ===
Name: Karina Souza
Bad name became: Unnamed Customer
Verified before any order (expect false): false
place empty order (expect false): false
place null order (expect false): false
place real order (expect true): true
place same order again (expect false): false
Orders in history: 1
Verified after an order (expect true): true
getOrder(5) (expect null): null
Customer browses "Drinks": 2 item(s)
    Large Soda | Drinks | $2.75 | rating 4.2
    Iced Tea | Drinks | $3.00 | rating 4.7
Customer browses a null menu (none expected): 0 item(s)
*/
