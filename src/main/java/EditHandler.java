import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class to interpret the edit selection in the menu
 *
 * @author Chloe Anbarcioglu
 * @version 1.0
 */
public class EditHandler extends ProblemPanelListener {

    /**
     * EditHandler constructor
     * @param deck A JPanel that contains all of the components
     * @param problemPanel A JPanel that contains of the problem panel components
     */
    public EditHandler(JPanel deck, ProblemPanel problemPanel) {
        super(deck, problemPanel);
    }

    /**
     * Controls the behavior of the different options in the edit tab in the JMenu
     * @param e An action event
     */
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "Delete problem":
                boolean success = DBHandler.getInstance().deleteProblem();
                Blackboard.getInstance().clearClasses();
                Blackboard.getInstance().getData("Exited Problem");
                if (success) {
                    JOptionPane.showMessageDialog(null, "Problem successfully deleted");
                    ((ProfessorSelectPanel) deck.getComponent(3)).setUpProfessorSelect();
                    ((CardLayout)deck.getLayout()).show(deck, "Professor Select");
                }

                Blackboard.getInstance().getMetrics().resetAllTimer();
                Blackboard.getInstance().getMetrics().stopTimer();
                break;
            case "Edit problem":
                problemPanel.setEditing(1);
                problemPanel.loadProfessorVersion();
                problemPanel.revalidate();
                break;
            case "See metrics":
                JTable t = DBHandler.getInstance().getAllStudentsProblemMetric(Blackboard.getInstance().getProblemId());
                JScrollPane scrollP = new JScrollPane();
                JFrame frame = new JFrame();
                JPanel p = new JPanel();
                p.add(scrollP);
                scrollP.setViewportView(t);
                frame.add(p);
                frame.setSize(600, 150);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
                break;
            case "Back":
                Blackboard.getInstance().clearClasses();
                Blackboard.getInstance().getData("Exited Problem");
                ((CardLayout)deck.getLayout()).show(deck, "Professor Select");

                // send data to database??
                Blackboard.getInstance().getMetrics().resetAllTimer();
                Blackboard.getInstance().getMetrics().stopTimer();
                break;
        }
    }
}
