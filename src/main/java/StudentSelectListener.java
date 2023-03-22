import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class to interpret student select panel item selection
 * 
 * @author Chloe Anbarcioglu
 * @version 1.0
 */
public class StudentSelectListener extends JPanel implements ActionListener {
    protected JPanel deck;
    protected StudentSelectPanel studentSelectPanel;

    /**
     * StudentSelectListener constructor
     * @param deck A JPanel that contains all of the components
     * @param studentSelectPanel A JPanel that contains of the student select components
     */
    public StudentSelectListener(JPanel deck, StudentSelectPanel studentSelectPanel) {
        this.studentSelectPanel = studentSelectPanel;
        this.deck = deck;
    }

    /**
     * Controls the behavior of the different options in the student select screen
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JScrollPane currentScrollPane;
        ArrayList<JButton> currentProblems;

        ImageIcon easy = new ImageIcon(getClass().getResource("easy.png"));
        ImageIcon medium = new ImageIcon(getClass().getResource("medium.png"));
        ImageIcon hard = new ImageIcon(getClass().getResource("hard.png"));

        String buttonText = "";
        if (e.getSource() instanceof JButton) {
            buttonText = (String) ((JButton) e.getSource()).getText();
        }

        if (e.getActionCommand().equals("comboBoxChanged")) {
            String selected = (String) ((JComboBox) e.getSource()).getSelectedItem();
            switch (selected) {
                case "Logout":
                    Blackboard.getInstance().setUserID("");
                    ((CardLayout)deck.getLayout()).show(deck, "Login");
                case "Problem Number":
                    studentSelectPanel.setDifficulty(0);
                    studentSelectPanel.setCompletion(0);
                    studentSelectPanel.remove(studentSelectPanel.getCurrentScrollPane());
                    currentScrollPane = studentSelectPanel.addProblems(easy, medium, hard);
                    studentSelectPanel.setCurrentScrollPane(currentScrollPane);
                    studentSelectPanel.add(currentScrollPane);
                    currentProblems = studentSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new StudentSelectListener(deck, studentSelectPanel));
                    }
                    studentSelectPanel.revalidate();
                    break;
                case "Difficulty":
                    studentSelectPanel.setDifficulty(1);
                    studentSelectPanel.setCompletion(0);
                    studentSelectPanel.remove(studentSelectPanel.getCurrentScrollPane());
                    currentScrollPane = studentSelectPanel.addProblems(easy, medium, hard);
                    studentSelectPanel.setCurrentScrollPane(currentScrollPane);
                    studentSelectPanel.add(currentScrollPane);
                    currentProblems = studentSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new StudentSelectListener(deck, studentSelectPanel));
                    }
                    studentSelectPanel.revalidate();
                    break;
                case "Completion":
                    studentSelectPanel.setDifficulty(0);
                    studentSelectPanel.setCompletion(1);
                    studentSelectPanel.remove(studentSelectPanel.getCurrentScrollPane());
                    currentScrollPane = studentSelectPanel.addProblems(easy, medium, hard);
                    studentSelectPanel.setCurrentScrollPane(currentScrollPane);
                    studentSelectPanel.add(currentScrollPane);
                    currentProblems = studentSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new StudentSelectListener(deck, studentSelectPanel));
                    }
                    studentSelectPanel.revalidate();
                    break;
            }
        } else if (e.getActionCommand().equals(buttonText)) {
            switch (buttonText) {
                case "Code -> UML":
                    studentSelectPanel.setType(1);
                    studentSelectPanel.remove(studentSelectPanel.getCurrentScrollPane());
                    currentScrollPane = studentSelectPanel.addProblems(easy, medium, hard);
                    studentSelectPanel.setCurrentScrollPane(currentScrollPane);
                    studentSelectPanel.add(currentScrollPane);
                    currentProblems = studentSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new StudentSelectListener(deck, studentSelectPanel));
                    }
                    studentSelectPanel.revalidate();
                    break;
                case "UML -> Code":
                    studentSelectPanel.setType(2);
                    studentSelectPanel.remove(studentSelectPanel.getCurrentScrollPane());
                    currentScrollPane = studentSelectPanel.addProblems(easy, medium, hard);
                    studentSelectPanel.setCurrentScrollPane(currentScrollPane);
                    studentSelectPanel.add(currentScrollPane);
                    currentProblems = studentSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new StudentSelectListener(deck, studentSelectPanel));
                    }
                    studentSelectPanel.revalidate();
                    break;
                case "UML -> Metrics":
                    studentSelectPanel.setType(3);
                    studentSelectPanel.remove(studentSelectPanel.getCurrentScrollPane());
                    currentScrollPane = studentSelectPanel.addProblems(easy, medium, hard);
                    studentSelectPanel.setCurrentScrollPane(currentScrollPane);
                    studentSelectPanel.add(currentScrollPane);
                    currentProblems = studentSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new StudentSelectListener(deck, studentSelectPanel));
                    }
                    studentSelectPanel.revalidate();
                    break;
                case "All Types":
                    studentSelectPanel.setType(0);
                    studentSelectPanel.remove(studentSelectPanel.getCurrentScrollPane());
                    currentScrollPane = studentSelectPanel.addProblems(easy, medium, hard);
                    studentSelectPanel.setCurrentScrollPane(currentScrollPane);
                    studentSelectPanel.add(currentScrollPane);
                    currentProblems = studentSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new StudentSelectListener(deck, studentSelectPanel));
                    }
                    studentSelectPanel.revalidate();
                    break;
                default:
                    int problemNum = Integer.parseInt(e.getActionCommand().trim().split(" ")[1]);
                    DBHandler.getInstance().queryProblemById(problemNum);
                    ((CardLayout)deck.getLayout()).show(deck, "Problem");
                    Blackboard.getInstance().setMetrics(new Metrics());
                    Blackboard.getInstance().getMetrics().startTimer();
                    new Thread(Feedback.getInstance()).start();
                    break;
            }
        } 
    } 
}
