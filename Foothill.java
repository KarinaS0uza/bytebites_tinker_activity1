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
     * @param name the customer's name
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
     * @param name the new name, which must have a valid length and must not
     *             start with a space
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
     * @param index the position of the order, starting at 0
     * @return the order, or null if the position does not hold an order
     */
    public Order getOrder(int index)
    {
        if (index < 0 || index >= orderCount)
            return null;

        return purchaseHistory[index];
    }

    /**
     * Records an order in the purchase history. An order is rejected if it
     * has no items or is already in the history.
     *
     * @param order the order to record, which must not be null
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
     * @param menu     the menu to browse, which must not be null
     * @param category the category to look for, such as "Drinks"
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
     * Checks whether the customer counts as a real user. A customer is
     * verified once at least one order is in the purchase history.
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
 * the menu can then return the items that belong to one category. The menu
 * keeps its own items and does not change them.
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
     * @param index the position of the item, starting at 0
     * @return the item, or null if the position does not hold an item
     */
    public MenuItem getItem(int index)
    {
        if (index < 0 || index >= itemCount)
            return null;

        return items[index];
    }

    /**
     * Adds an item to the menu. An item is rejected if another item on the
     * menu already has the same name, ignoring capitalization.
     *
     * @param item the item to add, which must not be null
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
     * Finds the items that belong to a category, ignoring capitalization.
     *
     * @param category the category to look for, such as "Drinks"
     * @return the matching items, or an array with no items if nothing matches
     *         or the category is null
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
}

/**
 * One object of this class represents one food item on the menu. Every
 * mutator validates its parameter and reports success or failure, and the
 * constructor calls those mutators so a new object never holds invalid data.
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
     * @param name             the item's name
     * @param price            the item's price in dollars
     * @param category         the item's category, such as "Drinks"
     * @param popularityRating the item's popularity rating
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
     * @param name the new name, which must have a valid length and must not
     *             start with a space
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
     * @param price the new price in dollars, which must be within the allowed
     *              range
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
     * @param category the new category, which must have a valid length and
     *                 must not start with a space
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
     * @param popularityRating the new rating, which must be within the allowed
     *                         range
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
 * One object of this class represents one transaction, the items a user
 * picked. Items are added one at a time, and the total cost is computed from
 * whatever has been added so far. The order holds the same MenuItem objects the
 * menu holds, not copies of them.
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
     * @param index the position of the item, starting at 0
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
     * @param item the item to add, which must not be null
     * @return true if the item was added, false if it was rejected or the
     *         order is full
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
     * Computes the total cost of the items in the order.
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
 * The client class for ByteBites. It will create the objects and do all the
 * input and output.
 *
 * @author Karina Souza
 */
public class Foothill
{
    /**
     * Runs the ByteBites demo.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
    }
}
