import java.util.Objects;

/**
 * CSC 309 UML Tutor
 * Class to store variable information
 *
 * @author Mario Vuletic
 * @author Alex Arrieta
 * @version 1.1
 */
public class VariableInformation{
    private String name;
    private UserClass connectedTo;
    private int type; //0 is a method var, 2 is a non null global, 3 is a method param, 4 is a null allowed global
    private String inMethod; //A null represents no method

    /**
     * VariableInformation constructor
     * @param name The name of the variable
     * @param connectedTo The class the variable is connected to
     * @param type The type of variable
     * @param inMethod The method the variable is in
     */
    public VariableInformation(String name, UserClass connectedTo, int type, String inMethod){
        this.name = name;
        this.connectedTo = connectedTo;
        this.type = type;
        this.inMethod = inMethod;
    }

    /**
     * Get the UserClass this variable is a type of
     * @return UserClass
     */
    public UserClass getConnectedTo() {
        return connectedTo;
    }

    /**
     * Set the name of this variable
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get the name of this variable
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Get the type of this variable
     * @return type
     */
    public int getType() {
        return type;
    }

    /**
     * Get the method this is in (Can be null)
     * @return method name
     */
    public String getInMethod() {
        return inMethod;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VariableInformation that = (VariableInformation) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
