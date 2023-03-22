import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Collection;
import java.util.HashMap;

/**
 * CSC 309 UML Tutor
 * ClassClickAdder handles all the controls for the main view screen
 *
 * @author Alex Arrieta
 * @author Chloe Anbarcioglu
 * @version 1.1
 */
public class PanelClickHandler implements MouseListener, MouseMotionListener { //Rename to avoid class as first word
    private Blackboard blackboard;
    private UserClass classClicked;
    private UserClass inClass;
    private UserClass outClass;
    private int status = 0;
    private boolean canEdit = true;

    /**
     * PanelClickHandler constructor
     */
    public PanelClickHandler(){
        blackboard = Blackboard.getInstance();
        classClicked = null;
    }

    /**
     * Invoked when the mouse button has been clicked (pressed and released) on a component
     * Sets classes for connection
     * Handles deleting classes and connections
     * @param e : MouseEvent
     */
    public void mouseClicked(MouseEvent e) {
        HashMap mapClasses = blackboard.getClasses();
        Collection<UserClass> classes = mapClasses.values();
        for(UserClass c : classes){
            int height = (50 + (30 * (c.getGlobals().size() + c.getMethods().size())));
            if(e.getY() > c.getPoint().getY() && e.getY() < c.getPoint().getY() + height){
                if(e.getX() > c.getPoint().getX() && e.getX() < c.getPoint().getX() + c.getWidth()){
                    if (SwingUtilities.isRightMouseButton(e)) {
                        new ClassClickHandler(c);
                        status=0;
                        return;
                    }
                    if (status == 0) {
                        inClass = c;
                        status = 1;
                        blackboard.getData("Class " + c.getName() + " clicked");
                    } else if (status == 1) {
                        outClass = c;
                        status = 0;
                        makeConnection();
                    }
                    return;
                }
            }
        }
    }

    /**
     * Updates the UserClasses when a connection is added via the drawpanel
     */
    public void makeConnection(){
        ConnectionMenu connectionMenu = new ConnectionMenu();
        JOptionPane.showMessageDialog(null, connectionMenu, "Choose Connection:", JOptionPane.INFORMATION_MESSAGE);
        Point type = connectionMenu.getConnectionType();
        if(type == null){
            return;
        }
        if(type.equals(new Point(0, 2)) || type.equals(new Point(0, 4))){
            inClass.addGlobal("var", outClass, type.y);
        }
        else if(type.equals(new Point(0, 1))){
            inClass.setParent(outClass);
            inClass.setIsImplementation(false);
        }
        else if(type.equals(new Point(1, 5))){
            inClass.setParent(outClass);
            inClass.setIsImplementation(true);
        }
        else{
            inClass.makeMethod("method");
            inClass.makeMethodVar("method", "temp", outClass, type.y);
        }
        blackboard.getData("Connection added");
    }

    public void setCanEdit(boolean b){
        this.canEdit = b;
    }

    public boolean canEdit(){
        return this.canEdit;
    }

    /**
     * Invoked when a mouse button has been pressed on a component
     * Creates a new class and asks for name input
     * Sets which class was clicked if existing class was pressed
     * @param e The MouseEvent triggered
     */
    public void mousePressed(MouseEvent e) {
        if (!canEdit) { return; }
        HashMap mapClasses = blackboard.getClasses();
        Collection<UserClass> classes = mapClasses.values();
        for(UserClass c : classes){
            int height = (50 + (30 * (c.getGlobals().size() + c.getMethods().size())));
            if(e.getY() > c.getPoint().getY() && e.getY() < c.getPoint().getY() + height){
                if(e.getX() > c.getPoint().getX() && e.getX() < c.getPoint().getX() + c.getWidth()){
                    classClicked = c;
                    return;
                }
            }
        }
        if(SwingUtilities.isRightMouseButton(e)){
            return;
        }
        UserClass c = new UserClass(e.getX(), e.getY());
        String name = JOptionPane.showInputDialog("Please input name");
        if(name == null){
            return;
        }
        if(!blackboard.addClass(name, c)){
            JOptionPane.showMessageDialog(null, "Class with that name already exists, you " +
                    "cannot add another class of with the same name");
            return;
        }
        c.setName(name);
        blackboard.getData("Added class" + name);
    }

    /**
     * Invoked when a mouse button has been released on a component
     * Sets classClicked to null
     * @param e The MouseEvent triggered
     */
    public void mouseReleased(MouseEvent e) {
        if (!canEdit) { return; }
        classClicked = null;
        blackboard.getData();
    }

    /**
     * Invoked when the mouse enters a component
     * @param e The MouseEvent triggered
     */
    public void mouseEntered(MouseEvent e) {

    }

    /**
     * Invoked when the mouse exits a component
     * @param e The MouseEvent triggered
     */
    public void mouseExited(MouseEvent e) {

    }

    /**
     * Invoked when a mouse button is pressed on a component and then dragged
     * Sets the classClicked point
     * @param e The MouseEvent triggered
     */
    public void mouseDragged(MouseEvent e) {
        if (!canEdit) { return; }
        if(classClicked != null){
            classClicked.setPoint(e.getX(), e.getY());
        }
        blackboard.getData();
    }

    /**
     * Invoked when the mouse cursor has been moved onto a component but no buttons have been pushed
     * @param e The MouseEvent triggered
     */
    public void mouseMoved(MouseEvent e) {

    }

}