import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * One object of this class represents the ByteBites window. The user picks a
 * category, optionally sorts by popularity, adds menu items to an order, and
 * places the order for a customer. The window holds no ByteBites rules of its
 * own and asks the backend classes to do all of the work.
 *
 * @author Karina Souza
 */
public class ByteBitesGui implements ActionListener
{
    private static final String ALL_CATEGORIES = "All";
    private static final String[] CATEGORIES = { ALL_CATEGORIES, "Entrees",
            "Drinks", "Desserts", "Sides" };
    private static final String CUSTOMER_NAME = "Karina Souza";
    private static final int WINDOW_WIDTH = 760;
    private static final int WINDOW_HEIGHT = 440;

    private Menu menu;
    private Customer customer;
    private Order currentOrder;
    private MenuItem[] shownItems;
    private NumberFormat money;

    private JFrame frame;
    private JComboBox<String> categoryBox;
    private JCheckBox sortBox;
    private DefaultListModel<String> menuModel;
    private JList<String> menuList;
    private DefaultListModel<String> orderModel;
    private JButton addButton;
    private JButton placeButton;
    private JButton clearButton;
    private JLabel totalLabel;
    private JLabel statusLabel;

    /**
     * Builds the window, the mock menu and a customer with no orders.
     */
    public ByteBitesGui()
    {
        JPanel filterPanel;
        JPanel menuPanel;
        JPanel orderPanel;
        JPanel orderBottom;
        JPanel orderButtons;
        JPanel centerPanel;

        money = NumberFormat.getCurrencyInstance(Locale.US);
        menu = buildMenu();
        customer = new Customer(CUSTOMER_NAME);
        currentOrder = new Order();

        categoryBox = new JComboBox<String>(CATEGORIES);
        categoryBox.addActionListener(this);
        sortBox = new JCheckBox("Sort by popularity");
        sortBox.addActionListener(this);
        filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Category:"));
        filterPanel.add(categoryBox);
        filterPanel.add(sortBox);

        menuModel = new DefaultListModel<String>();
        menuList = new JList<String>(menuModel);
        addButton = new JButton("Add to order");
        addButton.addActionListener(this);
        menuPanel = new JPanel(new BorderLayout());
        menuPanel.add(new JLabel("Menu"), BorderLayout.NORTH);
        menuPanel.add(new JScrollPane(menuList), BorderLayout.CENTER);
        menuPanel.add(addButton, BorderLayout.SOUTH);

        orderModel = new DefaultListModel<String>();
        totalLabel = new JLabel();
        placeButton = new JButton("Place order");
        placeButton.addActionListener(this);
        clearButton = new JButton("Clear order");
        clearButton.addActionListener(this);
        orderButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        orderButtons.add(placeButton);
        orderButtons.add(clearButton);
        orderBottom = new JPanel(new GridLayout(2, 1));
        orderBottom.add(totalLabel);
        orderBottom.add(orderButtons);
        orderPanel = new JPanel(new BorderLayout());
        orderPanel.add(new JLabel("Current order"), BorderLayout.NORTH);
        orderPanel.add(new JScrollPane(new JList<String>(orderModel)),
                BorderLayout.CENTER);
        orderPanel.add(orderBottom, BorderLayout.SOUTH);

        centerPanel = new JPanel(new GridLayout(1, 2));
        centerPanel.add(menuPanel);
        centerPanel.add(orderPanel);

        statusLabel = new JLabel();
        frame = new JFrame("ByteBites");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(filterPanel, BorderLayout.NORTH);
        frame.add(centerPanel, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.SOUTH);
        frame.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        frame.setLocationRelativeTo(null);

        refreshMenu();
        updateTotal();
        updateStatus("Pick an item and press Add to order.");
    }

    /**
     * Shows the window.
     */
    public void open()
    {
        frame.setVisible(true);
    }

    /**
     * Reacts to a click on a button or a change to the category or sort box.
     *
     * @param event the event that was triggered
     */
    public void actionPerformed(ActionEvent event)
    {
        Object source;

        source = event.getSource();
        if (source == addButton)
            addSelectedItem();
        else if (source == placeButton)
            placeCurrentOrder();
        else if (source == clearButton)
        {
            startNewOrder();
            updateStatus("Order cleared.");
        }
        else
            refreshMenu();
    }

    /**
     * Starts the prototype.
     *
     * @param args not used
     */
    public static void main(String[] args)
    {
        ByteBitesGui gui;

        gui = new ByteBitesGui();
        gui.open();
    }

    private static Menu buildMenu()
    {
        Menu newMenu;

        newMenu = new Menu();
        newMenu.addItem(new MenuItem("Spicy Burger", 8.50, "Entrees", 4.7));
        newMenu.addItem(new MenuItem("Large Soda", 2.75, "Drinks", 4.2));
        newMenu.addItem(new MenuItem("Chocolate Cake", 4.25, "Desserts", 4.5));
        newMenu.addItem(new MenuItem("Iced Tea", 3.00, "Drinks", 4.7));
        newMenu.addItem(new MenuItem("Curly Fries", 3.50, "Sides", 3.9));
        newMenu.addItem(new MenuItem("Vanilla Sundae", 3.95, "Desserts", 4.1));
        return newMenu;
    }

    private void refreshMenu()
    {
        int itemIndex;
        String category;
        Menu filterMenu;
        MenuItem[] source;

        category = (String) categoryBox.getSelectedItem();
        if (sortBox.isSelected())
            source = menu.sortByPopularity();
        else
        {
            source = new MenuItem[menu.getItemCount()];
            for (itemIndex = 0; itemIndex < source.length; itemIndex++)
                source[itemIndex] = menu.getItem(itemIndex);
        }

        if (category.equals(ALL_CATEGORIES))
            shownItems = source;
        else
        {
            filterMenu = new Menu();
            for (itemIndex = 0; itemIndex < source.length; itemIndex++)
                filterMenu.addItem(source[itemIndex]);
            shownItems = filterMenu.filterByCategory(category);
        }

        menuModel.clear();
        for (itemIndex = 0; itemIndex < shownItems.length; itemIndex++)
            menuModel.addElement(describe(shownItems[itemIndex]));
    }

    private void addSelectedItem()
    {
        int selectedIndex;

        selectedIndex = menuList.getSelectedIndex();
        if (selectedIndex < 0 || selectedIndex >= shownItems.length)
            updateStatus("Select a menu item first.");
        else if (currentOrder.addItem(shownItems[selectedIndex]))
        {
            orderModel.addElement(shownItems[selectedIndex].getName() + "  "
                    + money.format(shownItems[selectedIndex].getPrice()));
            updateTotal();
            updateStatus("Added " + shownItems[selectedIndex].getName() + ".");
        }
        else
            updateStatus("The order is full.");
    }

    private void placeCurrentOrder()
    {
        if (customer.placeOrder(currentOrder))
        {
            startNewOrder();
            updateStatus("Order placed.");
        }
        else
            updateStatus("Order not placed. Add at least one item first.");
    }

    private void startNewOrder()
    {
        currentOrder = new Order();
        orderModel.clear();
        updateTotal();
    }

    private void updateTotal()
    {
        totalLabel.setText("Total: " + money.format(currentOrder.calculateTotal()));
    }

    private void updateStatus(String message)
    {
        String verified;

        if (customer.verifyUser())
            verified = "yes";
        else
            verified = "no";
        statusLabel.setText(message + "    [Customer: " + customer.getName()
                + " | Orders placed: " + customer.getOrderCount()
                + " | Verified: " + verified + "]");
    }

    private String describe(MenuItem item)
    {
        return item.getName() + "  |  " + item.getCategory() + "  |  "
                + money.format(item.getPrice()) + "  |  rating "
                + item.getPopularityRating();
    }
}
