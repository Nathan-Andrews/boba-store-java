import java.util.*;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.Border;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Items that are commonly sold together
 *
 * Opens a popup window where you can view items commonly
 * sold together over a selected date range
 *
 * @author Nathan Andrews
 * @version 1.0
 */
public class SoldTogether extends PopupWindow {
    Color background = new Color(235,240,245);

    JPanel listBox;

    private void generateList(long timestampFrom, long timestampTo) {
        ArrayList<ItemPair> salesList = new reportsHandler().whatSalesTogether(timestampFrom,timestampTo);

        ArrayList<String> items = new menuHandler().getMenuItems();

        listBox.removeAll();
        
        int listCount = 10;
        listBox.setLayout(new GridLayout(listCount,1));

        for (ItemPair pair : salesList) {
            if (Integer.max(pair.items._1(),pair.items._2()) >= items.size()) continue;
            if (listCount-- <= 0) break;

            JLabel labelContent = new JLabel(
                items.get(pair.items._1()-1) 
                + " + " 
                + items.get(pair.items._2()-1) 
                + ": " 
                + Integer.toString(pair.sales)
            );
            labelContent.setFont(font.deriveFont(Font.PLAIN, 16));

            listBox.add(labelContent);
        }
    }

    private boolean handleDateInputs(long timestampFrom, long timestampTo) {
        if (timestampFrom >= timestampTo) {
            System.out.println("error: start date must be before end date");
            showError("Start date must be before end date");
            repaint();
            return false;
        }

        listBox.removeAll();
        
        JLabel loadingLabel = new JLabel("LOADING DATA");
        loadingLabel.setHorizontalAlignment(SwingConstants.CENTER);
        listBox.add(loadingLabel,BorderLayout.CENTER);

        repaint();

        new Thread(new Runnable() {
            @Override public void run() {

            generateList(timestampFrom, timestampTo);

            repaint();
            
            return;
            }
        }).start();

        return true;
    }

    /**
     * Constructor for the SoldTogether class. Initializes the UI components,
     * sets up the layout, and assigns functionalities to interactive elements.
     */
    SoldTogether() {
        super();

        frame.setName("Sold Together");

        panel.setBackground(background);


        // panel where the image of the graph will be displayed
        listBox = new JPanel(new BorderLayout());
        listBox.setBounds(10,40,width-20,height - 140);

        // Create a black line border
        Border blackLineBorder = BorderFactory.createLineBorder(new Color(155,165,185),3);
        listBox.setBorder(blackLineBorder);


        JLabel titleLabel = new JLabel("Often Sold Together");
        titleLabel.setFont(font.deriveFont(Font.PLAIN, 20));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBounds(10,10,width-20,30);

        // panel for the input
        JPanel inputBox = new JPanel(new GridLayout(1,3));
        inputBox.setBounds(10,height - 80,width - 80,40);

        inputBox.setBorder(blackLineBorder);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        JFormattedTextField dateField = new JFormattedTextField(dateFormat);
        dateField.setColumns(10); // Set the width of the field
        dateField.setText("2023-10-01"); // Set default text
        dateField.setFont(font.deriveFont(Font.PLAIN, 15));

        JFormattedTextField dateField2 = new JFormattedTextField(dateFormat);
        dateField2.setColumns(10); // Set the width of the field
        dateField2.setText("2023-10-16"); // Set default text
        dateField2.setFont(font.deriveFont(Font.PLAIN, 15));


        JButton submitButton = new JButton("Submit");
        submitButton.setBounds(width-70,height - 80,60,40);
        submitButton.setFont(font.deriveFont(Font.PLAIN, 14));
        submitButton.addActionListener(e -> {
            try {
                // Parse the date input from the field
                Date firstDate = dateFormat.parse(dateField.getText());
                Date secondDate = dateFormat.parse(dateField2.getText());

                handleDateInputs(firstDate.getTime() / 1000, secondDate.getTime() / 1000);
            } catch (ParseException ex) {
                System.err.println("Invalid date format");
                showError("Invalid date format. Use yyyy-MM-dd");
            }
        });

        JPanel dateFrom = new JPanel();
        dateFrom.setLayout(new BoxLayout(dateFrom, BoxLayout.LINE_AXIS));
        JLabel l1 = new JLabel("Start Date:");
        l1.setFont(font.deriveFont(Font.PLAIN, 16));
        dateFrom.add(l1);
        dateFrom.add(dateField);

        JPanel dateTo = new JPanel();
        dateTo.setLayout(new BoxLayout(dateTo, BoxLayout.LINE_AXIS));
        JLabel l2 = new JLabel("End Date:");
        l2.setFont(font.deriveFont(Font.PLAIN, 16));
        dateTo.add(l2);
        dateTo.add(dateField2);
        

        inputBox.add(dateFrom);
        inputBox.add(new JPanel());
        inputBox.add(dateTo);
        // inputBox.add(submitButton);

        panel.add(titleLabel);
        panel.add(inputBox);
        panel.add(submitButton);
        panel.add(listBox);

        repaint();
    }
}