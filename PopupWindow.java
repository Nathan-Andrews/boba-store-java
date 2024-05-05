import java.awt.*;
import java.io.File;
import java.io.IOException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.event.MouseEvent;
import java.awt.event.MouseAdapter;

/**
 * A small popup window
 *
 * Opens a popup window where components can be displayes
 *
 * @author Nathan Andrews
 * @version 1.0
 */
abstract class PopupWindow {
    /** The window itself */
    protected JFrame frame;

    /** The panel that holds all the visual elements */
    protected JPanel panel = new JPanel();
    private JPanel errorBox;

    Font font;

    /** The width of the window */
    public int width = 600;
    /** The height of the window */
    public int height = 400;
    
    PopupWindow () {
        frame = new JFrame();
        frame.setSize(width,height);

        // quit program when window closes
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

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

        frame.add(panel,BorderLayout.CENTER);

        frame.setVisible(true);
    }

    /** Refreshes all the visual elements so that new components can be drawn */
    protected void repaint() {
        frame.revalidate();
        frame.repaint();
    }

    /**
     * takes the coordinates of a rectangle centered on x, y and shifts it.
     * Use when setting the bounding box of any swing object so to make it easier to center
     * @param x x coordinate of the center of the rectangle
     * @param y y coordinate of the center of the rectangle
     * @param rectWidth width of the rectangle
     * @param rectHeight height of the rectangle
     * @return A Rectangle object with the center at x,y
    */
    public static Rectangle alignCenter(int x, int y, int rectWidth, int rectHeight) {
        return Page.alignCenter(x, y, rectWidth, rectHeight);
    }

    /**
    * hides any error box on the page
    * used when the close box button is hit or when the panel is switched to a different page
    */
    protected void hideError() {
        if (errorBox != null) errorBox.setVisible(false);
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
        errorBox.setBounds(width-250, height-100, 240, 60);

        errorBox.setBackground(background);

        // just a label with the text "ERROR"
        JLabel l1 = new JLabel("ERROR");
        l1.setFont(font.deriveFont(Font.PLAIN, 16));
        l1.setBorder(new EmptyBorder(6, 8, 3, 8));

        // a label that displays the given text in message
        JLabel messageDisplay = new JLabel(message);
        messageDisplay.setFont(font.deriveFont(Font.PLAIN, 12));
        messageDisplay.setBorder(new EmptyBorder(3, 30, 6, 8));
        messageDisplay.setAlignmentX(Component.LEFT_ALIGNMENT);

        // a button that hides the error box when clicked
        JLabel closeButton = new JLabel("X");
        closeButton.setFont(font.deriveFont(Font.PLAIN, 16));
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

        // ensures that the error box is rendered on top of other components
        panel.setComponentZOrder(errorBox,0);

        // finally, repaint the panel so that the new components are drawn
        repaint();
    }
    
}