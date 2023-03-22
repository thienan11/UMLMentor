import javax.swing.JPanel;
import java.util.Observable;
import java.util.Observer;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.*;
import java.awt.*;

/**
 * CSC 309 UML Tutor
 * Class for the code viewer and editor
 * 
 * @author Mario Vuletic
 * @version 1.1
 */
public class TextPanel extends JPanel implements Observer {
    private Parser parser;
    private JTextArea textArea;

    /**
     * Creates a JTextArea where code can be written
     */
    public TextPanel(){
        parser = new Parser();
        setLayout(new BorderLayout());

        textArea = new JTextArea();
        textArea.setFont(new Font("Courier New", Font.PLAIN, 16));

        JScrollPane scrollpane = new JScrollPane(textArea);
        scrollpane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        add(scrollpane, BorderLayout.CENTER);   
        textArea.setTabSize(2);
    }

    /**
     * Looks at what action needs to be taken
     * @param o the observable object.
     * @param arg an argument passed to the {@code notifyObservers} method.
     */
    @Override
    public void update(Observable o, Object arg) {
        String s = textArea.getText();
        if (arg == null) { recode();return; }
        switch ((String) arg){
            case "Parsing Needed":
                if (textArea.isEditable()){ parseInput(textArea.getText()); }
                break;
            case "Type changed":
                break;
            default:
                recode();
                break;
        }    
    } 

    /**
     * Gets newly parsed data and gives this data to blackboard
     * @param text A string that will be parsed
     */
    public void parseInput(String text){
        HashMap<String, UserClass> data = parser.parse(text);
        if (data != null){
            Blackboard.getInstance().setClasses(data);
            Blackboard.getInstance().getData("Successful Build");
        }
        else{
            Blackboard.getInstance().getData("Build Error");
        } 
    }

    /**
     * Enables/Disables typing on text panel depending on if it contains given information or not
     * @param editable : true if user is supposed to solve with textPanel, false otherwise.
     */
    public void setEditable(boolean editable){
        this.textArea.setEditable(editable);
    }

     /**
      * Writes the classes, inheritances, methods, variables in the text panel based on blackboard information
      * @return The string this constructs based on the class data
      */
    public String recode(){
        StringBuilder newText = new StringBuilder();
        HashMap<String, UserClass> data = this.textArea.isEditable() ? Blackboard.getInstance().getClasses() : Blackboard.getInstance().getProblemSolution();
        for (String curClass : data.keySet()){
            newText.append("class " + curClass);
            UserClass parent = data.get(curClass).getParent();
            if (parent != null){
                newText = data.get(curClass).getIsImplementation() ? newText.append(" implements " + parent.getName()) : newText.append(" extends " + parent.getName());
            }
            newText.append(" {\n");
            for (VariableInformation global : data.get(curClass).getGlobals()){
                newText.append("  " + global.getConnectedTo().getName() +" "+ global.getName() + ";\n");
            }
            for (String method : data.get(curClass).getMethods()){
                newText.append("  void " + method);
                String[] params = new String[data.get(curClass).getMethodParams(method).size()];
                for (int i = 0; i < params.length; i++){
                    VariableInformation param = (data.get(curClass).getMethodParams(method).get(i));
                    params[i] = param.getConnectedTo().getName() + " " + param.getName();
                }
                newText.append("(" + String.join(", ", params) + "){\n");
                ArrayList<VariableInformation> variables = data.get(curClass).getMethodVars(method);
                for (VariableInformation var : variables){
                    newText.append("    " + var.getConnectedTo().getName() + " " + var.getName() + ";");
                }
                newText.append("\n  }");
            }
            newText.append("\n}\n");
        }
        this.textArea.setText(newText.toString());
        return newText.toString();
    }

    /**
     * Clears the text panel
     */
    public void clearText(){
        textArea.setText("");
        revalidate();
    }

    /**
     * Gets the text currently in the text panel
     * @return The text in the text panel
     */
    public String getTextAreaString(){
        return textArea.getText();
    }

    /**
     * Loads the problem description into a text panel
     * @param problem The problem description string to load
     */
    public void loadProblem(String problem){
        this.textArea.setText(problem);
        revalidate();
    }
}
