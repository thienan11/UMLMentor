import java.awt.*;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;

/**
 * CSC 309 UML Tutor
 * Defines the information in a class
 *
 * @author Alex Arrieta
 * @author Chloe Anbarcioglu
 * @version 1.1
 */
public class UserClass{
    private String name;
    private Point point;
    private int height, width;
    private HashMap<String, ArrayList<VariableInformation>> methodVariables; //The string key represents the name of the method, the keyset of this hashmap is every method for this class
    private ArrayList<VariableInformation> globalVars;
    private UserClass parent;
    private boolean isImplementation; //Set to default false if there is no parent

    /**
     * Constructor for when all information about a class is known
     * @param name Name of the class
     * @param x X cord of the box
     * @param y Y cord of the box
     * @param height Height of the box
     * @param width Width of the box
     */
    public UserClass(String name, int x, int y, int height, int width){
        if(x < 25){
            x=25;
        }
        if(y < 25){
            y=25;
        }
        this.name = name;
        this.point = new Point(x-50, y-50);
        this.height = height;
        this.width = width;
        methodVariables = new HashMap<>();
        globalVars = new ArrayList<>();
    }

    /**
     * Constructor for a new class
     * @param x X cord of the box
     * @param y Y cord of the box
     */
    public UserClass(int x, int y){
        // get documentLocation
        if(x < 25){
            x=25;
        }
        if(y < 25){
            y=25;
        }
        point = new Point(x - 50, y - 25);
        height = 50;
        width = 100;

        methodVariables = new HashMap<>();
        globalVars = new ArrayList<>();
    }

    /**
     * Constructor for a new class by the parser
     * @param name The parsed name of the class
     */
    public UserClass(String name){
        this.name = name;
        point = new Point(25, 25);
        height = 50;
        width = 100;

        methodVariables = new HashMap<>();
        globalVars = new ArrayList<>();
    }

    /**
     * Set the parent of the user class
     * @param parent The UserClass parent of this UserClass
     */
    public void setParent(UserClass parent) {
        this.parent = parent;
    }

    /**
     * Get the parent of this user class
     * @return The UserClass parent of this UserClass
     */
    public UserClass getParent() {
        return parent;
    }

    /**
     * Set if this implements another class
     * @param isImplementation A boolean value to set isImplementation to
     */
    public void setIsImplementation(boolean isImplementation) {
        this.isImplementation = isImplementation;
    }

    /**
     * Get if this implements another class
     * @return isImplementation The boolean value of isImplementation
     */
    public boolean getIsImplementation() {
        return isImplementation;
    }

    /**
     * Returns the point this class is located at
     * @return Point point
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Returns the current height of the box
     * @return int height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the current height of the box
     * @return int height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * Returns the current width of the box
     * @return int width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Gets a hash map of the methods in a class
     * @return A set of strings that are all the names of the methods of this class
     */
    public Set<String> getMethods(){ return methodVariables.keySet(); }

    /**
     * This deletes the method of a class and ALL VARIABLES WITHIN THE METHOD
     * @param m The method to delete
     */
    public void deleteMethod(String m){
        methodVariables.remove(m);
    }

    /**
     * Deletes a method variable
     * @param methodName The name of the method
     * @param varName The name of the variable
     */
    public void deleteMethodVar(String methodName, String varName){
        if(methodVariables.get(methodName) == null){return;}
        methodVariables.get(methodName).removeIf(v -> v.getName().equals(varName));
    }

    /**
     * This makes a method and automatically adds it to this class
     * If the method already exists this returns the VariableInformation associated with the method
     * @param name The name of the method
     * @return
     */
    public ArrayList<VariableInformation> makeMethod(String name){
        return methodVariables.putIfAbsent(name, new ArrayList<>());
    }

    /**
     * Get the local variables of a method
     * @param name The method to get
     * @return An ArrayList of the VariableInformations in this method
     */
    public ArrayList<VariableInformation> getMethodVars(String name){
        if(methodVariables.get(name) == null){return null;}
        ArrayList<VariableInformation> toReturn = (ArrayList<VariableInformation>) methodVariables.get(name).clone();
        toReturn.removeIf(v -> v.getType() != 0);
        return toReturn;
    }

    /**
     * Get the parameters of a method
     * @param name The method to get
     * @return An ArrayList of the VariableInformations in this method
     */
    public ArrayList<VariableInformation> getMethodParams(String name){
        if(methodVariables.get(name) == null){return null;}
        ArrayList<VariableInformation> toReturn = (ArrayList<VariableInformation>) methodVariables.get(name).clone();
        toReturn.removeIf(v -> v.getType() != 3);
        return toReturn;
    }

    /**
     * Get the all variables of this UserClass
     * @return An ArrayList of the VariableInformations in this UserClass
     */
    public ArrayList<VariableInformation> getAllVars(){
        ArrayList<VariableInformation> list = (ArrayList<VariableInformation>) globalVars.clone();
        for(ArrayList<VariableInformation> v : methodVariables.values()){
            list.addAll(v);
        }
        return list;
    }

    /**
     * Make a new parameter or local variable in a method
     * @param methodName The method to put the variable in
     * @param varName The name of the variable
     * @param connectedTo What class type the variable is of
     * @param type The type of variable this is
     */
    public void makeMethodVar(String methodName, String varName, UserClass connectedTo, int type){
        if(methodVariables.get(methodName) == null || connectedTo == null || varName == null){return;}
        methodVariables.get(methodName).add(new VariableInformation(varName, connectedTo, type, methodName));
    }

    /**
     * Gets a hash map of the globals in a class
     * @return this.variables
     */
    public ArrayList<VariableInformation> getGlobals(){
        return globalVars;
    }

    /**
     * This makes a variable and automatically adds it to this class
     * @param varName
     * @param connectedTo
     * @param type
     */
    public void addGlobal(String varName, UserClass connectedTo, int type){
        if(connectedTo == null || varName == null){return;}
        globalVars.add(new VariableInformation(varName, connectedTo, type, null));
    }

    /**
     * Deletes a global variable of this class
     * @param v The VariableInformation to delete
     */
    public void deleteGlobal(VariableInformation v){
        globalVars.remove(v);
    }

    /**
     * Deletes a global variable of this class based on name
     * @param name The VariableInformation to delete
     */
    public void deleteGlobal(String name){
        globalVars.removeIf(v -> v.getName().equals(name));
    }

    /**
     * All the connections this UserClass has to other UserClasses
     * @return A hashmap where the key is a UserClass and value is an ArrayList of the connections it has to that class
     */
    public HashMap<UserClass, ArrayList<Point>> getConnections(){
        HashMap<UserClass, ArrayList<Point>> map = new HashMap();
        for(ArrayList<VariableInformation> list : methodVariables.values()){
            for(VariableInformation v : list){
                ArrayList curr = new ArrayList();
                curr.add(new Point(v.getType()/3, v.getType()));
                ArrayList prev = map.putIfAbsent(v.getConnectedTo(), curr);
                if(prev != null){
                    prev.add(new Point(v.getType()/3, v.getType()));
                }
            }
        }
        for(VariableInformation v : globalVars){
            ArrayList curr = new ArrayList();
            curr.add(new Point(0, v.getType()));
            ArrayList prev = map.putIfAbsent(v.getConnectedTo(), curr);
            if(prev != null){
                prev.add(new Point(0, v.getType()));
            }
        }
        if(parent != null){
            ArrayList curr = new ArrayList();
            if(isImplementation) {
                curr.add(new Point(1, 5));
                ArrayList prev = map.putIfAbsent(parent, curr);
                if(prev != null){
                    prev.add(new Point(1, 5));
                }
            }else{
                curr.add(new Point(0, 1));
                ArrayList prev = map.putIfAbsent(parent, curr);
                if(prev != null){
                    prev.add(new Point(0, 1));
                }
            }
        }
        return map;
    }

    /**
     * Deletes all connections this class has with another one
     * @param c The class to remove connections to
     */
    public void deleteConnection(UserClass c){
        globalVars.removeIf(v -> v.getConnectedTo().equals(c));
        for(ArrayList<VariableInformation> list : methodVariables.values()){
            list.removeIf(v -> v.getConnectedTo().equals(c));
        }
        if(c.equals(parent)){
            parent = null;
        }
    }

    /**
     * Sets the point this box is located at
     * @param x X cord as int
     * @param y Y cord as int
     */
    public void setPoint(int x, int y) {
        this.point = new Point(x - 50, y - 25);
    }

    /**
     * Turns class data into a string
     * @return name, point, height, and width comma deliniated
     */
    @Override
    public String toString() {
        return "ClassInformation{" +
                "name='" + name + '\'' +
                ", point=" + point +
                ", height=" + height +
                ", width=" + width +
                '}';
    }

    /**
     * Returns the name of the UserClass object
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the UserClass object
     * @param name : String
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserClass userClass = (UserClass) o;
        return Objects.equals(name, userClass.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}