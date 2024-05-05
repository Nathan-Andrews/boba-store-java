// importing Java AWT class  
import java.awt.*;
import javax.swing.*;
// import backend.*;

/**
 * The class that manages the ability to maneuver through each page.
 * This loads everything upon starting the program.
 */

public class GUI {
    // frame is the window which contains the panels
    private JFrame frame = new JFrame("boba");

    private static CardLayout cardLayout;
    private static JPanel cardPanel;
    
    private Login loginPage;
    private Batches batches;
    private Server serverPage;
    private Manager managerPage;
    private ManagerHome managerHomePage;
    private Reports reportsPage;

    /**
     * The width of the entire program. This is used for all pages.
     */
    public int width = 1200;
    /**
     * The height of the entire program. This is used for all pages.
     */
    public int height = 800;
  
    // initializing using constructor
    /**
     * Creates pages for each class (including login, manager, batches, etc)
     * The pages use a layout that allows us to switch between windows.
     */
    GUI() {  
        frame.setTitle("boba");
        frame.setSize(width,height);

        // quit program when window closes
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        loginPage = new Login(this);
        batches = new Batches(this);
        serverPage = new Server(this);
        managerPage = new Manager(this);
        reportsPage = new Reports(this);
        managerHomePage = new ManagerHome(this);

        // frame.add(this);
        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        // we can organize the different pages using card panels
        cardPanel.add(loginPage.getPanel(),"login");
        cardPanel.add(managerPage.getPanel(),"manager");
        cardPanel.add(serverPage.getPanel(),"server");
        cardPanel.add(reportsPage.getPanel(),"reports");
        cardPanel.add(managerHomePage.getPanel(),"managerhome");
        cardPanel.add(batches.getPanel(),"batches");

        frame.getContentPane().add(cardPanel, BorderLayout.CENTER);

        // now frame will be visible, by default it is not visible    
        frame.setVisible(true);  
    }
    /**
     * Uses a cardLayout to switch to another page given a name for said page
     * Note that this does not unload pages, and requires for pages to already have been made.
     * @param page the name to determine which page to switch to
     */
    void switchPage(String page) {
        cardLayout.show(cardPanel,page);
        System.out.println("opening page " + page);
    }

    // main method  
    public static void main(String args[]) {   
        // creating instance of Frame class
        new GUI();    
    }  
  
}    