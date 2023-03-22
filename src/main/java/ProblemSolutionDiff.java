import java.awt.*;
import java.util.*;

/**
 * CSC 309 UML Tutor
 * A class designed to find the difference between two class sets
 * 
 * @author Alex Arrieta
 * @version 1.0
 */
public class ProblemSolutionDiff {
    /**
     * Finds the differences between the student solution and the correct solution
     * @param specificity This labels how specific the differences found should be.
     *                    A 0 looks for isomorphic graph structures in the UserClass structures including the connection types being the same.
     *                    A 1 looks for matching class names and that classes connect to the correctly named classes with the correct connection
     *                    A 2 acts like 1 except it also looks for variable and method amounts for a class matching
     *                    A 3 looks for correctly names classes and variables and methods and that variables are in the correct method.
     * @return An ArrayList(ArrayList<String>, ArrayList<UserClass>). The string represents the problem found with the user class.
     * These are index matched. IE Index 0 of the string array list is the problem corresponding to Index 0 of the userclass arraylist.
     * A null for the value indicates that this problem is not tied to a specific UserClass
     * The possible problems are
     * "Absent" (The student solution does not have this user class),
     * "Extra" (The student solution has this UserClass but the solution doesn't)
     * "Missing Item (method, variable) (name)" (The UserClass is missing a variable or method)
     * "Missing Connection (connection type) (ToClass)" (This UserClass needs a connection to ToClass)
     * "Unneeded Item (method, variable) (name)" (The UserClass does not need this variable or method)
     * "Unneeded Connection (connection type) (ToClass)" (This UserClass does not need a connection to ToClass)
     * "Modify Item (method, variable) (name)" (This method or variable needs to somehow be changed or moved but should exist)
     * "Modify Connection (connection type) (ToClass)" (This connection points to the correct class but is of the wrong type)
     */
    public ArrayList<ArrayList<Object>> diff(int specificity){
        switch (specificity){
            case 0:
                return diffZeroSpecificity();
            case 1:
                return diffOneSpecificity();
            case 2:
                return diffTwoSpecificty();
            case 3:
                return diffThreeSpecificty();
            default:
                return new ArrayList<>();
        }
    }

    /**
     * This will first look for absent or extra classes, if the class counts do not line up then
     * connections will not be looked for
     * @return "Absent", "Extra", "Missing Connection", "Unneeded Connection"
     */
    public ArrayList<ArrayList<Object>> diffZeroSpecificity(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap<String, UserClass> student = blackboard.getClasses();
        HashMap<String, UserClass> solution = blackboard.getProblemSolution();
        ArrayList<ArrayList<Object>> problems = new ArrayList<>();
        problems.add(new ArrayList<>());
        problems.add(new ArrayList<>());

        int diff = student.values().size() - solution.values().size();
        for(int i = 0; i < Math.abs(diff); i++){
            if (diff < 0) {
                problems.get(0).add("Absent");
                problems.get(1).add(null);
            } else {
                problems.get(0).add("Extra");
                problems.get(1).add(null);
            }
        }
        if(diff != 0){
            return problems;
        }

        String[][] solutionArr = setUpSolutionArr(solution);

        Object[] studentClasses = student.values().toArray();
        ArrayList<ArrayList<Integer>> indices;
        ArrayList<Integer> initial = new ArrayList<Integer>(studentClasses.length);
        for(int i = 0; i < studentClasses.length; i++){
            initial.add(i);
        }
        indices = findPermutation(initial, studentClasses.length);

        return findGraphProblems(indices, solutionArr, studentClasses);

    }

    /**
     * Sets up the solution set of classes as a 2d array of strings. The string represents the connection between the
     * ith class and jth class. Each digit in the string is one connection type. An empty string represents no connections
     * @param solution The hashmap containing all the UserClasses in the solution
     * @return The 2d array created
     */
    public String[][] setUpSolutionArr(HashMap<String, UserClass> solution) {
        String[][] solutionArr = new String[solution.values().size()][solution.values().size()];
        for(int i = 0; i < solution.values().size(); i++){
            for(int j = 0; j < solution.values().size(); j++){
                solutionArr[i][j] = "";
            }
        }
        int i = 0;
        int j = 0;
        for(UserClass a : solution.values()){
            HashMap<UserClass, ArrayList<Point>> connection = a.getConnections();
            for(UserClass b : solution.values()) {
                if(connection.get(b) == null){
                    solutionArr[i][j] = "";
                    j++;
                    continue;
                }
                for(Point conn : connection.get(b)) {
                    solutionArr[i][j] = solutionArr[i][j] + (int) conn.getY();
                }
                j++;
            }
            i++;
            j = 0;
        }
        return solutionArr;
    }

    /**
     * Finds differences between two weighted graphs represented as 2d string arrays.
     * It reports the isomorphisms of the graphs that have the minimum differences
     * @param indices An arraylist of lists of ints that represent which index to assign each UserClass from the student
     *                solution to look at every possible permutation (IE every isomorphism)
     * @param solutionArr The correct solution classes represented in a 2d string array
     * @param studentClasses An array containing every student UserClass
     * @return An arraylist of problems matching the diff() documentation
     */
    public ArrayList<ArrayList<Object>> findGraphProblems(ArrayList<ArrayList<Integer>> indices, String[][] solutionArr, Object[] studentClasses) {
        ArrayList<ArrayList<Object>> possibleProblems = new ArrayList<>();
        possibleProblems.add(new ArrayList<>());
        possibleProblems.add(new ArrayList<>());
        ArrayList<ArrayList<Object>> problems = new ArrayList<>();
        problems.add(new ArrayList<>());
        problems.add(new ArrayList<>());
        for(int k = 0; k < indices.size(); k++) {
            String [][] solutionArrCopy = new String[solutionArr.length][solutionArr.length];
            for(int a = 0; a < solutionArrCopy.length; a++){
                solutionArrCopy[a] = Arrays.copyOf(solutionArr[a], solutionArr.length);
            }
            for (int i = 0; i < studentClasses.length; i++) {
                for(int j = 0; j < studentClasses.length; j++){
                    UserClass a = (UserClass) studentClasses[(indices.get(k).get(i))]; //The K acts as an offset for the array indexing, but this is still the (i,j) item
                    UserClass b = (UserClass) studentClasses[(indices.get(k).get(j))];
                    if(a.getConnections().get(b) != null) {
                        for (Point conn : a.getConnections().get(b)) {
                            String connStr = "";
                            if (conn != null) {
                                connStr = String.valueOf((int) conn.getY());
                            }
                            for(char c : connStr.toCharArray()) {
                                if (!solutionArrCopy[i][j].contains(String.valueOf(c))) {
                                    possibleProblems.get(0).add("Unneeded Connection " + connStr + " " + b.getName());
                                    possibleProblems.get(1).add(a);
                                } else {
                                    solutionArrCopy[i][j] = solutionArrCopy[i][j].replaceFirst(String.valueOf(c), "");
                                }
                            }
                        }
                    }
                    for (char c : solutionArrCopy[i][j].toCharArray()) {
                        possibleProblems.get(0).add("Missing Connection " + c + " " + b.getName());
                        possibleProblems.get(1).add(a);
                    }
                }
            }
            if(possibleProblems.get(0).size() < problems.get(0).size() || k==0){
                problems = possibleProblems;
            }
            possibleProblems = new ArrayList<>();
            possibleProblems.add(new ArrayList<>());
            possibleProblems.add(new ArrayList<>());
        }
        return problems;
    }

    /**
     * Finds permutations using heap algorithm
     * @param array An array of a given permutation
     * @param size The size of the problem you want to solve (initially the size of the array)
     * @return An arraylist containing lists of every possible permutation of the given original array
     */
    public ArrayList<ArrayList<Integer>> findPermutation(ArrayList<Integer> array, int size) {
        ArrayList<ArrayList<Integer>> toReturn = new ArrayList<>();
        if(size == 0){
            return toReturn;
        }
        if(size == 1) {
            toReturn.add((ArrayList<Integer>) array.clone());
            return toReturn;
        } else {
            for(int i = 0; i < size-1; i++) {
                toReturn.addAll(findPermutation(array, size-1));
                if(size % 2 == 0) {
                    int temp = array.get(i);
                    array.set(i, array.get(size-1));
                    array.set(size-1, temp);
                } else {
                    int temp = array.get(0);
                    array.set(0, array.get(size-1));
                    array.set(size-1, temp);
                }
            }
            toReturn.addAll(findPermutation(array, size - 1));
        }
        return toReturn;
    }

    /**
     * Looks for differences in connections among classes with same names
     * @return "Absent", "Extra", "Missing Connection", "Unneeded Connection" consistent with the diff() documentation
     */
    public ArrayList<ArrayList<Object>> diffOneSpecificity(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap<String, UserClass> student = blackboard.getClasses();
        HashMap<String, UserClass> solution = blackboard.getProblemSolution();
        ArrayList<ArrayList<Object>> problems = new ArrayList<>();
        problems.add(new ArrayList<>());
        problems.add(new ArrayList<>());

        for(UserClass stu : student.values()){
            if(solution.get(stu.getName()) == null){
                problems.get(0).add("Extra");
                problems.get(1).add(stu);
                continue;
            }
            getConnectionDiff(problems, stu);
        }

        for(UserClass sol : solution.values()){
            if(student.get(sol.getName()) == null){
                problems.get(0).add("Absent");
                problems.get(1).add(sol);
            }
        }
        return problems;
    }

    /**
     * Find differences in the connections between two classes
     * @param problems An array list of differences
     * @param stu The class to look for differences between
     */
    public void getConnectionDiff(ArrayList<ArrayList<Object>> problems, UserClass stu){
        HashMap<String, UserClass> solution = Blackboard.getInstance().getProblemSolution();
        UserClass sol = solution.get(stu.getName());
        HashMap<UserClass, ArrayList<Point>> stuConnections = stu.getConnections();
        HashMap<UserClass, ArrayList<Point>> solConnections = sol.getConnections();
        for(UserClass stuConClass : stuConnections.keySet()){
            ArrayList<Point> solPoints = solConnections.get(stuConClass);
            solConnections.remove(stuConClass);
            if (solPoints == null){
                for(Point stuCon : stuConnections.get(stuConClass)) {
                    problems.get(0).add("Unneeded Connection " + (int) stuCon.getY() + " " + stuConClass.getName());
                    problems.get(1).add(stu);
                }
                continue;
            }
            for (Point stuCon : stuConnections.get(stuConClass)) {
                if (!solPoints.remove(stuCon)) {
                    problems.get(0).add("Unneeded Connection " + (int) stuCon.getY() + " " + stuConClass.getName());
                    problems.get(1).add(stu);
                }
            }
            for (Point p : solPoints) {
                problems.get(0).add("Missing Connection " + (int) p.getY() + " " + stuConClass.getName());
                problems.get(1).add(stu);
            }
        }
        for(UserClass c : solConnections.keySet()){
            for(Point p : solConnections.get(c)){
                problems.get(0).add("Missing Connection " + (int) p.getY() + " " + c.getName());
                problems.get(1).add(stu);
            }
        }
    }

    //This level may not actually make sense because if the var numbers are wrong then the connections must also be wrong.
    public ArrayList<ArrayList<Object>> diffTwoSpecificty(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap<String, UserClass> student = blackboard.getClasses();
        HashMap<String, UserClass> solution = blackboard.getProblemSolution();
        ArrayList<ArrayList<Object>> problems = new ArrayList<>();

        for(UserClass stu : student.values()){
            if(solution.get(stu.getName()) == null){
                problems.get(0).add("Extra");
                problems.get(1).add(stu);
            }
            //More stuff pertaining to if the vars and methods are equal;
        }

        for(UserClass sol : solution.values()){
            if(student.get(sol.getName()) == null){
                problems.get(0).add("Absent");
                problems.get(1).add(sol);
            }
        }
        return problems;
    }

    /**
     * Looks for differences in the variables and methods between classes of the same name
     * @return "Extra", "Absent", "Unneeded/Missing Item", "Unneeded/Missing Connection" consistent with the diff() documentation.
     */
    public ArrayList<ArrayList<Object>> diffThreeSpecificty(){
        Blackboard blackboard = Blackboard.getInstance();
        HashMap<String, UserClass> student = blackboard.getClasses();
        HashMap<String, UserClass> solution = blackboard.getProblemSolution();
        ArrayList<ArrayList<Object>> problems = new ArrayList<>();
        problems.add(new ArrayList<>());
        problems.add(new ArrayList<>());

        for(UserClass stu : student.values()){
            if(solution.get(stu.getName()) == null){
                problems.get(0).add("Extra");
                problems.get(1).add(stu);
                continue;
            }
            if((solution.get(stu.getName()).getParent() == null
                && stu.getParent() != null) ||
                    (solution.get(stu.getName()).getParent() != null &&
                        !solution.get(stu.getName()).getParent().equals(stu.getParent()))){
                int typeA = stu.getIsImplementation() ? 5 : 1;
                int typeB = solution.get(stu.getName()).getIsImplementation() ? 5 : 1;
                if(stu.getParent() != null) {
                    problems.get(0).add("Unneeded Connection " + typeA + " " + stu.getParent().getName());
                    problems.get(1).add(stu);
                }
                if(solution.get(stu.getName()).getParent() != null) {
                    problems.get(0).add("Missing Connection " + typeB + " " + solution.get(stu.getName()).getParent().getName());
                    problems.get(1).add(stu);
                }
            }
            if(solution.get(stu.getName()).getIsImplementation() != stu.getIsImplementation()){
                int type = stu.getIsImplementation() ? 5 : 1;
                problems.get(0).add("Modify Connection " + type + " " + stu.getParent().getName());
                problems.get(1).add(stu);
            }
            getItemsDiff(problems, stu);
        }

        for(UserClass sol : solution.values()){
            if(student.get(sol.getName()) == null){
                problems.get(0).add("Absent");
                problems.get(1).add(sol);
            }
        }
        return problems;
    }

    /**
     * Gets the differences in items between two UserClasses
     * @param problems An array list of the differences
     * @param stu The class to compare between
     */
    public void getItemsDiff(ArrayList<ArrayList<Object>> problems, UserClass stu) {
        HashMap<String, UserClass> solution = Blackboard.getInstance().getProblemSolution();
        UserClass sol = solution.get(stu.getName());
        Set<String> stuMethods = stu.getMethods();
        Set<String> solMethods = sol.getMethods();
        ArrayList<VariableInformation> stuVars = stu.getAllVars();
        ArrayList<VariableInformation> solVars = sol.getAllVars();

        for(String method : stuMethods){
            if(!solMethods.remove(method)){
                problems.get(0).add("Unneeded Item method "+ method);
                problems.get(1).add(stu);
            }
        }
        for(String method : solMethods){
            problems.get(0).add("Missing Item method "+method);
            problems.get(1).add(stu);
        }
        for(VariableInformation stuVar : stuVars){
            if(!solVars.contains(stuVar)){
                problems.get(0).add("Unneeded Item variable "+ stuVar.getName());
                problems.get(1).add(stu);
                continue;
            }
            VariableInformation solVar = solVars.get(solVars.indexOf(stuVar));
            if (solVar.getConnectedTo() != stuVar.getConnectedTo()
            || solVar.getType() != stuVar.getType()
            || solVar.getInMethod() != stuVar.getInMethod()){
                problems.get(0).add("Modify Item variable " +stuVar.getName());
                problems.get(1).add(stu);
            }
            solVars.remove(stuVar);
        }
        for(VariableInformation solVar : solVars){
            problems.get(0).add("Missing Item variable "+solVar.getName());
            problems.get(1).add(stu);
        }
    }
}