import java.awt.event.ActionEvent;
import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class to interpret problem panel menu item selection
 * 
 * @author Alex Arrieta
 * @author Mario Vuletic
 * @author Chloe Anbarcioglu
 */
public class MenuListener extends ProblemPanelListener {

    /**
     * MenuListener constructor
     * @param deck A JPanel that contains all of the components
     * @param problemPanel A JPanel that contains of the problem panel components
     */
    public MenuListener(JPanel deck, ProblemPanel problemPanel){
        super(deck, problemPanel);
    }

    /**
     * Controls the behavior of the different options in the JMenu
     * @param e The action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case "Authors":
                JOptionPane.showMessageDialog(new JFrame(), 
                "Authors\n\nAlex Arrieta\nChloe Anbarcioglu\nThien An Tran\nMario Vuletic");
                break;
            case "Save":
                String name = JOptionPane.showInputDialog("Please put file name to save");
                SaveLoad.save(name);
                Blackboard.getInstance().getData("Project Saved");
                break;
            case "Load":
                JOptionPane.showMessageDialog(null, "All not saved data will be deleted");
                if(JOptionPane.showConfirmDialog(null, "Do you wish to load?") == 0) {
                    Blackboard.getInstance().clearClasses();
                    String load = JOptionPane.showInputDialog("Please put file name to load");
                    SaveLoad.load(load);
                    Blackboard.getInstance().getData("Project Loaded");
                }
                break;
            case "New":
                JOptionPane.showMessageDialog(null, "All not saved data will be deleted");
                if(JOptionPane.showConfirmDialog(null, "Do you wish to make a new canvas?") == 0) {
                    Blackboard.getInstance().clearClasses();
                    Blackboard.getInstance().getData("New project made");
                    Blackboard.getInstance().getMetrics().resetAllTimer();
                }
                break;
            case "Run code":
                Blackboard.getInstance().getData("Parsing Needed");
                break;
            case "Use hint":
                Blackboard.getInstance().getData("Parsing Needed");
                Feedback.getInstance().setFeedback(true);
                break;
            case "See metrics":
                Blackboard.getInstance().getMetrics().showMetrics();
                break;
            case "Generate problem":
                ProblemGenerator pg = new ProblemGenerator();
                try{
                    pg.generateProblem();
                } catch (Exception x) { JOptionPane.showMessageDialog(null, "Error generating problem. Please try again");}
                break;
            case "Change problem difficulty":
                HashMap<String, Integer> difficulties = new HashMap<>();
                difficulties.put("Easy", 1);
                difficulties.put("Medium", 2);
                difficulties.put("Hard", 3);
                String difficulty = selectOption(difficulties);
                if (difficulty == null) { return; } // User cancelled prompt
                Blackboard.getInstance().setProblemDifficulty(difficulties.get(difficulty));
                break;
            case "Change problem type":
                HashMap<String, Integer> problemTypes = new HashMap<>();
                problemTypes.put("Code -> UML", 1);
                problemTypes.put("UML -> Code", 2);
                problemTypes.put("UML -> Metrics", 3);
                String problemType = selectOption(problemTypes);
                if (problemType == null) { return; } // User cancelled prompt
                Blackboard.getInstance().setProblemType(problemTypes.get(problemType));
                break;
            case "Exit problem":
                problemPanel.setEditing(0);
                JOptionPane.showMessageDialog(null, "All unsaved changes will be deleted");
                if(JOptionPane.showConfirmDialog(null, "Do you wish exit this problem?") == 0) {
                    Blackboard.getInstance().clearClasses();
                    Blackboard.getInstance().getData("Exited Problem");
                    Feedback.getInstance().setRun(false);
                    if (Blackboard.getInstance().isProfessor()) {
                        ((ProfessorSelectPanel) deck.getComponent(3)).setUpProfessorSelect();
                        ((CardLayout)deck.getLayout()).show(deck, "Professor Select");
                    } else {
                        ((CardLayout)deck.getLayout()).show(deck, "Student Select");
                    }

                    Blackboard.getInstance().getMetrics().resetAllTimer();
                    Blackboard.getInstance().getMetrics().stopTimer();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Displays an option selection menu before problem submission to database
     * @param options : A HashMap of options for user selection to be arguments to the database insertion
     * @return the option selected by the user, or null if there was a cancellation
     */
    private String selectOption(HashMap<String, Integer> options) {
        List<String> optionNames = new ArrayList<>(options.keySet());
        Object selection = JOptionPane.showInputDialog(
                null,
                "Select an option",
                "Options",
                JOptionPane.PLAIN_MESSAGE,
                null,
                optionNames.toArray(),
                optionNames.get(0));
        return selection != null ? (String) selection : null;
    }
}
