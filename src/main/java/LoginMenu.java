import javax.swing.*;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class for UI of menu for logging in or registering
 *
 * @author Thien An Tran
 * @version 2.0
 */
public class LoginMenu extends JPanel{
    private JPanel loginPanel = new JPanel();
    private JLabel LPTitleLabel = new JLabel("UML Tutor", SwingConstants.CENTER);
    private JLabel LPIdLabel = new JLabel("Username", SwingConstants.LEFT);
    private JTextField LPIdField = new JTextField();
    private JLabel LPPassLabel = new JLabel("Password", SwingConstants.LEFT);
    private JPasswordField LPPassField = new JPasswordField();
    private JButton LPLoginButton = new JButton("Login");
    private JLabel LPAccField = new JLabel("Don't have an account?", SwingConstants.CENTER);
    private JButton LPRegisterButton = new JButton("Register");

    private JPanel regPanel = new JPanel();
    private JLabel RPTitleLabel = new JLabel("Create Account", SwingConstants.CENTER);
    private JLabel RPIdLabel = new JLabel("Username", SwingConstants.LEFT);
    private JTextField RPIdField = new JTextField();
    private JLabel RPPassLabel = new JLabel("Password", SwingConstants.LEFT);
    private JPasswordField RPPassField = new JPasswordField();
    private JButton RPRegisterButton = new JButton("Create Account");
    private JButton RPCancelButton = new JButton("Cancel");

    private CardLayout cl = new CardLayout();
    private JPanel deck;
    private String[] accOptions = {"Student", "Professor"};
    private JComboBox LPAccOptionsBox = new JComboBox(accOptions);
    private JComboBox RPAccOptionsBox = new JComboBox(accOptions);

    /**
     * Creates a JFrame for a login menu
     * @param deck A JPanel that contains all of the components
     */
    public LoginMenu(JPanel deck){
        this.deck = deck;
        setLayout(cl);
        loginPanel(deck);
        registerPanel(deck);

        add(loginPanel, "1");
        add(regPanel, "2");
        cl.show(this, "1");
    }

    /**
     * Sets the attributes and creates a login panel
     */
    public void loginPanel(JPanel deck){
        loginPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 125));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        centerPanel.setPreferredSize(new Dimension(500, 500));
        centerPanel.setBackground(Color.LIGHT_GRAY);
        centerPanel.setOpaque(true);

        LPTitleLabel.setFont(new Font("Verdana", Font.PLAIN, 36));
        LPTitleLabel.setPreferredSize(new Dimension(500, 60));
        centerPanel.add(LPTitleLabel);
        
        LPAccOptionsBox.setFont(new Font("Verdana", Font.PLAIN, 15));
        centerPanel.add(LPAccOptionsBox);

        LPIdLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        LPIdLabel.setPreferredSize(new Dimension(400, 30));
        centerPanel.add(LPIdLabel);

        LPIdField.setPreferredSize(new Dimension(400, 40));
        centerPanel.add(LPIdField);

        LPPassLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        LPPassLabel.setPreferredSize(new Dimension(400, 30));
        centerPanel.add(LPPassLabel);
        LPPassField.setPreferredSize(new Dimension(400, 40));
        centerPanel.add(LPPassField);

        LPLoginButton.setPreferredSize(new Dimension(400, 40));
        centerPanel.add(LPLoginButton);

        LPAccField.setFont(new Font("Verdana", Font.PLAIN, 16));
        LPAccField.setPreferredSize(new Dimension(500, 30));
        centerPanel.add(LPAccField);

        LPRegisterButton.setPreferredSize(new Dimension(100, 40));
        centerPanel.add(LPRegisterButton);
        
        loginPanel.add(centerPanel);

        LPLoginButton.addActionListener(new LoginListener(deck, this));
        LPRegisterButton.addActionListener(new LoginListener(deck, this));
        LPAccOptionsBox.addActionListener(new LoginListener(deck, this));
    }

    /**
     * Sets the attributes and create a register panel
     */
    public void registerPanel(JPanel deck){
        regPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 0, 125));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 15));
        centerPanel.setPreferredSize(new Dimension(500, 500));
        centerPanel.setBackground(Color.LIGHT_GRAY);
        centerPanel.setOpaque(true);

        RPTitleLabel.setFont(new Font("Verdana", Font.PLAIN, 36));
        RPTitleLabel.setPreferredSize(new Dimension(500, 80));
        centerPanel.add(RPTitleLabel);
        
        RPAccOptionsBox.setFont(new Font("Verdana", Font.PLAIN, 15));
        centerPanel.add(RPAccOptionsBox);

        RPIdLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        RPIdLabel.setPreferredSize(new Dimension(400, 30));
        centerPanel.add(RPIdLabel);

        RPIdField.setPreferredSize(new Dimension(400, 40));
        centerPanel.add(RPIdField);

        RPPassLabel.setFont(new Font("Verdana", Font.PLAIN, 20));
        RPPassLabel.setPreferredSize(new Dimension(400, 30));
        centerPanel.add(RPPassLabel);
        RPPassField.setPreferredSize(new Dimension(400, 40));
        centerPanel.add(RPPassField);

        RPRegisterButton.setPreferredSize(new Dimension(400, 40));
        centerPanel.add(RPRegisterButton);

        RPCancelButton.setPreferredSize(new Dimension(150, 40));
        centerPanel.add(RPCancelButton);

        RPRegisterButton.addActionListener(new LoginListener(deck, this));
        RPCancelButton.addActionListener(new LoginListener(deck, this));
        RPAccOptionsBox.addActionListener(new LoginListener(deck, this));

        regPanel.add(centerPanel);
    }

    /**
     * Gets the text field for typed username of the login page
     * @return JTextField of the inputted username
     */
    public JTextField getLPIdField() {
        return LPIdField;
    }

    /**
     * Gets the password field for typed password of the login page
     * @return JPasswordField of the inputted password
     */
    public JPasswordField getLPPassField() {
        return LPPassField;
    }

    /**
     * Gets the JComboBox containing the account types for the login page
     * @return JComboBox containing the account types
     */
    public JComboBox getLPAccOptionsBox() {
        return LPAccOptionsBox;
    }

    /**
     * Gets the CardLayout of login menu
     * @return CardLayout for login menu
     */
    public CardLayout getCL() {
        return cl;
    }

    /**
     * Gets the text field for typed username of the register page
     * @return JTextField of the inputted username
     */
    public JTextField getRPIdField() {
        return RPIdField;
    }

    /**
     * Gets the password field for typed password of the register page
     * @return JPasswordField of the inputted password
     */
    public JPasswordField getRPPassField() {
        return RPPassField;
    }

    /**
     * Gets the JComboBox containing the account types for the register page
     * @return JComboBox containing the account types
     */
    public JComboBox getRPAccOptionsBox() {
        return RPAccOptionsBox;
    }
}