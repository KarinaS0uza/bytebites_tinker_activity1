import org.junit.Test;
import org.junit.Assert;

/**
 * One object of this class represents independent ByteBites behavior tests.
 * Each test creates fresh objects and checks their public results against the
 * expected menu or order behavior.
 *
 * @author Karina Souza
 */
public class test_bytebites
{
    private static final double BURGER_PRICE = 10.00;
    private static final double SODA_PRICE = 5.00;
    private static final double CAKE_PRICE = 4.00;
    private static final double TEA_PRICE = 3.00;
    private static final double LOW_RATING = 4.2;
    private static final double MEDIUM_RATING = 4.5;
    private static final double HIGH_RATING = 4.7;
    private static final double EXPECTED_ORDER_TOTAL = 15.00;
    private static final double EXPECTED_REPEATED_TOTAL = 10.00;
    private static final double EMPTY_TOTAL = 0.00;
    private static final double PRICE_TOLERANCE = 0.000001;

    /**
     * Verifies that a $10 burger and a $5 soda produce an order total of $15.
     */
    @Test
    public void testCalculateTotalWithMultipleItems()
    {
        Order order = new Order();
        MenuItem burger = new MenuItem("Spicy Burger", BURGER_PRICE, "Entrees",
                LOW_RATING);
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);

        Assert.assertTrue("The burger must be added for this test.",
                order.addItem(burger));
        Assert.assertTrue("The soda must be added for this test.",
                order.addItem(soda));

        Assert.assertEquals("The burger and soda must total $15.",
                EXPECTED_ORDER_TOTAL, order.calculateTotal(), PRICE_TOLERANCE);
    }

    /**
     * Verifies that an order with no items has a total of $0.
     */
    @Test
    public void testOrderTotalIsZeroWhenEmpty()
    {
        Order order = new Order();

        Assert.assertEquals("An empty order must total $0.", EMPTY_TOTAL,
                order.calculateTotal(), PRICE_TOLERANCE);
    }

    /**
     * Verifies that filtering a mixed menu for Drinks returns only soda and
     * tea.
     */
    @Test
    public void testFilterByCategoryReturnsOnlyMatches()
    {
        Menu menu = new Menu();
        MenuItem burger = new MenuItem("Spicy Burger", BURGER_PRICE, "Entrees",
                LOW_RATING);
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);
        MenuItem cake = new MenuItem("Chocolate Cake", CAKE_PRICE, "Desserts",
                MEDIUM_RATING);
        MenuItem tea = new MenuItem("Iced Tea", TEA_PRICE, "Drinks",
                HIGH_RATING);
        String[] expectedNames = {"Large Soda", "Iced Tea"};
        MenuItem[] matches;

        Assert.assertTrue("The burger must be added for this test.",
                menu.addItem(burger));
        Assert.assertTrue("The soda must be added for this test.",
                menu.addItem(soda));
        Assert.assertTrue("The cake must be added for this test.",
                menu.addItem(cake));
        Assert.assertTrue("The tea must be added for this test.",
                menu.addItem(tea));

        matches = menu.filterByCategory("Drinks");

        Assert.assertNotNull("Filtering must return an array.", matches);
        Assert.assertArrayEquals("Only soda and tea should match Drinks.",
                expectedNames, itemNames(matches));
    }

    /**
     * Verifies that adding the same $5 soda twice produces a total of $10.
     */
    @Test
    public void testOrderTotalCountsRepeatedItems()
    {
        Order order = new Order();
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);

        Assert.assertTrue("The first soda must be added for this test.",
                order.addItem(soda));
        Assert.assertTrue("The repeated soda must be added for this test.",
                order.addItem(soda));

        Assert.assertEquals(
                "Both copies of the soda must count toward the total.",
                EXPECTED_REPEATED_TOTAL, order.calculateTotal(),
                PRICE_TOLERANCE);
    }

    /**
     * Verifies that filtering an empty menu returns an empty array.
     */
    @Test
    public void testFilteringEmptyMenuReturnsEmptyArray()
    {
        Menu menu = new Menu();
        MenuItem[] expectedItems = {};

        Assert.assertArrayEquals("An empty menu must have no matching items.",
                expectedItems, menu.filterByCategory("Drinks"));
    }

    /**
     * Verifies that filtering for an absent category returns an empty array.
     */
    @Test
    public void testFilteringUnknownCategoryReturnsEmptyArray()
    {
        Menu menu = new Menu();
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);
        MenuItem cake = new MenuItem("Chocolate Cake", CAKE_PRICE, "Desserts",
                MEDIUM_RATING);
        MenuItem[] expectedItems = {};

        Assert.assertTrue("The soda must be added for this test.",
                menu.addItem(soda));
        Assert.assertTrue("The cake must be added for this test.",
                menu.addItem(cake));

        Assert.assertArrayEquals("Salads must have no matches in this menu.",
                expectedItems, menu.filterByCategory("Salads"));
    }

    /**
     * Verifies that Drinks and drinks return the same matching items.
     */
    @Test
    public void testCategoryFilterIgnoresCase()
    {
        Menu menu = new Menu();
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);
        MenuItem cake = new MenuItem("Chocolate Cake", CAKE_PRICE, "Desserts",
                MEDIUM_RATING);
        MenuItem tea = new MenuItem("Iced Tea", TEA_PRICE, "Drinks",
                HIGH_RATING);
        String[] expectedNames = {"Large Soda", "Iced Tea"};
        MenuItem[] capitalizedMatches;
        MenuItem[] lowercaseMatches;

        Assert.assertTrue("The soda must be added for this test.",
                menu.addItem(soda));
        Assert.assertTrue("The cake must be added for this test.",
                menu.addItem(cake));
        Assert.assertTrue("The tea must be added for this test.",
                menu.addItem(tea));

        capitalizedMatches = menu.filterByCategory("Drinks");
        lowercaseMatches = menu.filterByCategory("drinks");

        Assert.assertNotNull("Drinks must return an array.",
                capitalizedMatches);
        Assert.assertNotNull("drinks must return an array.", lowercaseMatches);
        Assert.assertArrayEquals("Drinks must return soda and tea.",
                expectedNames, itemNames(capitalizedMatches));
        Assert.assertArrayEquals("Changing case must preserve the matches.",
                expectedNames, itemNames(lowercaseMatches));
    }

    /**
     * Verifies that filtering a populated menu with null returns an empty
     * array.
     */
    @Test
    public void testFilteringNullCategoryReturnsEmptyArray()
    {
        Menu menu = new Menu();
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);
        MenuItem[] expectedItems = {};

        Assert.assertTrue("The soda must be added for this test.",
                menu.addItem(soda));

        Assert.assertArrayEquals(
                "A null category must return no matching items.", expectedItems,
                menu.filterByCategory(null));
    }

    /**
     * Verifies that popularity ratings 4.2, 4.7, and 4.5 sort highest first.
     */
    @Test
    public void testSortByPopularityDescending()
    {
        Menu menu = new Menu();
        MenuItem burger = new MenuItem("Spicy Burger", BURGER_PRICE, "Entrees",
                LOW_RATING);
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);
        MenuItem cake = new MenuItem("Chocolate Cake", CAKE_PRICE, "Desserts",
                MEDIUM_RATING);
        String[] expectedNames = {"Large Soda", "Chocolate Cake",
                "Spicy Burger"};
        MenuItem[] sortedItems;

        Assert.assertTrue("The burger must be added for this test.",
                menu.addItem(burger));
        Assert.assertTrue("The soda must be added for this test.",
                menu.addItem(soda));
        Assert.assertTrue("The cake must be added for this test.",
                menu.addItem(cake));

        sortedItems = menu.sortByPopularity();

        Assert.assertNotNull("Sorting must return an array.", sortedItems);
        Assert.assertArrayEquals("Items must be ranked from highest to lowest.",
                expectedNames, itemNames(sortedItems));
    }

    /**
     * Verifies that sorting an empty menu returns an empty array.
     */
    @Test
    public void testSortingEmptyMenuReturnsEmptyArray()
    {
        Menu menu = new Menu();
        MenuItem[] expectedItems = {};

        Assert.assertArrayEquals("Sorting an empty menu must return no items.",
                expectedItems, menu.sortByPopularity());
    }

    /**
     * Verifies that equally rated items retain their relative insertion order.
     */
    @Test
    public void testEqualPopularityPreservesInsertionOrder()
    {
        Menu menu = new Menu();
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);
        MenuItem cake = new MenuItem("Chocolate Cake", CAKE_PRICE, "Desserts",
                MEDIUM_RATING);
        MenuItem tea = new MenuItem("Iced Tea", TEA_PRICE, "Drinks",
                HIGH_RATING);
        String[] expectedNames = {"Large Soda", "Iced Tea", "Chocolate Cake"};
        MenuItem[] sortedItems;

        Assert.assertTrue("The soda must be added first for this test.",
                menu.addItem(soda));
        Assert.assertTrue(
                "The cake must separate the tied items for this test.",
                menu.addItem(cake));
        Assert.assertTrue("The tea must be added after the soda for this test.",
                menu.addItem(tea));

        sortedItems = menu.sortByPopularity();

        Assert.assertNotNull("Sorting must return an array.", sortedItems);
        Assert.assertArrayEquals("The tied soda must stay before the tea.",
                expectedNames, itemNames(sortedItems));
    }

    /**
     * Verifies that sorting results leaves the original menu order intact.
     */
    @Test
    public void testSortingPreservesOriginalMenuOrder()
    {
        int itemIndex;
        Menu menu = new Menu();
        MenuItem burger = new MenuItem("Spicy Burger", BURGER_PRICE, "Entrees",
                LOW_RATING);
        MenuItem soda = new MenuItem("Large Soda", SODA_PRICE, "Drinks",
                HIGH_RATING);
        MenuItem cake = new MenuItem("Chocolate Cake", CAKE_PRICE, "Desserts",
                MEDIUM_RATING);
        String[] expectedNames = {"Spicy Burger", "Large Soda",
                "Chocolate Cake"};
        String[] actualNames = new String[expectedNames.length];

        Assert.assertTrue("The burger must be added first for this test.",
                menu.addItem(burger));
        Assert.assertTrue("The soda must be added second for this test.",
                menu.addItem(soda));
        Assert.assertTrue("The cake must be added third for this test.",
                menu.addItem(cake));

        menu.sortByPopularity();

        Assert.assertEquals("Sorting must preserve the number of menu items.",
                expectedNames.length, menu.getItemCount());
        for (itemIndex = 0; itemIndex < actualNames.length; itemIndex++)
        {
            Assert.assertNotNull(
                    "Every original menu position must hold an item.",
                    menu.getItem(itemIndex));
            actualNames[itemIndex] = menu.getItem(itemIndex).getName();
        }
        Assert.assertArrayEquals(
                "Sorting must preserve the original menu order.", expectedNames,
                actualNames);
    }

    /**
     * Returns the names of items exposed by a menu operation.
     *
     * @param items
     *            the non-null result of a menu operation
     * @return the item names in the same order as the result
     */
    private static String[] itemNames(MenuItem[] items)
    {
        int itemIndex;
        String[] names = new String[items.length];

        for (itemIndex = 0; itemIndex < items.length; itemIndex++)
            names[itemIndex] = items[itemIndex].getName();

        return names;
    }
}

/*
JUnit version 4.13.2
............
Time: 0.008

OK (12 tests)

*/
