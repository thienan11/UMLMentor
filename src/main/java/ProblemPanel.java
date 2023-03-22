import java.util.Observable;
import java.util.Observer;
import javax.swing.*;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class for the problem solving screen view
 * 
 * @author Mario Vuletic
 * @author Chloe Anbarcioglu
 * @version 1.0
 */
public class ProblemPanel extends JPanel implements Observer{
    private JTextArea problemText;
    private JSplitPane backdrop;
    private TextPanel textPanel;
    private DrawPanel drawPanel;
    private JButton submit;
    private JButton runCode;
    private JButton hint;
    private JButton exitProblem;
    private JButton autogenerate;
    private JMenuBar menu;
    private JPanel optionsPanel;
    private JButton deleteProblem;
    private JButton editProblem;
    private JButton seeMetrics;
    private JButton back;
    private JPanel deck;
    private JMenuItem metrics;
    private JMenu edit;
    private int editing = 0;
    
    /**
     * Creates a JPanel with components that make up the problem screen
     * @param deck A JPanel that contains all of the components
     */
    public ProblemPanel(JPanel deck){
        this.deck = deck;
        setLayout(new BorderLayout());

        menu = new JMenuBar();
        menu.setPreferredSize(new Dimension(1200, 40));
        MenuListener menuHandler = new MenuListener(deck, this);
        menu.setLayout(new BoxLayout(menu, BoxLayout.X_AXIS));

        optionsPanel = new JPanel(new FlowLayout(0, 0, 0));
        optionsPanel.setPreferredSize(new Dimension(1200, 40));

        deleteProblem = new JButton("Delete problem");
        deleteProblem.setPreferredSize(new Dimension(300, 40));
        editProblem = new JButton("Edit problem");
        editProblem.setPreferredSize(new Dimension(300, 40));
        seeMetrics = new JButton("See metrics");
        seeMetrics.setPreferredSize(new Dimension(300, 40));
        back = new JButton("Back");
        back.setPreferredSize(new Dimension(300, 40));

        deleteProblem.addActionListener(new EditHandler(deck, this));
        editProblem.addActionListener(new EditHandler(deck, this));
        seeMetrics.addActionListener(new EditHandler(deck, this));
        back.addActionListener(new EditHandler(deck, this));
    
        optionsPanel.add(deleteProblem);
        optionsPanel.add(editProblem);
        optionsPanel.add(seeMetrics);
        optionsPanel.add(back);

        DrawPanel drawPanel = new DrawPanel();
        TextPanel textPanel = new TextPanel();
        this.drawPanel = drawPanel;
        this.textPanel = textPanel;
        textPanel.setEditable(false);

        submit = new JButton("Save and submit");
        submit.addActionListener(new SubmissionListener(deck, this));

        autogenerate = new JButton("Generate problem");
        autogenerate.addActionListener(menuHandler);
        
        JPanel problemDescPanel = new JPanel(new BorderLayout());
        problemDescPanel.setBackground(Color.WHITE);
        problemDescPanel.setOpaque(true);
        problemText = new JTextArea();
        problemText.setText("[Problem Description Goes Here]");
        problemText.setLineWrap(true);
        problemText.setWrapStyleWord(true);
        problemText.setFont(new Font("Arial", Font.PLAIN, 15));
        problemText.setForeground(Color.BLACK);
        problemDescPanel.add(new JScrollPane(problemText), BorderLayout.CENTER);
        problemText.setEditable(false);

        JSplitPane problemAndGivenInfo = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        problemAndGivenInfo.setLeftComponent(problemDescPanel); 
        problemAndGivenInfo.setRightComponent(textPanel);
        
        JSplitPane problemAndSolution = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        problemAndSolution.setLeftComponent(problemAndGivenInfo);

        JPanel solvingPanel = new JPanel(new BorderLayout());
        solvingPanel.add(drawPanel, BorderLayout.CENTER);
        solvingPanel.add(submit, BorderLayout.SOUTH);

        problemAndSolution.setRightComponent(solvingPanel);
        this.backdrop = problemAndSolution;

        JMenu file = new JMenu("File");
        JMenuItem _new = new JMenuItem("New");
        JMenuItem save = new JMenuItem("Save");
        JMenuItem load = new JMenuItem("Load");
        metrics = new JMenuItem("See metrics");
        file.add(_new);
        file.add(save);
        file.add(load);
        file.add(metrics);
        _new.addActionListener(menuHandler);
        save.addActionListener(menuHandler);
        load.addActionListener(menuHandler);
        metrics.addActionListener(menuHandler);

        edit = new JMenu("Edit");
        JMenuItem changeDifficulty = new JMenuItem("Change problem difficulty");
        JMenuItem changeType = new JMenuItem("Change problem type");
        edit.add(changeDifficulty);
        edit.add(changeType);
        changeDifficulty.addActionListener(menuHandler);
        changeType.addActionListener(menuHandler);
    
        JMenu help = new JMenu("Help");
        JMenuItem authors = new JMenuItem("Authors");
        authors.addActionListener(menuHandler);
        help.add(authors);

        menu.add(file, BorderLayout.CENTER);
        menu.add(edit, BorderLayout.CENTER);
        menu.add(help, BorderLayout.CENTER);
        menu.add(Box.createHorizontalGlue());

        menu.add(autogenerate);
        autogenerate.setVisible(false);

        runCode = new JButton("Run code");
        runCode.addActionListener(menuHandler);
        menu.add(runCode);
        runCode.setVisible(false);
        
        hint = new JButton("Use hint");
        hint.addActionListener(menuHandler);
        menu.add(hint);

        exitProblem = new JButton("Exit problem");
        exitProblem.addActionListener(menuHandler);
        
        menu.add(exitProblem);
        
        StatusPanel statusPanel = new StatusPanel();

        Blackboard blackboard = Blackboard.getInstance();
        blackboard.addObserver(textPanel);
        blackboard.addObserver(drawPanel);
        blackboard.addObserver(statusPanel);
        blackboard.addObserver(this);
        blackboard.setMetrics(new Metrics());

        optionsPanel.add(menu);
        add(optionsPanel, BorderLayout.NORTH);
        add(problemAndSolution, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        resetSplitPanes();
        Blackboard.getInstance().setIsProfessor(false);

    }

    /**
     * Configures panel for student use
     */
    public void loadStudentVersion(){
        menu.setVisible(true);
        metrics.setVisible(true);
        edit.setVisible(false);
        deleteProblem.setVisible(false);
        editProblem.setVisible(false);
        seeMetrics.setVisible(false);
        back.setVisible(false);
        autogenerate.setVisible(false);
        runCode.setVisible(false);
        hint.setVisible(true);
        submit.setText("Save and submit");
        drawPanel.setEditable(true);
        textPanel.setEditable(false);
        exitProblem.setVisible(true);
        resetSplitPanes();
        Blackboard.getInstance().setIsProfessor(false);
    }

    /**
     * Orients the problem panel to the current state of the user and/or problem type selected
     * by virtue of the blackboard's current problem type state. Please set the proper problem
     * type in blackboard before calling this method.
     */
    public void orient(){
        int type = Blackboard.getInstance().getProblemType();
        Class<?> solving = ((Container) backdrop.getComponent(2)).getComponent(0).getClass();
        if (type == 1 && solving.equals(TextPanel.class)){
            DrawPanel dp = (DrawPanel) ((Container) (backdrop.getComponent(1))).getComponent(2);
            TextPanel tp = (TextPanel) ((Container) backdrop.getComponent(2)).getComponent(0);
            ((Container) backdrop.getComponent(2)).remove(0);
            ((Container) backdrop.getComponent(2)).add(dp, 0);
            ((JSplitPane) backdrop.getComponent(1)).setRightComponent(tp);
            tp.setEditable(Blackboard.getInstance().isProfessor());
            dp.setEditable(true);
        }
        else if (type == 2 && solving.equals(DrawPanel.class)){
            DrawPanel dp = (DrawPanel) ((Container) (backdrop.getComponent(2))).getComponent(0);
            TextPanel tp = (TextPanel) ((Container) backdrop.getComponent(1)).getComponent(2);
            ((Container) backdrop.getComponent(2)).remove(0);
            ((Container) backdrop.getComponent(2)).add(tp, 0);
            ((JSplitPane) backdrop.getComponent(1)).setRightComponent(dp);
            tp.setEditable(true);
            dp.setEditable(Blackboard.getInstance().isProfessor());
        }
        else if (type == 3){

        } else{}
        resetSplitPanes();
    }            

    /**
     * Configures panel for professor use
     */
    public void loadProfessorVersion(){
        if (editing == 1) {
            menu.setVisible(true);
            edit.setVisible(true);
            metrics.setVisible(false);
            deleteProblem.setVisible(false);
            editProblem.setVisible(false);
            seeMetrics.setVisible(false);
            back.setVisible(false);
            optionsPanel.setVisible(true);
            autogenerate.setVisible(true);
            runCode.setVisible(true);
            hint.setVisible(false);
            submit.setText("Save");
            submit.setVisible(true);
            problemText.setEditable(false);
            textPanel.setEditable(true);
            drawPanel.setEditable(true);
            exitProblem.setVisible(true);
            resetSplitPanes();
            Blackboard.getInstance().setIsProfessor(true);
        } else {
            menu.setVisible(false);
            deleteProblem.setVisible(true);
            editProblem.setVisible(true);
            seeMetrics.setVisible(true);
            back.setVisible(true);
            optionsPanel.setVisible(true);
            autogenerate.setVisible(false);
            runCode.setVisible(false);
            submit.setVisible(false);
            problemText.setEditable(false);
            textPanel.setEditable(false);
            drawPanel.setEditable(false);
            exitProblem.setVisible(false);
            resetSplitPanes();
            Blackboard.getInstance().setIsProfessor(true);
        }
    }

    /**
     * Configures editing screen for professors
     */
    public void loadProblemMaker(){
        drawPanel.setEditable(true);
        textPanel.setEditable(true);
        menu.setVisible(true);
        edit.setVisible(false);
        exitProblem.setVisible(true);
        metrics.setVisible(false);
        edit.setVisible(false);
        deleteProblem.setVisible(false);
        editProblem.setVisible(false);
        seeMetrics.setVisible(false);
        back.setVisible(false);
        submit.setText("Submit to database");
        submit.setVisible(true);
        problemText.setText("[You are currently creating a problem. After you click \"Submit to database\", you will be prompted to select the difficulty and problem type.]");
        runCode.setVisible(true);
        autogenerate.setVisible(true);
        hint.setVisible(false);   
        resetSplitPanes();
    }
    
    /**
     * Resizes the split panes
     */
    private void resetSplitPanes(){
        backdrop.setResizeWeight(0.1);
        ((JSplitPane) backdrop.getComponent(1)).revalidate();
    }

    /**
     * Sets editing to 1 if the edit button was selected, 0 if not
     * @param x An int value to set editing to
     */
    public void setEditing(int x) {
        editing = x;
    }

    /**
     * Sets the problem desciption in the top left panel
     * @param s : The problem's text
     */
    public void setProblemText(String s){
        problemText.setText(s);
    }

    /**
     * Gets the current problem panel's description
     * @return The text in the problem description box
     */
    public String getProblemText(){
        return this.problemText.getText();
    }

    /**
     * Gets the DrawPanel object
     * @return This ProblemPanel's DrawPanel instance
     */
    public DrawPanel getDrawPanel(){
        return this.drawPanel;
    }

    /**
     * Gets the TextPanel object
     * @return This ProblemPanel's TextPanel instance
     */
    public TextPanel getTextPanel(){
        return this.textPanel;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg == null) { return; }
        switch ((String) arg){
            case "Type changed":
                orient();
                break;
            case "Description changed":
                setProblemText(Blackboard.getInstance().getProblemDescription());
                break;
            case "Professor":
                loadProfessorVersion();
                break;
            default:
                break;
        }
    }
}
