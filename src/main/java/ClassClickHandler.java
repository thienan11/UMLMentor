import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * CSC 309 UML Tutor
 * This class handles the menu and action listening for when a class is right clicked
 * 
 * @author Alexander Arrieta
 * @version 1.0
 */
public class ClassClickHandler extends JFrame implements ActionListener {
    private JPanel panel = new JPanel();
    private JButton deleteClass = new JButton("Delete Class");
    private JButton addMethod = new JButton("Add Method");
    private JButton deleteMethod = new JButton("Delete Method");
    private JButton addGlobal = new JButton("Add Global");
    private JButton deleteGlobal = new JButton("Delete Global");
    private JButton addVariable = new JButton("Add Variable");
    private JButton deleteVariable = new JButton("Delete Variable");
    private JButton addParameter = new JButton("Add Parameter");
    private JButton deleteParameter = new JButton("Delete Parameter");
    private UserClass clicked;
    private ButtonGroup buttons = new ButtonGroup();

    /**
     * Creates a JPanel for a menu with different options to edit a class
     * @param clicked A UserClass that was clicked
     */
    public ClassClickHandler(UserClass clicked){
        this.clicked = clicked;

        buttons.add(deleteClass);
        buttons.add(addMethod);
        buttons.add(deleteMethod);
        buttons.add(addGlobal);
        buttons.add(deleteGlobal);
        buttons.add(addVariable);
        buttons.add(deleteVariable);
        buttons.add(addParameter);
        buttons.add(deleteParameter);

        deleteClass.addActionListener(this);
        addMethod.addActionListener(this);
        deleteMethod.addActionListener(this);
        addGlobal.addActionListener(this);
        deleteGlobal.addActionListener(this);
        addVariable.addActionListener(this);
        deleteVariable.addActionListener(this);
        addParameter.addActionListener(this);
        deleteParameter.addActionListener(this);

        panel.setLayout(new GridLayout(5,2));
        panel.add(deleteClass);
        panel.add(addMethod);
        panel.add(deleteMethod);
        panel.add(addVariable);
        panel.add(deleteVariable);
        panel.add(addGlobal);
        panel.add(deleteGlobal);
        panel.add(addParameter);
        panel.add(deleteParameter);
        this.add(panel);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(600, 400);
        this.setVisible(true);
    }

    /**
     * Controls the behavior of the the different options in the menu
     * @param e An action event
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Delete Class")){
            deleteClass();
        }
        if(e.getActionCommand().equals("Add Method")){
            String name = JOptionPane.showInputDialog("What is the name of the method you want to add");
            if(name == null){return;}
            clicked.makeMethod(name);
        }
        if(e.getActionCommand().equals("Delete Method")){
            String name = JOptionPane.showInputDialog("What is the name of the method you want to delete");
            clicked.deleteMethod(name);
        }
        if(e.getActionCommand().equals("Add Variable")){
            String methodName = JOptionPane.showInputDialog("What is the name of the method you want to add this variable to");
            String varName = JOptionPane.showInputDialog("What is the name of the variable you want to add");
            String className = JOptionPane.showInputDialog("What is the type of the variable (this should be a class name)");
            clicked.makeMethodVar(methodName, varName, Blackboard.getInstance().getAClass(className), 0);
        }
        if(e.getActionCommand().equals("Delete Variable")){
            String methodName = JOptionPane.showInputDialog("What is the name of the method you want to delete this variable from");
            String varName = JOptionPane.showInputDialog("What is the name of the variable you want to delete");
            clicked.deleteMethodVar(methodName, varName);
        }
        if(e.getActionCommand().equals("Add Global")){
            String varName = JOptionPane.showInputDialog("What is the name of the variable you want to add");
            String className = JOptionPane.showInputDialog("What is the type of the variable (this should be a class name)");
            clicked.addGlobal(varName, Blackboard.getInstance().getAClass(className), 4);
        }
        if(e.getActionCommand().equals("Delete Global")){
            String varName = JOptionPane.showInputDialog("What is the name of the variable you want to delete");
            clicked.deleteGlobal(varName);
        }
        if(e.getActionCommand().equals("Add Parameter")){
            String methodName = JOptionPane.showInputDialog("What is the name of the method you want to add this parameter to");
            String varName = JOptionPane.showInputDialog("What is the name of the variable you want to add");
            String className = JOptionPane.showInputDialog("What is the type of the variable (this should be a class name)");
            clicked.makeMethodVar(methodName, varName, Blackboard.getInstance().getAClass(className), 3);
        }
        if(e.getActionCommand().equals("Delete Parameter")){
            String methodName = JOptionPane.showInputDialog("What is the name of the method you want to delete this parameter from");
            String varName = JOptionPane.showInputDialog("What is the name of the variable you want to delete");
            clicked.deleteMethodVar(methodName, varName);
        }
        Blackboard.getInstance().getData();
        this.dispose();
    }

    /**
     * Deletes a class
     */
    public void deleteClass(){
        Collection<UserClass> classes = Blackboard.getInstance().getClasses().values();
        // yes = 0, no = 2, cancel = 3
        int confirmation = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this class?");
        if (confirmation == 0) {
            classes.remove(clicked);
            for (UserClass other: classes) {
                other.deleteConnection(clicked);
            }
            Blackboard.getInstance().getData("Class deleted: " + clicked.getName());
        }
    }
}
