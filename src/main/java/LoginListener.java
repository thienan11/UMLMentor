import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class to interpret login menu item selection
 *
 * @author Chloe Anbarcioglu
 * @author Thien An Tran
 * @version 2.0
 */
public class LoginListener extends JPanel implements ActionListener {
    protected JPanel deck;
    protected LoginMenu loginMenu;

    /**
     * 
     * @param deck A JPanel that contains all of the components
     * @param loginMenu A JPanel that contains all of the login menu components
     */
    public LoginListener(JPanel deck, LoginMenu loginMenu) {
        this.loginMenu = loginMenu;
        this.deck = deck;
    }

    /**
     * Performs actions based on inputs from login and register panels of LoginMenu
     * @param e the event to be processed
     */
    public void actionPerformed(ActionEvent e) {
        String username;
        String password;;
        String opt;
        String accType;

        switch (e.getActionCommand()) {
            case "Login":
                username = loginMenu.getLPIdField().getText();
                password = String.valueOf(loginMenu.getLPPassField().getPassword());
                opt = (String) loginMenu.getLPAccOptionsBox().getSelectedItem();
                accType = DBHandler.getInstance().getAccType(username, password);

                if(DBHandler.getInstance().login(username, password)){
                    switch (opt){
                        case "Student":
                            if(accType.equals("Student")) {
                                Blackboard.getInstance().setUserID(username);
                                JOptionPane.showMessageDialog(this, "Login successful!\nWelcome back " + username);
                                loginMenu.getLPIdField().setText("");
                                loginMenu.getLPPassField().setText("");
                                ((ProblemPanel) deck.getComponent(1)).loadStudentVersion();
                                ((StudentSelectPanel) deck.getComponent(0)).setUpStudentSelect();
                                ((CardLayout) deck.getLayout()).show(deck, "Student Select");
                            }
                            else{
                                JOptionPane.showMessageDialog(this, "Student account doesn't exist");
                            }

                            break;
                        case "Professor":
                            if(accType.equals("Professor")) {
                                Blackboard.getInstance().setUserID(username);
                                loginMenu.getLPIdField().setText("");
                                loginMenu.getLPPassField().setText("");
                                // OPENS PROFESSOR VERSION
                                JOptionPane.showMessageDialog(this, "Login successful!\nHello Professor");
                                ((ProblemPanel) deck.getComponent(1)).loadProfessorVersion();
                                ((ProfessorSelectPanel) deck.getComponent(3)).setUpProfessorSelect();
                                ((CardLayout) deck.getLayout()).show(deck, "Professor Select");
                            }
                            else{
                                JOptionPane.showMessageDialog(this, "Professor account doesn't exist");
                            }
                            break;
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "Invalid username or password!\nTry again.");
                }
                break;
            case "Register":
                loginMenu.getCL().show(loginMenu, "2");
                loginMenu.getLPIdField().setText("");
                loginMenu.getLPPassField().setText("");
                break;
            case "Create Account":
                username = loginMenu.getRPIdField().getText();
                password = String.valueOf(loginMenu.getRPPassField().getPassword());
                opt = (String) loginMenu.getRPAccOptionsBox().getSelectedItem();
    
                switch(opt){
                    case "Student":
                        if(DBHandler.getInstance().createStudentAccount(username, password)){
                            JOptionPane.showMessageDialog(this, "Successfully registered as Student!");
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "Username already exists!\nTry again.");
                        }
                        break;
                    case "Professor":
                        if(DBHandler.getInstance().createProfAccount(username, password)){
                            JOptionPane.showMessageDialog(this, "Successfully registered as Professor!");
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "Username already exists!\nTry again.");
                        }
                        break;
                }
                break;
            case "Cancel":
                loginMenu.getCL().show(loginMenu, "1");
                loginMenu.getRPIdField().setText("");
                loginMenu.getRPPassField().setText("");
                break;     
        }
    }
}
