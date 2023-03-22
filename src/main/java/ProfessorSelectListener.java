import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.awt.*;

public class ProfessorSelectListener extends JPanel implements ActionListener {
    protected JPanel deck;
    protected ProfessorSelectPanel professorSelectPanel;

    /**
     * ProfessorSelectListener constructor
     * @param deck A JPanel that contains all of the components
     * @param professorSelectPanel A JPanel that contains of the professor select components
     */
    public ProfessorSelectListener(JPanel deck, ProfessorSelectPanel professorSelectPanel) {
        this.professorSelectPanel = professorSelectPanel;
        this.deck = deck;
    }

    /**
     * Controls the behavior of the different options in the professor select screen
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        JScrollPane currentScrollPane;
        ArrayList<JButton> currentProblems;
        JButton addProblem;

        ImageIcon easy = new ImageIcon(getClass().getResource("easy.png"));
        ImageIcon medium = new ImageIcon(getClass().getResource("medium.png"));
        ImageIcon hard = new ImageIcon(getClass().getResource("hard.png"));
        ImageIcon plus = new ImageIcon(getClass().getResource("plus.png"));

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
                    professorSelectPanel.setDifficulty(0);
                    professorSelectPanel.remove(professorSelectPanel.getCurrentScrollPane());
                    currentScrollPane = professorSelectPanel.addProblems(easy, medium, hard, plus);
                    professorSelectPanel.setCurrentScrollPane(currentScrollPane);
                    professorSelectPanel.add(currentScrollPane);
                    currentProblems = professorSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    }
                    addProblem = professorSelectPanel.getAddProblem();
                    addProblem.addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    professorSelectPanel.revalidate();
                    break;
                case "Difficulty":
                    professorSelectPanel.setDifficulty(1);
                    professorSelectPanel.remove(professorSelectPanel.getCurrentScrollPane());
                    currentScrollPane = professorSelectPanel.addProblems(easy, medium, hard, plus);
                    professorSelectPanel.setCurrentScrollPane(currentScrollPane);
                    professorSelectPanel.add(currentScrollPane);
                    currentProblems = professorSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    }
                    addProblem = professorSelectPanel.getAddProblem();
                    addProblem.addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    professorSelectPanel.revalidate();
                    break;
            }
        } else if (e.getActionCommand().equals(buttonText)) {
            switch (buttonText) {
                case "Code -> UML":
                    professorSelectPanel.setType(1);
                    professorSelectPanel.remove(professorSelectPanel.getCurrentScrollPane());
                    currentScrollPane = professorSelectPanel.addProblems(easy, medium, hard, plus);
                    professorSelectPanel.setCurrentScrollPane(currentScrollPane);
                    professorSelectPanel.add(currentScrollPane);
                    currentProblems = professorSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    }
                    addProblem = professorSelectPanel.getAddProblem();
                    addProblem.addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    professorSelectPanel.revalidate();
                    break;
                case "UML -> Code":
                    professorSelectPanel.setType(2);
                    professorSelectPanel.remove(professorSelectPanel.getCurrentScrollPane());
                    currentScrollPane = professorSelectPanel.addProblems(easy, medium, hard, plus);
                    professorSelectPanel.setCurrentScrollPane(currentScrollPane);
                    professorSelectPanel.add(currentScrollPane);
                    currentProblems = professorSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    }
                    addProblem = professorSelectPanel.getAddProblem();
                    addProblem.addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    professorSelectPanel.revalidate();
                    break;
                case "UML -> Metrics":
                    professorSelectPanel.setType(3);
                    professorSelectPanel.remove(professorSelectPanel.getCurrentScrollPane());
                    currentScrollPane = professorSelectPanel.addProblems(easy, medium, hard, plus);
                    professorSelectPanel.setCurrentScrollPane(currentScrollPane);
                    professorSelectPanel.add(currentScrollPane);
                    currentProblems = professorSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    }
                    addProblem = professorSelectPanel.getAddProblem();
                    addProblem.addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    professorSelectPanel.revalidate();
                    break;
                case "All Types":
                    professorSelectPanel.setType(0);
                    professorSelectPanel.remove(professorSelectPanel.getCurrentScrollPane());
                    currentScrollPane = professorSelectPanel.addProblems(easy, medium, hard, plus);
                    professorSelectPanel.setCurrentScrollPane(currentScrollPane);
                    professorSelectPanel.add(currentScrollPane);
                    currentProblems = professorSelectPanel.getCurrentProblems();
                    for (int i = 0; i < currentProblems.size(); i++) {
                        currentProblems.get(i).addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    }
                    addProblem = professorSelectPanel.getAddProblem();
                    addProblem.addActionListener(new ProfessorSelectListener(deck, professorSelectPanel));
                    professorSelectPanel.revalidate();
                    break;
                case "":
                    ((ProblemPanel) deck.getComponent(1)).loadProblemMaker();
                    ((CardLayout) deck.getLayout()).show(deck, "Problem");
                    Blackboard.getInstance().setProblemDescription("[You are currently creating a problem. After you click \"Submit to database\", you will be prompted to select the difficulty and problem type.]");
                    break;
                default:
                    int problemNum = Integer.parseInt(e.getActionCommand().trim().split(" ")[1]);
                    DBHandler.getInstance().queryProblemById(problemNum);
                    ((ProblemPanel) deck.getComponent(1)).loadProfessorVersion();
                    ((CardLayout)deck.getLayout()).show(deck, "Problem");
                    Blackboard.getInstance().setMetrics(new Metrics());
                    Blackboard.getInstance().getMetrics().startTimer();
                    break;
            }
        } 
    }
}

