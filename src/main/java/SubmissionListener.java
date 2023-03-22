import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.JOptionPane;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * CSC 309 UML Tutor
 * This class listens for when the user wants to submit their work, or for the professor to submit a problem
 * 
 * @author Mario Vuletic
 */
public class SubmissionListener extends ProblemPanelListener{

    /**
     * SubmissionListener constructor
     * @param deck A JPanel that contains all of the components
     * @param problemPanel A JPanel that contains of the problem panel components
     */
    public SubmissionListener(JPanel deck, ProblemPanel problemPanel) {
        super(deck, problemPanel);
    }

    /**
     * Submits the solution to be processed
     * @param e the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        Blackboard.getInstance().getData("Parsing Needed");
        switch (e.getActionCommand()){
            case "Save and submit":  
                ProblemSolutionDiff p  = new ProblemSolutionDiff();
                int correct = 1;
                if(Blackboard.getInstance().getClasses().isEmpty()){
                    JOptionPane.showMessageDialog(null, "Incorrect");
                }
                else if(p.diffOneSpecificity().get(0).isEmpty()){
                    JOptionPane.showMessageDialog(null, "Correct!");
                    correct = 2;
                }
                else {
                    JOptionPane.showMessageDialog(null, "Incorrect");
                    correct = 0;
                }
                Blackboard.getInstance().getMetrics().incAttemptCount();
                DBHandler.getInstance().insertUserMetrics();
                DBHandler.getInstance().addStudentSolution(correct);
                Blackboard.getInstance().getMetrics().resetAttemptTimer();
                break;
            case "Submit to database":
                if (Blackboard.getInstance().getClasses().equals(new HashMap<>())){ JOptionPane.showMessageDialog(null, "Parsing error");return; }
                HashMap<String, Integer> difficulties = new HashMap<>();
                difficulties.put("Easy", 1);
                difficulties.put("Medium", 2);
                difficulties.put("Hard", 3);
                String difficulty = selectOption(difficulties);
                if (difficulty == null) { return; } // User cancelled prompt
                HashMap<String, Integer> problemTypes = new HashMap<>();
                problemTypes.put("UML -> Code", 2);
                problemTypes.put("Code -> UML", 1);
                problemTypes.put("UML -> Metrics", 3);
                String problemType = selectOption(problemTypes);
                if (problemType == null) { return; } // User cancelled prompt
                DBHandler.getInstance().insertNewProblem(difficulties.get(difficulty), problemTypes.get(problemType));
                JOptionPane.showMessageDialog(null, "Problem successfully submitted");
                break;
            case "Save":
                if (Blackboard.getInstance().getClasses().equals(new HashMap<>())){ JOptionPane.showMessageDialog(null, "Parsing error");return; }
                DBHandler.getInstance().updateProblem(Blackboard.getInstance().getProblemDifficulty(), Blackboard.getInstance().getProblemType());
                JOptionPane.showMessageDialog(null, "Problem successfully updated");
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
    
