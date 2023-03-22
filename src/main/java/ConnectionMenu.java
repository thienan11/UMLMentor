import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * CSC 309 UML Tutor
 * Class for UI of menu for choosing connections
 *
 * @author Thien An Tran
 * @author Chloe Anbarcioglu
 * @version 1.1
 */
public class ConnectionMenu extends JPanel implements ActionListener{
    private JPanel panel = new JPanel();
    private JRadioButton association = new JRadioButton("Association");
    private JRadioButton composition = new JRadioButton("Composition");
    private JRadioButton generalization = new JRadioButton("Generalization");
    private JRadioButton dependency = new JRadioButton("Dependency");
    private JRadioButton aggregation = new JRadioButton("Aggregation");
    private JRadioButton implementation = new JRadioButton("Implementation");
    private ButtonGroup connections = new ButtonGroup();
    private Point type; // 0 = association, 2 = composition, 5 = generalization

    /**
     * Creates a JPanel for the connection menu
     */
    public ConnectionMenu() {

        association.addActionListener(this);
        connections.add(association);

        dependency.addActionListener(this);
        connections.add(dependency);

        composition.addActionListener(this);
        connections.add(composition);

        aggregation.addActionListener(this);
        connections.add(aggregation);

        generalization.addActionListener(this);
        connections.add(generalization);

        implementation.addActionListener(this);
        connections.add(implementation);
        
        GridLayout grid = new GridLayout(3, 1);
        panel.setLayout(grid);
        panel.add(association);
        panel.add(composition);
        panel.add(generalization);
        panel.add(dependency);
        panel.add(aggregation);
        panel.add(implementation);
        add(panel);
    }

    // 0 = solid line
    // 0 = arrow head, 1 = triangle, 2 filled diamond

    /**
     * Controls the behaviour when different connection buttons are pressed
     * @param e An action event
     */
    public void actionPerformed(ActionEvent e){
        if(association.isSelected()){
            type = new Point(0, 0);
        }

        if (composition.isSelected()){
            type = new Point(0, 2);
        }

        if (generalization.isSelected()){
            type = new Point(0, 1);
        }

        if(dependency.isSelected()){
            type = new Point(1, 3);
        }

        if(aggregation.isSelected()){
            type = new Point(0, 4);
        }

        if(implementation.isSelected()){
            type = new Point(1, 5);
        }
    }

    /**
     * Gets the type of connection chosen from menu
     *
     * @return A point: 0,0 for association, 0,2 for composition, 0,1 for generalization
     */
    public Point getConnectionType() {
        return type;
    }
}