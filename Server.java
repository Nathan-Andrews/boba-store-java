import java.util.*;

import java.awt.*;
import javax.swing.*;

/**
 * Server class handles the operations related to order processing in a GUI.
 * Extends the Page class and is responsible for managing drinks orders.
 */
public class Server extends Page {
    // inherets JPanel panel from Page which is where we display things

    private JPanel drinkBox;
    private JPanel orderBox;
    JLabel costLabel;

    menuHandler h = new menuHandler();

    ArrayList<Drink> drinks = new ArrayList<>();

    // a hashmap that stores the count of each drink
    HashMap<Drink, Integer> currentOrder = new HashMap<>();
    // a hashmap that stores the panels of each drink so that they can be updated and removed
    HashMap<Drink,JPanel> orderBoxes = new HashMap<>();

    private double runningCost = 0;

    Color background = new Color(235,240,245);

    /**
     * Record representing a Drink with a name, price, and id.
     */
    public static record Drink(String name, Double price, Integer id) { }

    /**
     * Adds a drink to the current order and updates the UI.
     *
     * @param drink The drink to be added.
     */
    void addDrinkToOrder(Drink drink) {
        updateDrinkToOrder(drink, 1);
        System.out.println("   added 1 drink " + drink.name());
    }

    /**
     * Removes a drink from the current order and updates the UI.
     *
     * @param drink The drink to be removed.
     */
    void removeDrinkFromOrder(Drink drink) {
        updateDrinkToOrder(drink, -1);
        System.out.println("   removed 1 drink " + drink.name());
    }

    /**
     * Updates the quantity of a specific drink in the current order and
     * updates the UI accordingly.
     *
     * @param drink The drink to be updated.
     * @param increment The amount to increment in the order.
     */
    void updateDrinkToOrder(Drink drink,int increment) {
        String drinkName = drink.name();
        currentOrder.put(drink,(currentOrder.containsKey(drink) ? currentOrder.get(drink) : 0) + increment);
        runningCost += drink.price() * increment; // if we remove drinks or add multiple drinks we want the cost to reflect that

        JPanel horizontalBox;
        if (orderBoxes.containsKey(drink)) {
            horizontalBox = orderBoxes.get(drink);
            horizontalBox.removeAll();
        }
        else {
            horizontalBox = new JPanel();
            horizontalBox.setLayout(new BoxLayout(horizontalBox,BoxLayout.LINE_AXIS));
            horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);
            horizontalBox.setBackground(background);
        }

        // remove if reduced to 0
        if (currentOrder.get(drink) <= 0) {
            horizontalBox.removeAll();
            orderBoxes.remove(drink);
            orderBox.remove(horizontalBox);
            currentOrder.remove(drink);
        }
        else {
            JLabel drinkLabel = new JLabel((drink.price() * currentOrder.get(drink)) + "-------" + drinkName + " x" + currentOrder.get(drink));
            drinkLabel.setFont(font.deriveFont(Font.PLAIN, 16));

            JButton minusButton = new JButton("-");
            minusButton.setFont(font.deriveFont(Font.PLAIN, 20));
            minusButton.addActionListener(e -> {
                removeDrinkFromOrder(drink);
            });

            JButton plusButton = new JButton("+");
            plusButton.setFont(font.deriveFont(Font.PLAIN, 20));
            plusButton.addActionListener(e -> {
                addDrinkToOrder(drink);
            });

            horizontalBox.add(drinkLabel);
            horizontalBox.add(Box.createHorizontalGlue()); // this does some sort of magic to make closeButton align with the right edge
            horizontalBox.add(plusButton);
            horizontalBox.add(minusButton);

            if (!orderBoxes.containsKey(drink)) orderBox.add(horizontalBox);

            orderBoxes.put(drink,horizontalBox);
        }

        costLabel.setText("Total: $" + runningCost);

        // repaint the panel so that the new components are drawn
        panel.revalidate();
        panel.repaint();
    }

    /**
     * Resets the current order, clearing all selected drinks and costs.
     */
    void resetOrder() {
        currentOrder.clear();
        orderBoxes.clear();
        runningCost = 0;

        orderBox.removeAll();

        costLabel.setText("Total: $" + runningCost);

        // repaint the panel so that the new components are drawn
        panel.revalidate();
        panel.repaint();
    }


    /**
     * Completes the current order, outputting the total cost and resetting the order.
     */
    void checkout() {
        System.out.println("checking out...");
        System.out.println("  this order cost $"+runningCost);

        ArrayList<orders.Tuple<Integer, Integer>> items = new ArrayList<>();

        currentOrder.forEach((key, count) -> {
            if (count > 0) {
                items.add(new orders.Tuple<Integer, Integer>(key.id(),count));
                // System.out.println(key.id() + " " + count);
            }
        });
        System.out.println();
        
        if (new orders().addOrder(items))
            System.out.println("Successfully added order .. resetting the frame.");


        resetOrder();
    }

    /**
     * Processes and displays the available menu items in the UI.
     */
    void processMenuItems() {
        h.populateMenuList(drinks);

        for (Drink drink : drinks) {
            JButton b = new JButton(drink.name());
            b.setFont(font.deriveFont(Font.PLAIN, 16));
            b.addActionListener(e -> {
                addDrinkToOrder(drink);
            });
            drinkBox.add(b);
        }
    }

    /**
     * Constructor for the Server class, initializing the UI components and layout.
     *
     * @param g An instance of the GUI class used to manage the visual interface.
     */
    Server(GUI g) {
        super(g);
        
        panel.setBackground(background);

        // a panel that holds all of the buttons to add drinks to 
        drinkBox = new JPanel();
        // creates a grid layout.  With a minimum size of 10
        drinkBox.setLayout(new GridLayout(Math.max(drinks.size(),10),1));
        drinkBox.setBackground(background);
        drinkBox.setBounds(alignCenter(width/4, height/2, width/2 - 100, height - 100));

        orderBox = new JPanel(new GridLayout(20,1));
        orderBox.setBackground(background);
        orderBox.setBounds(alignCenter(3*width/4, height/2 - 25, width/2 - 100, height - 150));

        processMenuItems();

        JButton checkoutButton = new JButton("checkout");
        checkoutButton.setFont(font.deriveFont(Font.PLAIN, 16));
        checkoutButton.setBounds(alignCenter(width - 100,height - 75,100,45));
        checkoutButton.addActionListener(e -> {
                checkout();
            });

        costLabel = new JLabel("Total: $" + runningCost);
        costLabel.setBounds(width/2 + 50, height - 85, 200,20);
        costLabel.setFont(font.deriveFont(Font.PLAIN, 20));

        JButton homeButton = new JButton("back");
        homeButton.setFont(font.deriveFont(Font.PLAIN, 16));
        homeButton.setBounds(width-100,10,70,30);
        homeButton.addActionListener(e -> {
                switchPage("login");
            });

        panel.add(drinkBox);
        panel.add(orderBox);
        panel.add(checkoutButton);
        panel.add(costLabel);
        panel.add(homeButton);
    }
}