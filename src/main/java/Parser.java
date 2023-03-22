import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * CSC 309 UML Tutor
 * 
 * Class to parse the text panel and generate new Class, Method, and Variable information objects.
 * 
 * @author Mario Vuletic
 * @author Alex Arrieta
 * @version 1.0
 */
public class Parser{
    private HashMap<String, UserClass> classes;
    private int offset;

    /**
     * Parses the classes
     * @param text The raw text from the text panel
     * @return The parsed classes in the string and their data in HashMap format, or null if a parsing error occured.
     */
    public HashMap<String,UserClass> parse(String text){
        classes = new HashMap<>();
        offset = -150;
        String[] classesArr = text.split("class +");
        for (int i = 1; i < classesArr.length; i++){
            String[] bracket = classesArr[i].split("\\{", 2);
            UserClass myClass = findClass(bracket[0]);
            if (findVarsAndMethods(myClass, bracket[1]) == 0){
                return null;
            }
        }
        return classes;
    }

    /**
     * Finds the class information from the declaration
     * @param classDec The string that contains the class declaration
     * @return A UserClass that has all of its variables and methods made
     */
    private UserClass findClass(String classDec){
        String[] signature = classDec.split("\\s+");
        String curClass = signature[0].trim();
        UserClass myClass = new UserClass(curClass);
        UserClass temp = classes.putIfAbsent(curClass, myClass);
        offset += 150;
        if(temp != null){
            myClass = temp;
        }
        myClass.setPoint(75 + (offset%450), 50 + (150 * (offset/450)));
        String parent = "";
        if (signature.length == 3){
            parent = signature[2].trim();
            UserClass p = new UserClass(parent);
            temp = classes.putIfAbsent(parent, p);
            if(temp != null){
                p = temp;
            }
            myClass.setParent(p);
            myClass.setIsImplementation(signature[1].trim().equals("implements"));
        }
        return myClass;
    }

    /**
     * Finds all the variables and methods within the body of a class declaration
     * @param myClass The class to add the methods and variables to
     * @param classBody A string containing all the text of the body of the class
     * @return 1 if this successfully parsed the code. 0 if an error occurred
     */
    private int findVarsAndMethods(UserClass myClass, String classBody){
        String[] loc = classBody.split("(?<=;)|(?<=\\()", 2);
        while(!loc[0].matches("\\s*\\}\\s*")){
            if(loc[0].length() == 0){
                JOptionPane.showMessageDialog(null, "Error");
                return 0;
            }
            if(loc[0].charAt(loc[0].length() - 1) == ';'){
                processVars(myClass, loc[0], null);
            }
            else if (loc[0].charAt(loc[0].length() - 1) == '('){
                loc = processMethods(myClass, loc);
                if (loc == null){
                    return 0;
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Error");
                return 0;
            }
            loc = loc[1].split("(?<=;)|(?<=\\()", 2);
        }
        return 1;
    }

    /**
     * Process a variable declaration and inserts a VariableInformation into a UserClass
     * @param myClass The user class to insert the VariableInformation
     * @param varDec The string containing a line of code that is a variable declaration
     * @param method The method this variable was declared in (null if this is a global variable).
     */
    private void processVars(UserClass myClass, String varDec, String method){
        String[] variable = varDec.trim().split(" +");
        String varClassName = variable[0].trim();
        String varName = variable[1].split("( *=| *;)")[0];
        UserClass varClass = new UserClass(varClassName);
        UserClass temp = classes.putIfAbsent(varClassName, varClass);
        if(temp != null){
            varClass = temp;
        }
        if(method != null){
            myClass.makeMethodVar(method, varName, varClass, 0);
            return;
        }
        myClass.addGlobal(varName, varClass, 4);
    }

    /**
     * Processes a method declaration and body
     * @param myClass The class to insert this method into
     * @param loc A string array containing the text of the method declaration and its body
     * @return A string array where the first element is has text that begins directly after the method
     */
    private String[] processMethods(UserClass myClass, String[] loc){
        String[] method = loc[0].trim().split(" +");
        String methodName = new String();
        if (method.length == 3){ //Checks to make sure there is no extra "(" at the end of the method name
            methodName = method[1];
            myClass.makeMethod(methodName);
        }
        else if (method.length == 2){
            methodName = method[1].substring(0, method[1].length() - 1);
            myClass.makeMethod(methodName);
        }
        else{
            JOptionPane.showMessageDialog(null, "Method name error, may be missing a return type");
            return null;
        }
        loc = loc[1].split("\\)", 2);
        if (processMethodParameter(myClass, loc[0], methodName) == 0){
            return null;
        }
        loc = loc[1].trim().replaceFirst("\\{", "").split("(?<=;)|(?<=\\})", 2);
        while(!loc[0].matches("\\s*\\}\\s*")){
            if(loc[0].charAt(loc[0].length() - 1) == ';'){
                String usefulBit = loc[0].trim().split("=")[0];
                if(!usefulBit.contains("(") && usefulBit.split("\\s*").length > 1){
                    processVars(myClass, loc[0]/*usefulBit.split("\\s+")*/, methodName);
                }
            }
            else{
                JOptionPane.showMessageDialog(null, "Method Error");
                return null;
            }
            loc = loc[1].split("(?<=;)|(?<=\\})", 2);
        }
        return loc;
    }

    /**
     * Processes the method parameters of a method
     * @param myClass The class to insert the method into
     * @param methodParams The string containing the method parameters
     * @param method The method these parameters are for
     * @return 1 if the parsing was succesful, 0 otherwise.
     */
    private int processMethodParameter(UserClass myClass, String methodParams, String method){
        if(methodParams.isBlank()){
            return 1;
        }
        String[] methodParam = methodParams.split(",");
        for(String param : methodParam){
            if(param.split("\s+").length == 1){
                JOptionPane.showMessageDialog(null, "This is an invalid parameter");
                return 0;
            }
            String[] temp = param.trim().split("\s+");
            String paramClass = temp[0].trim();
            String paramName = temp[1].trim();
            classes.putIfAbsent(paramClass, new UserClass(paramClass));
            myClass.makeMethodVar(method, paramName, classes.get(paramClass), 3);
        }
        return 1;
    }
}