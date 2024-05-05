import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/** The controlling class for the login page where users will enter their pin */
public class Login extends Page {
    private loginHandler loginHandler = new loginHandler();
    // inherets JPanel panel from Page which is where we display things

     private void handlePasswordInput(String enteredPassword) {
        int loginVal = loginHandler.tryLogin(enteredPassword);
        switch(loginVal){
            case 1:
                switchPage("managerhome");
                break;
            case 0:
                switchPage("server");
                break;
            default:
                showError("Incorrect login key submitted");
        }
    }



    // constuctor
    Login(GUI g) {
        super(g);

        panel.setBackground(new Color(155,165,185));

        JLabel l1=new JLabel("LOGIN");  
        l1.setBounds(alignCenter(width/2, height/2 - 100, 200, 60));
        // set the font to our custom font
        l1.setFont(font.deriveFont(Font.PLAIN, 60));
        l1.setHorizontalAlignment(SwingConstants.CENTER);

        JPasswordField passwordInput = new JPasswordField();
        passwordInput.setBounds(alignCenter(width/2,height/2,200,50));
        passwordInput.setFont(new Font("Arial", Font.BOLD, 20));
        passwordInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // if the user presses the enter key check if the password matches
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    char[] inputPassword = passwordInput.getPassword();

                    handlePasswordInput(new String(inputPassword));
                }
            }
        });

        panel.add(l1);
        panel.add(passwordInput);
    }
}
