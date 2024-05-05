import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * The Reports class is a Page containing a list of reports to view.
 * This class is accessed via ManagerHome, and can also return there.
 * @author caleb austin
 */
public class Reports extends Page {

    private JPanel contentPanel;
    private JScrollPane scrollPane;

    private JPanel contentPanel2;

    private JScrollPane scrollPane2;

    private ArrayList<Component> boxRigidAreas = new ArrayList<java.awt.Component>();
    private ArrayList<JPanel> rowPanels = new ArrayList<JPanel>();

    private reportsHandler reportMethods = new reportsHandler();

    private HashMap<Integer, String> resultsExcessReport = new HashMap<Integer,String>();
    private ArrayList<String> resultsRestockList = new ArrayList<String>();

    /**
     * Removes all contents of the left-panel, including data from previous reports.
     */

    private void clear() {
        while (!rowPanels.isEmpty()) {
            JPanel removedRow = rowPanels.remove(0); // Remove the first row
            contentPanel2.remove(removedRow);
        }
        while (!boxRigidAreas.isEmpty()) {
            java.awt.Component get = boxRigidAreas.remove(0);
            contentPanel2.remove(get);
        }
    }
    /**
     * Fills the contents of the left panel with a list of item names for a restock report.
     */
    private void restockReport() {
        clear();
        resultsRestockList.clear();
        resultsRestockList = reportMethods.restockList();

        for (int i = 0; i < resultsRestockList.size(); i++) {
            JPanel rowPanel = new JPanel();

            rowPanel.setPreferredSize(new Dimension(550, 50));
            rowPanel.setBackground(new Color(255,255,255));
            rowPanel.setMaximumSize(new Dimension(999999,50));

            rowPanel.setOpaque(true);

            JLabel textLabel = new JLabel(resultsRestockList.get(i));
            textLabel.setFont(new Font("Arial", Font.BOLD, 16));



            textLabel.setHorizontalTextPosition(SwingConstants.CENTER);

            textLabel.setHorizontalAlignment(SwingConstants.CENTER);

            rowPanel.add(textLabel);


            java.awt.Component rig = Box.createRigidArea(new Dimension(0,10));

            boxRigidAreas.add(rig);
            rowPanels.add(rowPanel);
            contentPanel2.add(rig);
            contentPanel2.add(rowPanel);


        }
        contentPanel2.revalidate();
        contentPanel2.repaint();
    }
    /**
     * Fills the contents of the left panel with a list of item names for a excess stock report.
     */
    private void excessReport(int timestamp) {
        clear();
        resultsExcessReport.clear();
        resultsExcessReport = reportMethods.getItemsBelow(timestamp);

        for (Map.Entry<Integer, String> entry : resultsExcessReport.entrySet()) {
            Integer key = entry.getKey();
            String value = entry.getValue();

            JPanel rowPanel = new JPanel();
            rowPanel.setLayout(new GridLayout(1,2));

            rowPanel.setPreferredSize(new Dimension(550, 50));
            rowPanel.setBackground(new Color(255,255,255));
            rowPanel.setMaximumSize(new Dimension(999999,50));

            rowPanel.setOpaque(true);

            JLabel leftLabel = new JLabel("Key: " + Integer.toString(key));
            leftLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel rightLabel = new JLabel(value);
            rightLabel.setFont(new Font("Arial", Font.BOLD, 16));

            leftLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            rightLabel.setHorizontalTextPosition(SwingConstants.CENTER);

            leftLabel.setHorizontalAlignment(SwingConstants.CENTER);
            rightLabel.setHorizontalAlignment(SwingConstants.CENTER);

            rowPanel.add(leftLabel);
            rowPanel.add(rightLabel);


            java.awt.Component rig = Box.createRigidArea(new Dimension(0,10));

            boxRigidAreas.add(rig);
            rowPanels.add(rowPanel);
            contentPanel2.add(rig);
            contentPanel2.add(rowPanel);


        }
        contentPanel2.revalidate();
        contentPanel2.repaint();


    }
    /**
     * Setups up the report page, including buttons for the reports on the right side.
     * This also creates the back button to return to ManagerHome.
     * @param g The Gui object that contains the panel object to store our UI.
     */
    Reports(GUI g) {
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

        JPanel leftPanel = new JPanel();
        JPanel rightPanel = new JPanel();

        leftPanel.setBackground(new Color(230,230,230));
        rightPanel.setBackground(new Color(230,230,230));
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);

        //right panel work
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

        //left panel work
        contentPanel2 = new JPanel();
        BoxLayout box2 = new BoxLayout(contentPanel2,BoxLayout.Y_AXIS);
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        contentPanel2.setLayout(box2);


        scrollPane2 = new JScrollPane(contentPanel2);
        scrollPane2.setOpaque(true);
        scrollPane2.setBorder(null);
        contentPanel2.setOpaque(true);
        contentPanel2.setBorder(null);

        scrollPane2.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        leftPanel.add(scrollPane2);

        //buttons on the right

        JPanel getItemsBelowPanel = new JPanel();
        getItemsBelowPanel.setLayout(new GridLayout(1,3));

        getItemsBelowPanel.setPreferredSize(new Dimension(550, 100));
        getItemsBelowPanel.setBackground(new Color(255,255,255));
        getItemsBelowPanel.setMaximumSize(new Dimension(10000,100));

        JLabel panelName = new JLabel("Excess Report");
        JPanel middlePanel = new JPanel(new GridLayout(2,1));
        JLabel middleLabel = new JLabel("Timestamp");
        JTextField userInput = new JTextField("0");
        JButton panelButton = new JButton("View");

        middlePanel.setOpaque(false);

        panelName.setHorizontalTextPosition(SwingConstants.CENTER);
        panelName.setHorizontalAlignment(SwingConstants.CENTER);
        userInput.setHorizontalAlignment(SwingConstants.CENTER);
        panelButton.setHorizontalTextPosition(SwingConstants.CENTER);
        panelButton.setHorizontalAlignment(SwingConstants.CENTER);
        middleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        middleLabel.setHorizontalTextPosition(SwingConstants.CENTER);

        panelName.setFont(new Font("Arial", Font.BOLD, 16));
        userInput.setFont(new Font("Arial", Font.PLAIN, 16));
        panelButton.setFont(new Font("Arial", Font.PLAIN, 16));
        middleLabel.setFont(new Font("Arial", Font.PLAIN, 16));

        middlePanel.add(middleLabel);
        middlePanel.add(userInput);
        getItemsBelowPanel.add(panelName);
        getItemsBelowPanel.add(middlePanel);
        getItemsBelowPanel.add(panelButton);

        panelButton.addActionListener(e -> {
            try {
                String userNumber = userInput.getText();
                int numberToUse = Integer.parseInt(userNumber);
                if (numberToUse < 0) {
                    numberToUse = 0;
                }
                excessReport(numberToUse);
            } catch (NumberFormatException b) {
                b.printStackTrace();
            }

        });

        // split

        JPanel restockListPanel = new JPanel();
        restockListPanel.setLayout(new GridLayout(1,3));

        restockListPanel.setPreferredSize(new Dimension(550, 100));
        restockListPanel.setBackground(new Color(255,255,255));
        restockListPanel.setMaximumSize(new Dimension(10000,100));

        JLabel panelName2 = new JLabel("Restock Report");
        JLabel nothing = new JLabel("");
        JButton panelButton2 = new JButton("View");

        panelName2.setHorizontalTextPosition(SwingConstants.CENTER);
        panelName2.setHorizontalAlignment(SwingConstants.CENTER);
        panelButton2.setHorizontalTextPosition(SwingConstants.CENTER);
        panelButton2.setHorizontalAlignment(SwingConstants.CENTER);

        panelName2.setFont(new Font("Arial", Font.BOLD, 16));
        panelButton2.setFont(new Font("Arial", Font.PLAIN, 16));


        restockListPanel.add(panelName2);
        restockListPanel.add(nothing);
        restockListPanel.add(panelButton2);

        panelButton2.addActionListener(e -> {
            restockReport();
        });

        //add into right

        java.awt.Component rig = Box.createRigidArea(new Dimension(0,10));
        contentPanel.add(rig);
        contentPanel.add(getItemsBelowPanel);

        java.awt.Component rig2 = Box.createRigidArea(new Dimension(0,10));
        contentPanel.add(rig2);
        contentPanel.add(restockListPanel);

        //finishing up



        panel.add(mainPanel,BorderLayout.CENTER);
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        JLabel l1=new JLabel("REPORTS");
        panel.add(l1,BorderLayout.NORTH);


    }
}