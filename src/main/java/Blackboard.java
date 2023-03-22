import java.util.Observable;
import java.util.HashMap;

/**
 * CSC 309 UML Tutor
 * Blackboard class for primary information storage
 *
 * @author Mario Vuletic
 * @author Alexander Arrieta
 * @author Chloe Anbarcioglu
 * @author Thien An Tran
 * @version 1.1
 */
public class Blackboard extends Observable{
    private static Blackboard blackboard;
    private HashMap<String, UserClass> classes;
    private HashMap<String, UserClass> problemSolution;
    private int problemId;
    private int problemType = 1;
    private int problemDifficulty;
    private String problemDescription;
    private Metrics metrics;
    private int feedbackTypeWanted;
    private String userID;
    private boolean isProfessor = false;
    private final HashMap<Integer, String> descriptions;

    /**
     * Creates a hashmap of classes
     **/
    private Blackboard(){
        classes = new HashMap<>();
        problemSolution = new HashMap<>();
        descriptions = new HashMap<>();
        descriptions.put(1, "Create a UML diagram that represents the given snippet of code. Start by identifying the classes, and then observe the data in each class to determine the connections between classes.");
        descriptions.put(2, "Create a snippet of code that represents the given UML diagram. Start by identifying the classes, and then observe the connections to determine what should go inside each class.");
        descriptions.put(3, "UML to Metrics Default description");
    }

    /**
     * Returns singleton instance of the blackboard
     * @return The active instance of Blackboard
     */
    public static Blackboard getInstance(){
        if (blackboard == null){
            blackboard = new Blackboard();
        }
        return blackboard;
    }

    /**
     * Sets the isProfessor variable to a boolean value
     * @param b A boolean value to set the isProfessor variable
     */
    public void setIsProfessor(boolean b){
        this.isProfessor = b;
    }

    /**
     * Returns the boolean value of isProfessor
     * @return The boolean value of isProfessor
     */
    public boolean isProfessor(){
        return this.isProfessor;
    }

    /**
     * Sets the type of problem the student is working on
     * @param type An int that represents the type of the problem
     */
    public void setProblemType(int type){
        this.problemType = type;
        this.problemDescription = descriptions.get(type);
        setChanged();
        notifyObservers("Type changed");
    }

    /**
     * Sets the number for the estimated problem difficulty
     * @param difficulty An int that represents the estimated difficult (1-10)
     */
    public void setProblemDifficulty(int difficulty){
        this.problemDifficulty = difficulty;
    }

    /**
     * Gets the int that represents the problem type
     * @return An int that represents the problem type
     */
    public int getProblemType(){
        return this.problemType;
    }

    /**
     * Sets the id of the current problem (Should match database)
     * @param id An int that is the problem id
     */
    public void setProblemId(int id){
        this.problemId = id;
    }

    /**
     * Gets the id of the current problem (Should match database)
     * @return The problem id
     */
    public int getProblemId(){return problemId;}

    /**
     * Gets new data that was updated in blackboard and notifies observers
     */
    public void getData(){
        setChanged();
        notifyObservers();
    }

    /**
     * Sets the solution for the current problem
     * @param solution A solution set containing the UserClass objects
     */
    public void setProblemSolution(HashMap<String, UserClass> solution){
        this.problemSolution = solution;
        if (isProfessor()){
            this.classes = solution;
        }
        getData("Solution loaded");
    }

    /**
     * Sets the problem description of the problem
     * @param s A string that is the problem description
     */
    public void setProblemDescription(String s){
        this.problemDescription = s;
        getData("Description changed");
    }

    /**
     * Gets the problem description based on the problem type
     * @param problemType An int that represents the problem type
     * @return The problem description based on the problem type
     */
    public String getProblemDescriptionForSubmit(int problemType){
        return this.descriptions.get(problemType);
    }

    /**
     * Gets the problem description of the problem
     * @return The problem description of the problem
     */
    public String getProblemDescription(){
        return this.problemDescription;
    }

    /**
     * Gets the difficulty of the current problem
     * @return The int that represents the problem's difficulty
     */
    public int getProblemDifficulty(){
        return this.problemDifficulty;
    }

    /**
     * Gets new data that was updated in blackboard and notifies observers
     * @param s A string to signify what changes were made to the blackboard
     */
    public void getData(String s){
        setChanged();
        notifyObservers(s);
    }

    /**
     * Sets the current classes the student has made
     * @param classes A hashmap of the current classes the student has made
     */
    public void setClasses(HashMap<String, UserClass> classes){
        this.classes = classes;
        getData("classes changed");
    }

    /**
     * Clears all the data of the current problem
     */
    public void clearData(){
        classes = new HashMap<>();
        problemSolution = new HashMap<>();
        problemDescription = "";
    }

    /**
     * Adds a class to the stored ClassInformation objects if the name does not exist
     * @param name A string of the name of the new class
     * @param classBox A ClassInformation object to be added
     * @return Returns true if there is no class existing with such a name and the new class was successfully added.
     * Returns false if a class with the given name already exists.
     */
    public boolean addClass(String name, UserClass classBox){
        Object o = classes.putIfAbsent(name, classBox);
        notifyObservers("Added class: " + name);
        if(o == null){
            return true;
        }
        return false;
    }

    /**
     * Returns the current ClassInformation objects
     * @return All the classes currently in the blackboard
     */
    public HashMap<String, UserClass> getClasses(){
        return classes;
    }

    /**
     * Returns the instance of a class with the given name from the student solution
     * @param name A string of the name of the desired class
     * @return A UserClass object of the same name, or null if no such class exists
     */
    public UserClass getAClass(String name){return classes.get(name);}

    /**
     * Clears all the class data
     */
    public void clearClasses(){
        classes = new HashMap<>();
        getData("Classes cleared");
    }

    /**
     * Gets the problem solution class set
     * @return The hashmap containing the UserClasses of the solution
     */
    public HashMap<String, UserClass> getProblemSolution() {
        return problemSolution;
    }

    /**
     * Gets the metrics of the current problem
     * @return The metrics of the current problem
     */
    public Metrics getMetrics() {
        return metrics;
    }

    /**
     * Sets the metrics of the current problem
     * @param metrics Metrics of the current problem
     */
    public void setMetrics(Metrics metrics) {
        this.metrics = metrics;
    }

    /**
     * Sets what type of feedback is wanted for the current problem
     * @param feedbackTypeWanted An int that represents the type of feedback wanted
     */
    public void setFeedbackTypeWanted(int feedbackTypeWanted) {
        this.feedbackTypeWanted = feedbackTypeWanted;
    }

    /**
     * Gets the type of feedback wanted for the current problem
     * @return The int that represents the type of feedback wanted
     */
    public int getFeedbackTypeWanted() {
        return feedbackTypeWanted;
    }
        
    /**
     * Sets the id of the current user (Should match database)
     * @param id A string that represents the id of the current user
     */
    public void setUserID(String id){
        this.userID = id;
    }

    /**
     * Gets the id of the current user (Should match database)
     */
    public String getUserID(){
        return userID;
    }
}