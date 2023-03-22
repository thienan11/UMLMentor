import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * CSC 309 UML Tutor
 * Abstract controller class for the problem panel
 * 
 * @author Mario Vuletic
 */
public abstract class ProblemPanelListener implements ActionListener{
    protected JPanel deck;
    protected ProblemPanel problemPanel;
    
    /**
     * ProblemPanelListener constructor
     * @param deck A JPanel that contains all of the components
     * @param problemPanel A JPanel that contains of the problem panel components
     */
    public ProblemPanelListener(JPanel deck, ProblemPanel problemPanel){
        this.problemPanel = problemPanel;
        this.deck = deck;
    }
}
