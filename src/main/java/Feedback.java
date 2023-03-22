import javax.swing.*;
import java.util.ArrayList;

/**
 * CSC 309 UML Tutor
 * A class designed to give feedback to the student
 * 
 * @author Alex Arrieta
 * @version 0.9
 */
public class Feedback implements Runnable{
    private static Feedback feedback;
    private boolean giveFeedback;
    private long elapsedTime;
    private long prevTime;
    private boolean run;

    /**
     * Feedback constructor
     */
    private Feedback(){}

    /**
     * Looks for the information needed to produce feedback
     * @param type The type of problem or the desired type of correctness?
     */
    public void getFeedback(int type){
        ProblemSolutionDiff p = new ProblemSolutionDiff();
        ArrayList<ArrayList<Object>> diff = p.diff(0);
        interpretResult(diff);
    }

    /**
     * Returns singleton instance of the feedback
     * @return The active instance of feedback
     */
    public static Feedback getInstance(){
        if(feedback == null){
            feedback = new Feedback();
        }
        return feedback;
    }

    /**
     * Uses user information and the state of the problem to generate feedback
     * @param diff The differences between the student solution and the correct solution
     */
    public void interpretResult(ArrayList<ArrayList<Object>> diff){
        String feedback = "";
        boolean classNum = false;
        if(diff.get(0).isEmpty()){
            JOptionPane.showMessageDialog(null, "You've solved the problem!");
            return;
        }
        if(diff.get(0).contains("Extra")){
            feedback = feedback + "You seem to have extra classes\n";
            classNum = true;
        }
        if(diff.get(0).contains("Absent")) {
            feedback = feedback + "You seem to missing some classes\n";
            classNum = true;
        }
        if(!classNum && Blackboard.getInstance().getMetrics().getAttemptsCount() < 5 && diff.get(0).size() > 4){
            feedback = getRatioFeedback(diff);
        }
        else if(!classNum){
            feedback = getConnectionsFeedback(diff);
        }
        JOptionPane.showMessageDialog(null, feedback);
    }

    /**
     * Looks for high ratios of certain errors to find certain ways to guide the student
     * @param diff Differences between the student solution and the correct solution
     * @return The string containing feedback for the student to be displayed
     */
    private String getRatioFeedback(ArrayList<ArrayList<Object>> diff) {
        String feedback = "";
        if(findNumThatContain("Missing", diff.get(0))/diff.get(0).size() > .5){
            feedback = "You seem to be missing a lot of connections\nRemember all variables will contribute a connection";
        }
        if(findNumThatContain("Unneeded", diff.get(0))/diff.get(0).size() > .5){
            feedback = "You seem to have a lot of extra connections\nRemember all variables will contribute only a single connection";
        }
        return feedback;
    }

    /**
     * Finds the number of problems that contain the string S
     * @param s A string to look for in problems
     * @param probs An arraylist of strings representing problems
     * @return The number of problems containing S
     */
    public double findNumThatContain(String s, ArrayList<Object> probs){
        double num = 0;
        for(int i = 0; i<probs.size(); i++){
            if(probs.get(i).toString().contains(s)){
                num++;
            }
        }
        return num;
    }

    /**
     * Gets feedback for specific connections between classes
     * @param diff Differences between the student solution and the correct solution
     * @return The string containing feedback for the student to be displayed
     */
    public String getConnectionsFeedback(ArrayList<ArrayList<Object>> diff){
        ArrayList<String> problemStrings = (ArrayList<String>) diff.get(0).clone();
        ArrayList<UserClass> problemClasses = (ArrayList<UserClass>) diff.get(1).clone();
        String[] feedback = new String[]{""};
        boolean MissingConnection = false;
        boolean ExtraConnection = false;
        for(int i = 0; i < problemStrings.size(); i++){
            if((problemStrings.get(i)).contains("Missing Connection")) {
                MissingConnection = missingConnectionsFeedback(problemStrings.get(i), problemClasses.get(i), MissingConnection, feedback);
            }
            if((problemStrings.get(i)).contains("Unneeded Connection")) {
                ExtraConnection = extraConnectionFeedback(problemStrings.get(i), problemClasses.get(i), ExtraConnection, feedback);
            }
        }
        return feedback[0];
    }

    /**
     * Gets feedback for when there is an extra connection
     * @param s 
     * @param userClass
     * @param ExtraConnection
     * @param feedback
     * @return The boolean value of whether or not there is an extra connection
     */
    private boolean extraConnectionFeedback(String s, UserClass userClass, boolean ExtraConnection, String[] feedback) {
        Metrics metrics = Blackboard.getInstance().getMetrics();
        if (metrics.getAttemptsCount() <= 1 && !ExtraConnection) {
            feedback[0] = feedback[0] + "Look at the connections between classes again. You have at least one extra\n";
            ExtraConnection = true;
        }
        else if(metrics.getAttemptsCount() <= 5 && metrics.getAttemptsCount() >= 2){
            feedback[0] = feedback[0] + "Class " + userClass.getName() + " has an extra connection to a different class, try to figure out which\n";
        }
        else if(metrics.getAttemptsCount() <= 15 && metrics.getAttemptsCount() >= 6 && metrics.getHours() < 1){
            feedback[0] = feedback[0] + "Class " + userClass.getName() + " has an extra connection to a different class, try to figure out which\n";
        }
        else if(metrics.getAttemptsCount() >= 16 || metrics.getHours() >= 1){
            feedback[0] = feedback[0] + "Class " + userClass.getName() +" needs to remove " + connNumToString(Integer.parseInt(s.split(" ")[2])) +
                    " type of connection with " + s.split(" ")[3];
        }
        return ExtraConnection;
    }

    /**
     * Gets feedback for when there is a missing connection
     * @param s
     * @param userClass
     * @param MissingConnection
     * @param feedback
     * @return The boolean value of whether or not there is a missing connection
     */
    private boolean missingConnectionsFeedback(String s, UserClass userClass, boolean MissingConnection, String[] feedback) {
        Metrics metrics = Blackboard.getInstance().getMetrics();
        if (metrics.getAttemptsCount() <= 1 && !MissingConnection) {
            feedback[0] = feedback[0] + "Look at the connections between classes again. You are missing at least one\n";
            MissingConnection = true;
        }
        else if(metrics.getAttemptsCount() <= 5 && metrics.getAttemptsCount() >= 2){
            feedback[0] = feedback[0] + "Class " + userClass.getName() + " has a missing connection to a different class, try to figure out which\n";
        }
        else if(metrics.getAttemptsCount() <= 15 && metrics.getAttemptsCount() >= 6 && metrics.getHours() < 1){
            feedback[0] = feedback[0] + "Class " + userClass.getName() + " has a missing connection to a different class, try to figure out which\n";
        }
        else if(metrics.getAttemptsCount() >= 16 || metrics.getHours() >= 1){
            feedback[0] = feedback[0] + "Class " + userClass.getName() +" needs a(n) " + connNumToString(Integer.parseInt(s.split(" ")[2])) +
                    " type of connection with " + s.split(" ")[3] + "\n";
        }
        return MissingConnection;
    }

    /**
     * Makes a connection number into a string
     * @param conn A int that represents the connection number
     * @return The string of the connection
     */
    public String connNumToString(int conn){
        switch (conn){
            case 0:
                return "association";
            case 1:
                return "generalization";
            case 2:
                return "composition";
            case 3:
                return "dependency";
            case 4:
                return "aggregation";
            case 5:
                return "implementation";
            default:
                return "unknown";
        }
    }

    /**
     * Runs every 10 milliseconds seeing if the student needs feedback
     */
    @Override
    public void run() {
        giveFeedback = false;
        elapsedTime = 0;
        prevTime = System.currentTimeMillis();
        run = true;
        while (true){
            if(giveFeedback || elapsedTime/1000 > 600 && JOptionPane.showConfirmDialog(null, "Do you need some help?") == 0){
                elapsedTime = 0;
                giveFeedback = false;
                getFeedback(Blackboard.getInstance().getFeedbackTypeWanted());
            }
            else if(elapsedTime/1000 > 600) {
                elapsedTime = 0;
            }
            elapsedTime += System.currentTimeMillis() - prevTime;
            prevTime = System.currentTimeMillis();
            if(!run){
                return;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Sets to true if the student wants feedback, false if not
     * @param b The boolean value to set feedback
     */
    public void setFeedback(boolean b){
        giveFeedback = b;
    }

    /**
     * Stops feedback from running in the background
     * @param b The boolean value to set run
     */
    public void setRun(boolean b){
        run = b;
    }
}
