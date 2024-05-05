import java.awt.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/**
 * The Batch class contains data for a particular batch, obtained from the database.
 * It contains information such as the key, expriation date, item name, ingredient id, and etc
 *
 * @author caleb austin
 */

class Batch {
    private int batchKey;
    private int inDate;
    private int expirationDate;
    private String itemName;
    private int ingredientId;

    private int amount;
    /**
     * Constructs a batch with given information, must connect to the database to obtain the info
     *
     * @param batchKey Unique id for this batch
     * @param inDate When this batch entered storage
     * @param expirationDate When this batch will have its contents expire
     * @param itemName The name of the particular item inside of the batch
     * @param ingredientId The unique id for the type of item this batch contains
     * @param amount The amount of items inside of the batch
     */
    public Batch(int batchKey, int inDate, int expirationDate, String itemName, int ingredientId, int amount) {
        this.batchKey = batchKey;
        this.inDate = inDate;
        this.expirationDate = expirationDate;
        this.itemName = itemName;
        this.ingredientId = ingredientId;
        this.amount = amount;
    }

    // Getters and setters for each property

    /**
     * @return integer representing the batch key id
     */
    public int getBatchKey() {
        return batchKey;
    }

    /**
     * @return date when batch entered storage as a UNIX timestamp
     */
    public int getInDate() {
        return inDate;
    }

    /**
     * @return date when batches contents will expire as a UNIX timestamp
     */
    public int getExpirationDate() {
        return expirationDate;
    }

    /**
     *
     * @return the name of the item that this batch contains
     */
    public String getItemName() {
        return itemName;
    }

    /**
     *
     * @return an integer representing the id of the item the batch contains
     */
    public int getIngredientId() {
        return ingredientId;
    }

    /**
     *
     * @return the amount of items the batch contains
     */
    public int getAmount() {
        return amount;
    }

    /**
     *
     * @param ch the number that will be directly added to amount, can be either sign
     */
    public void changeAmount(int ch) {
        this.amount = this.amount + ch;
    }
}
/**
 * The Batches class is a Page containing a list of batches to view.
 * The page is split with the left side containing batches and the right item names.
 * It allows for the creation of new batches, as well as managing the counts of each batch.
 * This class is accessed via ManagerHome, and can also return there.
 *
 * @author caleb austin
 */
public class Batches extends Page {
    // inherets JPanel panel from Page which is where we display things

    // constuctor
    private batchHandler batchMethods = new batchHandler();
    private ArrayList<String> ingredientNames;
    private JScrollPane scrollPane;
    private JPanel contentPanel;
    private JPanel contentPanel2;

    private JLabel addBatchLabel;
    private JTextField addBatchField;

    private ArrayList<JPanel> rowPanels = new ArrayList<JPanel>();
    private ArrayList<JPanel> batchPanels = new ArrayList<JPanel>();

    private ArrayList<java.awt.Component> boxRigidAreas = new ArrayList<java.awt.Component>();
    private ArrayList<java.awt.Component> boxRigidAreas2 = new ArrayList<java.awt.Component>();

    JScrollPane scrollingFrame;
    JPanel bottomPanel;
    AtomicReference<String> keeper = new AtomicReference<>("None");

    int currentIngredient;
    String currentIngredientName;

    private String convertDateToString(long givenDate) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(givenDate),
                ZoneId.systemDefault());

        // Format the LocalDateTime to a string with the format "HH:mm:yyyy-MM-dd"
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:yyyy-MM-dd");
        return dateTime.format(formatter);
    }

    private void populateBatchEntries(String ingredientName, int i, JLabel topRightLabel) { //i is ingredient id
        while (!batchPanels.isEmpty()) {
            JPanel get = batchPanels.remove(0);
            contentPanel2.remove(get);
        }
        while (!boxRigidAreas2.isEmpty()) {
            java.awt.Component get = boxRigidAreas2.remove(0);
            contentPanel2.remove(get);
        }
        ArrayList<BatchEntry> batches = batchMethods.getBatchesFromIngredient(ingredientNames.get(i));
        AtomicInteger amount = new AtomicInteger();
        for (BatchEntry b : batches){
            amount.addAndGet(b.getAmount());
        }

        topRightLabel.setText("Amount: " + Integer.toString(amount.get()));
        keeper.set(ingredientName);
        while (!batchPanels.isEmpty()) {
            JPanel get = batchPanels.remove(0);
            contentPanel2.remove(get);
        }
        while (!boxRigidAreas2.isEmpty()) {
            java.awt.Component get = boxRigidAreas2.remove(0);
            contentPanel2.remove(get);
        }
        contentPanel2.repaint();
        contentPanel2.revalidate();
        for (int j = 0; j < batches.size(); j++) {
            //Print out batches
            System.out.println("======");
            System.out.println(batches.get(j).getBatchKey());
            System.out.println(batches.get(j).getIngredientId());
            System.out.println(batches.get(j).getItemName());
            System.out.println(batches.get(j).getAmount());
            System.out.println(convertDateToString(batches.get(j).getInDate().toEpochMilli()));
            System.out.println(convertDateToString(batches.get(j).getExpirationDate().toEpochMilli()));
            System.out.println("======");

            AtomicInteger realAmount = new AtomicInteger(batches.get(j).getAmount());
            JPanel batchPanel = new JPanel();
            batchPanel.setLayout(new BorderLayout());

            batchPanel.setPreferredSize(new Dimension(550, 50));
            batchPanel.setMaximumSize(new Dimension(999999,50));
            batchPanel.setBackground(new Color(255,255,255));

            JLabel topLeftLabelBatch = new JLabel("Key: " + Integer.toString(batches.get(j).getBatchKey()));
            topLeftLabelBatch.setOpaque(false);
            topLeftLabelBatch.setFont(new Font("Arial", Font.PLAIN, 16));
            Dimension buttonSize2 = new Dimension(250, topLeftLabelBatch.getPreferredSize().height);
            topLeftLabelBatch.setPreferredSize(buttonSize2);

            JLabel topRightLabelBatch = new JLabel("Num: " + Integer.toString(realAmount.get()));
            topRightLabelBatch.setOpaque(false);
            topRightLabelBatch.setFont(new Font("Arial", Font.PLAIN, 16));
            topRightLabelBatch.setHorizontalAlignment(SwingConstants.RIGHT);

            JButton plusButton = new JButton("+");
            JButton minusButton = new JButton("-");
            JTextField numberInput = new JTextField("1");
            plusButton.setOpaque(false);
            minusButton.setOpaque(false);
            numberInput.setOpaque(false);
            plusButton.setFont(new Font("Arial", Font.PLAIN, 16));
            minusButton.setFont(new Font("Arial", Font.PLAIN, 16));
            numberInput.setFont(new Font("Arial", Font.PLAIN, 16));
            numberInput.setHorizontalAlignment(SwingConstants.CENTER);
            int finalJ = j;
            plusButton.addActionListener(m -> {
                String userInput = numberInput.getText();

                try {
                    int numberToUse = Integer.parseInt(userInput);
                    if (numberToUse < 0) {
                        numberToUse = 0;
                    }
                    batchMethods.addAmountToBatch(Integer.toString(batches.get(finalJ).getBatchKey()),numberToUse);
                    amount.addAndGet(numberToUse);
                    realAmount.addAndGet(numberToUse);
                    topRightLabel.setText("Amount: " + Integer.toString(amount.get()));
                    topRightLabelBatch.setText("Amount: " + realAmount);

                } catch (NumberFormatException ex) {
                    System.out.println("invalid input");
                };
            });
            minusButton.addActionListener(m -> {
                String userInput = numberInput.getText();

                try {
                    int numberToUse = Integer.parseInt(userInput);
                    if (numberToUse < 0) {
                        numberToUse = 0;
                    }
                    int checkLess = realAmount.get() - numberToUse;
                    if (checkLess < 0) {
                        numberToUse = realAmount.get();
                    }
                    batchMethods.removeAmountToBatch(Integer.toString(batches.get(finalJ).getBatchKey()),numberToUse);
                    amount.addAndGet(-numberToUse);
                    realAmount.addAndGet(-numberToUse);
                    topRightLabel.setText("Amount: " + Integer.toString(amount.get()));
                    topRightLabelBatch.setText("Amount: " + realAmount);
                } catch (NumberFormatException ex) {
                    System.out.println("invalid input");
                };
            });


            JPanel buttonPanelBatch = new JPanel();
            buttonPanelBatch.setLayout(new GridLayout());
            buttonPanelBatch.setOpaque(false);
            buttonPanelBatch.add(topRightLabelBatch);
            buttonPanelBatch.add(plusButton);
            buttonPanelBatch.add(minusButton);
            buttonPanelBatch.add(numberInput);

            JPanel buttonPanelBatch2 = new JPanel();
            buttonPanelBatch2.setLayout(new GridLayout());
            buttonPanelBatch2.setOpaque(false);
            buttonPanelBatch2.add(topLeftLabelBatch);
            buttonPanelBatch2.setPreferredSize(new Dimension(200,25));

            JButton batchCloseButton = new JButton("X");
            batchCloseButton.setOpaque(false);
            batchCloseButton.setFont(new Font("Arial", Font.PLAIN, 16));
            batchCloseButton.setPreferredSize(new Dimension(25,25));
            buttonPanelBatch2.add(batchCloseButton);

            int finalJ1 = j;
            batchCloseButton.addActionListener(m -> {

                batchMethods.removeBatch(Integer.toString(batches.get(finalJ1).getBatchKey()));

                populateIngredientEntries();

                contentPanel2.revalidate();
                contentPanel2.repaint();
                contentPanel.revalidate();
                contentPanel.repaint();

            });



            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Date inDate = new Date(batches.get(j).getInDate().getEpochSecond() * 1000L); // Multiply by 1000 to convert from seconds to milliseconds
            Date exDate = new Date(batches.get(j).getExpirationDate().getEpochSecond() * 1000L);
            String formatInDate = dateFormat.format(inDate);
            String formateExDate = dateFormat.format(exDate);

            JLabel bottomLeftLabelBatch = new JLabel("In Date:" + formatInDate);
            bottomLeftLabelBatch.setOpaque(false);
            bottomLeftLabelBatch.setFont(new Font("Arial", Font.PLAIN, 12));

            JLabel bottomRightLabelBatch = new JLabel("Expires: " + formateExDate);
            bottomRightLabelBatch.setOpaque(false);
            bottomRightLabelBatch.setFont(new Font("Arial", Font.PLAIN, 12));
            bottomRightLabelBatch.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel topLabelsPanelBatch = new JPanel(new BorderLayout());
            topLabelsPanelBatch.setOpaque(false);
            topLabelsPanelBatch.add(buttonPanelBatch2, BorderLayout.WEST);
            topLabelsPanelBatch.add(buttonPanelBatch, BorderLayout.EAST);

            JPanel bottomLabelsPanelBatch = new JPanel(new BorderLayout());
            bottomLabelsPanelBatch.setOpaque(false);
            bottomLabelsPanelBatch.add(bottomLeftLabelBatch, BorderLayout.WEST);
            bottomLabelsPanelBatch.add(bottomRightLabelBatch, BorderLayout.EAST);

            batchPanel.add(topLabelsPanelBatch, BorderLayout.NORTH);
            batchPanel.add(bottomLabelsPanelBatch, BorderLayout.SOUTH);
            topLabelsPanelBatch.setBorder(BorderFactory.createEmptyBorder(0,5,0,10));
            bottomLabelsPanelBatch.setBorder(BorderFactory.createEmptyBorder(0,5,0,10));


            batchPanel.setOpaque(true);

            batchPanels.add(batchPanel);
            java.awt.Component rigid = Box.createRigidArea(new Dimension(0,10));
            boxRigidAreas2.add(rigid);
            contentPanel2.add(rigid);
            contentPanel2.add(batchPanel);
            contentPanel2.revalidate();
            contentPanel2.repaint();

        }

        bottomPanel.removeAll();
        bottomPanel.setLayout(new GridLayout(3, 1));
        addBatchLabel = new JLabel("Add Batch: " + ingredientName);
        addBatchLabel.setHorizontalAlignment(SwingConstants.CENTER);
        bottomPanel.add(addBatchLabel);

        JPanel amountRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel amountLabel = new JLabel("Amount in Batch");
        addBatchField = new JTextField(10);
        amountRow.add(amountLabel);
        amountRow.add(addBatchField);
        bottomPanel.add(amountRow);

        JPanel buttonRow = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton submitButton = new JButton("Submit");
        buttonRow.add(submitButton);
        bottomPanel.add(buttonRow);
        submitButton.addActionListener(e -> {
            try {
            String userInput = addBatchField.getText();
            int numberToUse = Integer.parseInt(userInput);
            if (numberToUse < 0) {
                numberToUse = 0;
            }
            if (!keeper.get().equals("None")) {
                System.out.println("Batch Amount: " + numberToUse);
                //UNCOMMENT THIS
                batchMethods.addBatch(numberToUse,ingredientName);
                populateBatchEntries(ingredientName,i,topRightLabel);

            } } catch (NumberFormatException b) {
                b.printStackTrace();
                System.out.println("Invalid input");
            }

        });
        bottomPanel.revalidate();
        bottomPanel.repaint();
        System.out.println("Done loading");
    }

    private void populateIngredientEntries() {
        ingredientNames = batchMethods.getIngredients();
        bottomPanel.removeAll();
        while (!rowPanels.isEmpty()) {
            JPanel removedRow = rowPanels.remove(0); // Remove the first row
            contentPanel.remove(removedRow);
        }
        while (!boxRigidAreas.isEmpty()) {
            java.awt.Component get = boxRigidAreas.remove(0);
            contentPanel.remove(get);
        }
        while (!batchPanels.isEmpty()) {
            JPanel get = batchPanels.remove(0);
            contentPanel2.remove(get);
        }
        while (!boxRigidAreas2.isEmpty()) {
            java.awt.Component get = boxRigidAreas2.remove(0);
            contentPanel2.remove(get);
        }



        int placeholderKey = 0;

        for (int i = 0; i < ingredientNames.size(); i++) {
            ArrayList<BatchEntry> batches = batchMethods.getBatchesFromIngredient(ingredientNames.get(i));
            AtomicInteger amount = new AtomicInteger();
            for (BatchEntry b : batches){
                amount.addAndGet(b.getAmount());
            }

            String inDateToUse = "XX:XX:XXXX";
            String orderByToUse = "XX:XX:XXXX";

            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new BorderLayout());

            rowPanel.setPreferredSize(new Dimension(550, 50));
            rowPanel.setBackground(new Color(255,255,255));

            JButton topLeftLabel = new JButton(ingredientNames.get(i));
            topLeftLabel.setOpaque(false);
            topLeftLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            Dimension buttonSize = new Dimension(200, topLeftLabel.getPreferredSize().height);
            topLeftLabel.setPreferredSize(buttonSize);

            JLabel topRightLabel = new JLabel("Amount: " + Integer.toString(amount.get()));
            topRightLabel.setOpaque(false);
            topRightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
            topRightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            int finalI = i;
            topLeftLabel.addActionListener(e -> {
                if (ingredientNames.get(finalI) != null) {
                    keeper.set(ingredientNames.get(finalI));
                }

                populateBatchEntries(ingredientNames.get(finalI),finalI,topRightLabel);
            });

            if (keeper.get() != null && ingredientNames.get(finalI) != null) {
                if (ingredientNames.get(finalI).equals(keeper.get())) {
                    populateBatchEntries(ingredientNames.get(finalI),finalI,topRightLabel);
                }
            }




            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout());
            buttonPanel.setOpaque(false);
            buttonPanel.add(topRightLabel);



            JLabel bottomLeftLabel = new JLabel("Last Order:" + inDateToUse);
            bottomLeftLabel.setOpaque(false);
            bottomLeftLabel.setFont(new Font("Arial", Font.PLAIN, 12));

            JLabel bottomRightLabel = new JLabel("Order By: " + orderByToUse);
            bottomRightLabel.setOpaque(false);
            bottomRightLabel.setFont(new Font("Arial", Font.PLAIN, 12));
            bottomRightLabel.setHorizontalAlignment(SwingConstants.RIGHT);

            JPanel topLabelsPanel = new JPanel(new BorderLayout());
            topLabelsPanel.setOpaque(false);
            topLabelsPanel.add(topLeftLabel, BorderLayout.WEST);
            topLabelsPanel.add(buttonPanel, BorderLayout.EAST);

            JPanel bottomLabelsPanel = new JPanel(new BorderLayout());
            bottomLabelsPanel.setOpaque(false);
            bottomLabelsPanel.add(bottomLeftLabel, BorderLayout.WEST);
            bottomLabelsPanel.add(bottomRightLabel, BorderLayout.EAST);

            rowPanel.add(topLabelsPanel, BorderLayout.NORTH);
            rowPanel.add(bottomLabelsPanel, BorderLayout.SOUTH);
            topLabelsPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,10));
            bottomLabelsPanel.setBorder(BorderFactory.createEmptyBorder(0,5,0,10));


            rowPanel.setOpaque(true);

            java.awt.Component rig = Box.createRigidArea(new Dimension(0,10));
            contentPanel.add(rig);
            boxRigidAreas.add(rig);
            contentPanel.add(rowPanel);

            //add to list
            rowPanels.add(rowPanel);

        }
    }
    Batches(GUI g) {
        super(g);
        
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(155,165,185));


        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> {
            switchPage("managerhome");
        });

        GridLayout gdLayout = new GridLayout(1,2);
        gdLayout.setHgap(10);
        JPanel mainPanel = new JPanel(gdLayout);
        mainPanel.setOpaque(false);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        BorderLayout leftLayout = new BorderLayout();
        JPanel leftPanel = new JPanel(leftLayout);
        JPanel rightPanel = new JPanel();


        leftPanel.setBackground(new Color(230,230,230));
        rightPanel.setBackground(new Color(230,230,230));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        batchMethods = new batchHandler();


        contentPanel = new JPanel();
        BoxLayout boxl = new BoxLayout(contentPanel,BoxLayout.Y_AXIS);
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        contentPanel.setLayout(boxl);


        scrollPane = new JScrollPane(contentPanel);
        scrollPane.setOpaque(true);
        scrollPane.setBorder(null);
        contentPanel.setOpaque(true);
        contentPanel.setBorder(null);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        rightPanel.add(scrollPane);


        //Left Side work


        leftPanel.setLayout(new GridBagLayout());

        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        //topPanel.setPreferredSize(new Dimension(leftPanel.getWidth(), (int) (leftPanel.getHeight()*.7)));

        contentPanel2 = new JPanel();
        BoxLayout box2 = new BoxLayout(contentPanel2,BoxLayout.Y_AXIS);
        contentPanel2.setLayout(box2);


        scrollingFrame = new JScrollPane(contentPanel2);
        scrollingFrame.setOpaque(true);
        scrollingFrame.setBorder(null);
        contentPanel2.setOpaque(true);
        contentPanel2.setBorder(null);

        scrollingFrame.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        topPanel.add(scrollingFrame);

        bottomPanel = new JPanel();
        bottomPanel.setPreferredSize(new Dimension(leftPanel.getWidth(), (int) (leftPanel.getHeight() * 0.3)));

        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1.0;
        c.weighty = 0.7; // 70%
        leftPanel.add(topPanel, c);

        c.gridy = 1;
        c.weighty = 0.3; // 30%
        leftPanel.add(bottomPanel, c);

        panel.add(mainPanel,BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JLabel l1=new JLabel("BATCHES");
        panel.add(l1,BorderLayout.NORTH);

        populateIngredientEntries();

        bottomPanel.setLayout(new GridLayout(3, 1));
    }
}