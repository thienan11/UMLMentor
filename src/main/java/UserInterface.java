import java.awt.*;
import javax.swing.*;
import java.util.HashMap;

/**
 * CSC 309 UML Tutor
 * Class to initialize and manipulate user interface
 * 
 * @author Chloe Anbarcioglu
 * @author Mario Vuletic
 * @version 1.0
 */
public class UserInterface extends JFrame{
    private HashMap<String, Integer> screens;
    private JPanel deck;

    /**
     * Sets up CardLayout to control which screen is displayed
     */
    public UserInterface(){
        super("UML Tutor");
        screens = new HashMap<>();
        deck = new JPanel(new CardLayout());
        deck.add(new StudentSelectPanel(deck), "Student Select");
        deck.add(new ProblemPanel(deck), "Problem");
        deck.add(new LoginMenu(deck), "Login");
        deck.add(new ProfessorSelectPanel(deck), "Professor Select");
        screens.put("Student Select", 0);
        screens.put("Problem", 1);
        screens.put("Login", 2);
        screens.put("Professor Select", 3);

        ((CardLayout)deck.getLayout()).show(deck, "Login"); // show a panel

        add(deck);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setVisible(true);
        setResizable(false);
    }

    /**
     * Gets the panel object in the CardLayout
     * @param panelName : The name associated with the panel: Options: "Problem", "Problem Select"
     * @return The component object specified by panelName
     */
    public Component getPanel(String panelName){
        return deck.getComponent(screens.get(panelName));
    }
}
