import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.swing.*;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class for the student's problem select screen view
 * 
 * @author Chloe Anbarcioglu
 * @version 1.0
 */
public class StudentSelectPanel extends JPanel{
    public static ArrayList<JButton> currentProblems = new ArrayList<>();
    public static JScrollPane currentScrollPane;
    public static int type = 0;
    public static int difficulty = 0;
    public static int completion = 0;
    public static JComboBox userMenu;
    private JPanel deck;

    /**
     * Lays out the UI format for the problem select panel
     * @param deck The deck to use for the formatting
     */
    public StudentSelectPanel(JPanel deck){
        this.deck = deck;
    }

    public void setUpStudentSelect(){
        removeAll();
        setLayout(new FlowLayout(0, 0, 0));
        setBackground(Color.LIGHT_GRAY);

        ImageIcon easy = new ImageIcon(getClass().getResource("easy.png"));
        ImageIcon medium = new ImageIcon(getClass().getResource("medium.png"));
        ImageIcon hard = new ImageIcon(getClass().getResource("hard.png"));
        ImageIcon userIcon = new ImageIcon(getClass().getResource("user.png"));

        // top panel
        JPanel topPanel = new JPanel();
        topPanel.setPreferredSize(new Dimension(1200, 60));
        topPanel.setBackground(Color.GRAY);
        topPanel.setOpaque(true);
        String username = Blackboard.getInstance().getUserID();
        int nameWidth = 0;
        if (username.length() > 6) {
            nameWidth = username.length() + 20;
        }
        JLabel intro = new JLabel("UML Tutor", SwingConstants.LEFT);
        intro.setFont(new Font("Verdana", Font.PLAIN, 40));
        intro.setPreferredSize(new Dimension(1020 - nameWidth, 50));
        intro.setForeground(Color.BLACK);
        intro.setBackground(Color.GRAY);
        intro.setOpaque(true);
        JLabel user = new JLabel(userIcon);
        user.setPreferredSize(new Dimension(40, 50));
        user.setForeground(Color.BLACK);
        user.setBackground(Color.GRAY);
        user.setOpaque(true);
        String[] options = {username, "Logout"};
        JComboBox userMenu = new JComboBox<>(options);
        userMenu.setMaximumSize(new Dimension(100, 50));
        topPanel.add(intro);
        topPanel.add(user);
        topPanel.add(userMenu);
        add(topPanel);

        // title
        JLabel title = new JLabel("  Problems", SwingConstants.LEFT);
        title.setVerticalAlignment(SwingConstants.BOTTOM);
        title.setFont(new Font("Verdana", Font.PLAIN, 28));
        title.setForeground(Color.BLACK);
        title.setBackground(Color.LIGHT_GRAY);
        title.setOpaque(true);
        title.setPreferredSize(new Dimension(1200, 80));
        add(title);

        // gap
        JLabel gap1 = new JLabel();
        gap1.setPreferredSize(new Dimension(1200, 15));
        add(gap1);

        // type of problem buttons
        JButton codeUML = new JButton("Code -> UML");
        JButton umlCode = new JButton("UML -> Code");
        JButton umlMetrics = new JButton("UML -> Metrics");
        JButton allTypes = new JButton("All Types");
        codeUML.setPreferredSize(new Dimension(300, 30));
        umlCode.setPreferredSize(new Dimension(300, 30));
        umlMetrics.setPreferredSize(new Dimension(300, 30));
        allTypes.setPreferredSize(new Dimension(300, 30));
        add(codeUML);
        add(umlCode);
        add(umlMetrics);
        add(allTypes);

        // gap 2
        JLabel gap2 = new JLabel();
        gap2.setPreferredSize(new Dimension(1200, 15));
        add(gap2);

        // filter
        JLabel sort = new JLabel("Sort by: ", SwingConstants.RIGHT);
        sort.setPreferredSize(new Dimension(1000, 30));
        add(sort);
        String[] filterOptions = {"Problem Number", "Difficulty", "Completion"};
        JComboBox filter = new JComboBox<>(filterOptions);
        filter.setPreferredSize(new Dimension(200, 30));
        add(filter);

        // gap 3
        JLabel gap3 = new JLabel();
        gap3.setPreferredSize(new Dimension(1200, 15));
        add(gap3);

        // problems
        currentScrollPane = addProblems(easy, medium, hard);
        add(currentScrollPane);

        // action listeners
        userMenu.addActionListener(new StudentSelectListener(deck, this));
        filter.addActionListener(new StudentSelectListener(deck, this));
        codeUML.addActionListener(new StudentSelectListener(deck, this));
        umlCode.addActionListener(new StudentSelectListener(deck, this));
        umlMetrics.addActionListener(new StudentSelectListener(deck, this));
        allTypes.addActionListener(new StudentSelectListener(deck, this));

        for (int i = 0; i < currentProblems.size(); i++) {
            currentProblems.get(i).addActionListener(new StudentSelectListener(deck, this));
        }
    }

    /**
     * Adds a problem to the list of problems displayed
     * @param easy The icon for easy problems
     * @param medium The icon for medium problems
     * @param hard The icon for hard problems
     * @return The JScrollPane that contains all the buttons to select problems
     */
    public JScrollPane addProblems(ImageIcon easy, ImageIcon medium, ImageIcon hard) {
        currentProblems.clear();
        ArrayList<ArrayList<String>> rs;
        if (type == 1) {
            rs = DBHandler.getInstance().getCodeUMLProblems();
        } else if (type == 2) {
            rs = DBHandler.getInstance().getUMLCodeProblems();
        } else if (type == 3) {
            rs = DBHandler.getInstance().getUMLMetricsProblems(); 
        } else {
            rs = DBHandler.getInstance().getProblems();
        }

        for (int i = 0; i < rs.size(); i++) {
            int id = Integer.parseInt(rs.get(i).get(0));
            ArrayList<String> rss = DBHandler.getInstance().getCurrentStudentSolution(Blackboard.getInstance().getUserID(), id);

            if (rss == null) {
                if (rs.get(i).size() == 5) {
                    rs.get(i).add("1");
                } else if (rs.get(i).size() == 6) {
                    rs.get(i).set(5, "1");
                }
            } else {
                if (rs.get(i).size() == 5) {
                    rs.get(i).add(rss.get(1));
                } else if (rs.get(i).size() == 6) {
                    rs.get(i).set(5, rss.get(1));
                }
            }
        }

        if (difficulty == 1) {
            Collections.sort(rs, new Comparator<ArrayList<String>> () {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    return o1.get(3).compareTo(o2.get(3));
                }
            });
        } else if (completion == 1) {
            Collections.sort(rs, new Comparator<ArrayList<String>> () {
                @Override
                public int compare(ArrayList<String> o1, ArrayList<String> o2) {
                    return o1.get(5).compareTo(o2.get(5));
                }
            });
        }

        int height = 0;
        JPanel problemsPanel = new JPanel(new FlowLayout());

        for (int i = 0; i < rs.size(); i++) {
            int difficulty = Integer.parseInt(rs.get(i).get(3));
            int id = Integer.parseInt(rs.get(i).get(0));

            String solvedString;
            if (rs.get(i).get(5).equals("1")) {
                solvedString = "Not started";
            } else if (rs.get(i).get(5).equals("0")) {
                solvedString = "In progress";
            } else {
                solvedString = "Finished";
            }

            if (difficulty == 1) {
                JButton problem = new JButton(easy);
                problem.setFont(new Font("Arial", Font.PLAIN, 20));
                String description = String.format("%-130s %-15s", "   Problem " + id, solvedString);
                problem.setText(description);
                problem.setPreferredSize(new Dimension(1100, 100));
                problemsPanel.add(problem);
                currentProblems.add(problem);
                height += 110;
            } else if (difficulty == 2) {
                JButton problem = new JButton(medium);
                problem.setFont(new Font("Arial", Font.PLAIN, 20));
                String description = String.format("%-130s %-15s", "   Problem " + id, solvedString);
                problem.setText(description);
                problem.setPreferredSize(new Dimension(1100, 100));
                problemsPanel.add(problem);
                currentProblems.add(problem);
                height += 110;
            } else if (difficulty == 3) {
                JButton problem = new JButton(hard);
                problem.setFont(new Font("Arial", Font.PLAIN, 20));
                String description = String.format("%-130s %-15s", "   Problem " + id, solvedString);
                problem.setText(description);
                problem.setPreferredSize(new Dimension(1100, 100));
                problemsPanel.add(problem);
                currentProblems.add(problem);
                height += 110;
            }
        }

        problemsPanel.setPreferredSize(new Dimension(1100, height));
        JScrollPane scrollFrame = new JScrollPane(problemsPanel);
        scrollFrame.getVerticalScrollBar().setUnitIncrement(16);
        scrollFrame.setPreferredSize(new Dimension(1200,535));
        return scrollFrame;
    }

    /**
     * Sets the problem dificulty
     * @param x An int to set the difficulty to
     */
    public void setDifficulty(int x) {
        difficulty = x;
    }

    /**
     * Sets the completion status
     * @param x An int to set the completion status to
     */
    public void setCompletion(int x) {
        completion = x;
    }

    /**
     * Gets the current scroll pane
     * @return The current scroll pane
     */
    public JScrollPane getCurrentScrollPane() {
        return currentScrollPane;
    }

    /**
     * Sets the current scroll pane
     * @param pane The JSrollPane to set the current scroll pane to
     */
    public void setCurrentScrollPane(JScrollPane pane) {
        currentScrollPane = pane;
    }

    /**
     * Gets the current problems
     * @return The list of current problems
     */
    public ArrayList<JButton> getCurrentProblems() {
        return currentProblems;
    }

    /**
     * Sets the problem type
     * @param x An int to set the type to
     */
    public void setType(int x) {
        type = x;
    }
}