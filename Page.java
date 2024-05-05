import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.IOException;

/** abstract class that all pages will inheret from */
abstract class Page {
    /**
     * A reference to the main gui, so that the cards can be switched and the main frame component can be accessed
     */
    protected GUI gui;

    /**
     * The main panel component where everything is displayed
     */
    protected JPanel panel = new JPanel();
    private JPanel errorBox;

    Font font;

    /**
     * The width of the window
     */
    public int width = 1200;
    /**
     * The height of the window
     */
    public int height = 800;

    Page(GUI g) {
        gui = g;

        // using null layout for precise positioning
        panel.setLayout(null); 

        try {
            // creates a new font
            File fontFile = new File("./assets/fonts/Lato-Bold.ttf");
            font = Font.createFont(Font.TRUETYPE_FONT, fontFile);

            // Register the custom font with the GraphicsEnvironment
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
        }
        catch (IOException | FontFormatException e) {
            // incase getting font fails then set to a default
            e.printStackTrace();
            font = new Font("Arial", Font.BOLD, 60);
        }
    }

    /** 
     * getter for the panel 
     * @return The panel which holds all the visual components
    */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * takes the coordinates of a rectangle centered on x, y and shifts it.
     * Use when setting the bounding box of any swing object so to make it easier to center
     * @param x x coordinate of the center of the rectangle
     * @param y y coordinate of the center of the rectangle
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @return A Rectangle object with the center at x,y
    */
    public static Rectangle alignCenter(int x, int y, int width, int height) {
        return new Rectangle(x - (width/2), y - (height/2), width, height);
    }

    /**
    * hides any error box on the page
    * used when the close box button is hit or when the panel is switched to a different page
    */
    protected void hideError() {
        if (errorBox != null) errorBox.setVisible(false);
    }

    /** 
     * switches the page 
     * @param page A string which describes the constraint of the card panel
     */
    protected void switchPage(String page) {
        // hide errors on the current page
        hideError();
        gui.switchPage(page);
    }

    /**
     * displays a popup box with the error message
     * old error boxes will be automaticly hidden (this could be impoved in the future)
     * @param message A string of the message that will appear in the error box
    */
    protected void showError(String message) {
        hideError();

        Color background = new Color(255,130,100);

        errorBox = new JPanel();

        errorBox.setLayout(new BoxLayout(errorBox,BoxLayout.PAGE_AXIS));
        // errorBox.setLayout(new FlowLayout());
        errorBox.setBounds(width-400, height-120, 390, 80);

        errorBox.setBackground(background);

        // just a label with the text "ERROR"
        JLabel l1 = new JLabel("ERROR");
        l1.setFont(font.deriveFont(Font.PLAIN, 20));
        l1.setBorder(new EmptyBorder(6, 8, 3, 8));

        // a label that displays the given text in message
        JLabel messageDisplay = new JLabel(message);
        messageDisplay.setFont(font.deriveFont(Font.PLAIN, 16));
        messageDisplay.setBorder(new EmptyBorder(3, 30, 6, 8));
        messageDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

        // a button that hides the error box when clicked
        JLabel closeButton = new JLabel("X");
        closeButton.setFont(font.deriveFont(Font.PLAIN, 20));
        closeButton.setBorder(new EmptyBorder(6, 8, 3, 8));
        closeButton.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent me) {
                hideError();
            }
        });

        // creating a new panel to align the text "ERROR" and the close button horizontaly
        JPanel horizontalBox = new JPanel();
        horizontalBox.setLayout(new BoxLayout(horizontalBox,BoxLayout.LINE_AXIS));
        horizontalBox.setBackground(background);
        horizontalBox.setAlignmentX(Component.LEFT_ALIGNMENT);

        horizontalBox.add(l1);
        horizontalBox.add(Box.createHorizontalGlue()); // this does some sort of magic to make closeButton align with the right edge
        horizontalBox.add(closeButton);
        
        errorBox.add(horizontalBox);
        errorBox.add(messageDisplay);

        // add it to the main panel
        panel.add(errorBox);

        // finally, repaint the panel so that the new components are drawn
        panel.revalidate();
        panel.repaint();
    }
}