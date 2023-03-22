import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.Instant;
import java.util.HashMap;

public class DBtests {

    @Test
    public void testGetDifficulty() throws SQLException{
        DBHandler.getInstance().queryProblem(1);
        Assertions.assertEquals(1, Blackboard.getInstance().getProblemDifficulty());
        DBHandler.getInstance().queryProblem(2);
        Assertions.assertEquals(2, Blackboard.getInstance().getProblemDifficulty());
        DBHandler.getInstance().queryProblem(3);
        Assertions.assertEquals(3, Blackboard.getInstance().getProblemDifficulty());
        Assertions.assertFalse(DBHandler.getInstance().queryProblem(-1));
    }

    @Test
    public void testSuccessfulLogin() throws SQLException {
        Assertions.assertTrue(DBHandler.getInstance().login("user3", "passW"));
    }

    @Test
    public void testFailedLogin() throws SQLException {
        Assertions.assertFalse(DBHandler.getInstance().login("user99", "passW"));
    }

    @Test
    public void testCreateStudentAccount() throws SQLException {
        Assertions.assertTrue(DBHandler.getInstance().createStudentAccount("user" + Instant.now(), "passW"));
    }

    @Test
    public void testCreateStudentAccountFailure() throws SQLException {
        Assertions.assertFalse(DBHandler.getInstance().createStudentAccount("user3", "password"));
    }

    @Test
    public void testCreateProblem() throws SQLException {
        HashMap<String, UserClass> classes = new HashMap<>();
        classes.put("V", new UserClass("V"));
        classes.put("W", new UserClass("W"));
        Blackboard.getInstance().setClasses(classes);
        int id = DBHandler.getInstance().insertNewProblem(1, 1);
        DBHandler.getInstance().queryProblemById(id);
        Assertions.assertEquals(classes, Blackboard.getInstance().getProblemSolution());
    }

    @Test
    public void testGetClassCode(){
        String classCode = "1Z";
        int number = Integer.parseInt(classCode.substring(0, classCode.length() - 1));
        int character = (int) classCode.charAt(classCode.length() - 1);
        if (character == 90){
            number++;
            character = 64;
        }
        String newClassCode = String.valueOf(number) + String.valueOf((char) ++character);
        Assertions.assertEquals("2A", newClassCode);
    }
}
