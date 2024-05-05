
import java.awt.*;
import javax.swing.*;

/**
 * This class creates the page that acts as a hub to reach other manager pages.
 * This includes menu items, batches and reports.
 * @author caleb austin
 */

public class ManagerHome extends Page {
    // inherets JPanel panel from Page which is where we display things

    // constuctor

    /**
     * Creates the manager home page. This is only loaded once.
     * @param g The GUI object that contains the main object panel.
     */
    ManagerHome(GUI g) {
        super(g);
        // JTextPane
        panel.setBackground(new Color(155,165,185));
        JLabel l1=new JLabel("MANAGER HOMEPAGE");
        l1.setBounds(0, 50, width, 60);
        l1.setFont(font.deriveFont(Font.PLAIN, 60));
        l1.setHorizontalAlignment(SwingConstants.CENTER);
        l1.setBackground(new Color(230,230,230));
        l1.setOpaque(true);

        panel.add(l1);

        JButton Batches = new JButton("Batches");
        Batches.addActionListener(e -> {
            switchPage("batches");
        });
        Batches.setBounds(100,150,1000,150);
        Batches.setFont(font.deriveFont(font.PLAIN,60));
        Batches.setText("Batches");
        Batches.setHorizontalAlignment(SwingConstants.CENTER);
        Batches.setBackground(new Color(250,250,250));
        Batches.setOpaque(true);

        panel.add(Batches);

        JButton Editing = new JButton("Editing");
        Editing.addActionListener(e -> {
            switchPage("manager");
        });
        Editing.setBounds(100,350,1000,150);
        Editing.setFont(font.deriveFont(Font.PLAIN,60));
        Editing.setText("Menu Items");
        Editing.setHorizontalAlignment(SwingConstants.CENTER);
        Editing.setBackground(new Color(250,250,250));
        Editing.setOpaque(true);

        panel.add(Editing);

        JButton Reports = new JButton("Reports");
        Reports.addActionListener(e -> {
            switchPage("reports");
        });
        Reports.setBounds(100,550,1000,150);
        Reports.setFont(font.deriveFont(Font.PLAIN,60));
        Reports.setText("Reports");
        Reports.setHorizontalAlignment(SwingConstants.CENTER);
        Reports.setBackground(new Color(250,250,250));
        Reports.setOpaque(true);

        panel.add(Reports);

        JButton backButton = new JButton("back");
        backButton.setFont(font.deriveFont(Font.PLAIN, 16));
        backButton.setBounds(width-100,10,70,30);
        backButton.addActionListener(e -> {
            switchPage("login");
        });

        panel.add(backButton);

        //JPanel buttonPanel = new JPanel();
        //buttonPanel.add(backButton);
        //panel.add(buttonPanel, BorderLayout.SOUTH);
    }
}