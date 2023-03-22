
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.ArrayList;

/**
 * CSC 309 UML Tutor
 * Class to handle database reads and writes
 * 
 * @author Mario Vuletic
 * @author Thien An Tran
 * @author Chloe Anbarcioglu
 * @version 1.0
 */
public class DBHandler {
    private static DBHandler dbhandler;

    private DBHandler(){
        
    }

    /**
     * Gets singleton instance of DBHandler
     * @return The singleton DBHandler object
     */
    public static DBHandler getInstance(){
        if (dbhandler == null){
            return new DBHandler();
        }
        return dbhandler;
    }

    /**
     * Reads a problem from the database with a particular ID and updates blackboard data with it
     * @param problemNum : The problem ID to query
     */
    public void queryProblemById(int problemNum){
        String problemDesc = "";
        String problemCode = "";
        int problemId = problemNum;
        int problemDifficulty = -1;
        int problemType = -1;
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {  
            String sql = "SELECT * FROM sql9601527.problems WHERE problemId = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, problemNum);
                ResultSet rs = ps.executeQuery();
                while (rs.next()){
                    problemId = rs.getInt(1);
                    problemDesc = rs.getString(2);
                    problemCode = rs.getString(3);
                    problemDifficulty = rs.getInt(4);
                    problemType = rs.getInt(5);
                }
            }
            conn.close();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no question loaded?");
            return;
        }
        Blackboard.getInstance().setProblemId(problemId);
        Blackboard.getInstance().setProblemType(problemType);
        Blackboard.getInstance().setProblemDescription(problemDesc);
        Blackboard.getInstance().setProblemDifficulty(problemDifficulty);
        Blackboard.getInstance().setProblemSolution((new Parser()).parse(problemCode));
        ArrayList<String> soln = getCurrentStudentSolution(Blackboard.getInstance().getUserID(), problemId);
        if(soln != null){
            new TextPanel().parseInput(soln.get(0));
        }
    }

    public ArrayList<String> getCurrentStudentSolution(String user, int ProblemID){
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            String sql = "SELECT solution, status FROM sql9601527.studentProblemSolutions WHERE studentID = ? AND problemID = ?;";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setObject(1, user);
                ps.setObject(2, ProblemID);
                ResultSet rs = ps.executeQuery();
                if(rs.isBeforeFirst()) {
                    rs.next();
                    ArrayList<String> list = new ArrayList<>();
                    list.add(rs.getString(1));
                    list.add(String.valueOf(rs.getInt(2)));
                    return list;
                }
                rs.close();
            }
            conn.close();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no answer loaded");
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * Queries a problem from the database given difficulty or a problem id?
     * This updates the blackboard with the problem information and returns nothing
     * @param difficulty The difficulty of the desired problem
     */
    public boolean queryProblem(int difficulty){
        String problemDesc = "";
        String problemCode = "";
        int problemId = -1;
        int problemDifficulty = difficulty;
        int problemType = -1;
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            String sql = "SELECT * FROM sql9601527.problems WHERE difficulty = ? AND code IS NOT NULL LIMIT 1;";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setInt(1, difficulty);
                ResultSet rs = ps.executeQuery();
                if (!rs.isBeforeFirst()){
                    throw new SQLException();
                }
                while (rs.next()){
                    problemId = rs.getInt(1);
                    problemDesc = rs.getString(2);
                    problemCode = rs.getString(3);
                    problemDifficulty = rs.getInt(4);
                    problemType = rs.getInt(5);
                }
            }
            conn.close();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no question loaded");
            return false;
        }
        Blackboard.getInstance().setProblemId(problemId);
        Blackboard.getInstance().setProblemType(problemType);
        Blackboard.getInstance().setProblemDescription(problemDesc);
        Blackboard.getInstance().setProblemDifficulty(problemDifficulty);
        Blackboard.getInstance().setProblemSolution((new Parser()).parse(problemCode));
        ArrayList<String> soln = getCurrentStudentSolution(Blackboard.getInstance().getUserID(), problemId);
        if(soln != null){
            new TextPanel().parseInput(soln.get(0));
        }
        Blackboard.getInstance().setProblemType(problemType);
        return true;
    }

    /**
     * Gets a random problem from the database
     * This updates the blackboard with the problem information and returns nothing
     */
    public void queryRandomProblem(){
        String problemDesc = "";
        String problemCode = "";
        int problemId = -1;
        int problemDifficulty = 1;
        int problemType = -1;
        try (Connection conn = ConnectionPool.getInstance().getConnection()){
            String sql = "SELECT * FROM sql9601527.problems WHERE code IS NOT NULL ORDER BY RAND() LIMIT 1;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                problemId = rs.getInt(1);
                problemDesc = rs.getString(2);
                problemCode = rs.getString(3);
                problemDifficulty = rs.getInt(4);
                problemType = rs.getInt(5);
            }
            conn.close();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no question loaded");
            return;
        }
        Blackboard.getInstance().setProblemId(problemId);
        Blackboard.getInstance().setProblemType(problemType);
        Blackboard.getInstance().setProblemDescription(problemDesc);
        Blackboard.getInstance().setProblemDifficulty(problemDifficulty);
        Blackboard.getInstance().setProblemSolution((new Parser()).parse(problemCode));
        ArrayList<String> soln = getCurrentStudentSolution(Blackboard.getInstance().getUserID(), problemId);
        if(soln != null){
            new TextPanel().parseInput(soln.get(0));
        }
        Blackboard.getInstance().setProblemType(problemType);
    }

    /**
     * Checks if a user exists in the database
     * @param username : The inputted username by the user
     * @param password : The inputted password by the user
     * @return The truth-value of if a user with the specified credentials exists in the database.
     * @throws SQLException
     */
    public boolean login(String username, String password) {
        boolean t;
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {                                               
            String sql = "SELECT * FROM sql9601527.users WHERE username = ? AND password = ?;";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setObject(1, username);
                ps.setObject(2, password);
                ResultSet rs = ps.executeQuery();
                t = rs.isBeforeFirst(); // This is false if no rows were returned
                conn.close();
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "No such user found");
            return false;
        }
        return t;
    }

   /**
     * Creates a new student account in the database
     * @param user : The user-specified username
     * @param pass : The user-specified password
     * @return If the write was successful or nots
     */
    public boolean createStudentAccount(String user, String pass) {
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            String sql = "INSERT INTO sql9601527.users VALUES (?, ?, ?);";
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setString(1, user);
                ps.setString(2, pass);
                ps.setString(3, "student");
                ps.executeUpdate();
            }
            
            String sql2 = "INSERT INTO sql9601527.students VALUES (?);";
            try (PreparedStatement ps2 = conn.prepareStatement(sql2)){
                ps2.setString(1, user);
                ps2.executeUpdate();
            }
            conn.commit();
            conn.close();
            return true;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Database error, no user created");
            return false;
        }
    }
    

    /**
     * Creates a new professor account in the database
     * @param user : The user-specified username
     * @param pass : The user-specified password
     * @return If the write was successful or nots
     */
    public boolean createProfAccount(String user, String pass) {
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            String sql = "INSERT INTO sql9601527.users VALUES (?, ?, ?);";
            conn.setAutoCommit(false);
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setObject(1, user);
                ps.setObject(2, pass);
                ps.setObject(3, "professor");
                ps.executeUpdate();
            }
            sql = "INSERT INTO sql9601527.professors VALUES (?, ?);";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setObject(1, user);
                ps.setObject(2,"user0");
                ps.executeUpdate();
            }
            conn.commit();
            conn.close();
        } catch (SQLException s) { JOptionPane.showMessageDialog(null, "Database error, no user created"); return false;}
        return true;
    }

    /**
     * Gets the account type of the specified user
     * @param username : The user-specified username
     * @param password : The user-specified password
     * @return A string of the user's account type
     */
    public String getAccType(String username, String password) {
        String acc = "";
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            String sql = "SELECT * FROM sql9601527.users WHERE username = ? AND password = ?;";
            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setObject(1, username);
                ps.setObject(2, password);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String u = rs.getString("accountType");
                    if(u.equals("student")){
                        acc = "Student";
                    }
                    if(u.equals("professor")){
                        acc = "Professor";
                    }
                }
            }
            conn.close();
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, not able to get account type");
        }
        return acc;
    }

    /**
     * Gets all the problems current in the database
     * @return And ArrayList containing ArrayLists of the information of a problem.
     * Returns an empty list if there are no problems and null if there is an error in loading.
     */
    public ArrayList<ArrayList<String>> getProblems(){
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM sql9601527.problems";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ArrayList<ArrayList<String>> problems = new ArrayList<>();
            ArrayList<String> problem = new ArrayList<>();
            while (rs.next()) {
                int i = 1;
                while(i <= columnCount) {
                    problem.add(rs.getString(i++));
                }
                problems.add(problem);
                problem = new ArrayList<>();
            }
            conn.close();
            return problems;
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no problems gotten");
            return null;
        }
    }

    /**
     * Gets all the problems current in the database
     * @return And ArrayList containg ArrayLists of the information of a problem.
     * Returns an empty list if there are no problems and null if there is an error in loading.
     */
    public ArrayList<ArrayList<String>> getCodeUMLProblems(){
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM sql9601527.problems WHERE type = 1";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ArrayList<ArrayList<String>> problems = new ArrayList<>();
            ArrayList<String> problem = new ArrayList<>();
            while (rs.next()) {
                int i = 1;
                while(i <= columnCount) {
                    problem.add(rs.getString(i++));
                }
                problems.add(problem);
                problem = new ArrayList<>();

            }
            conn.close();
            return problems;
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no problems gotten");
            return null;
        }
    }

    /**
     * Gets all the problems current in the database
     * @return And ArrayList containg ArrayLists of the information of a problem.
     * Returns an empty list if there are no problems and null if there is an error in loading.
     */
    public ArrayList<ArrayList<String>> getUMLCodeProblems(){
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM sql9601527.problems WHERE type = 2";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ArrayList<ArrayList<String>> problems = new ArrayList<>();
            ArrayList<String> problem = new ArrayList<>();
            while (rs.next()) {
                int i = 1;
                while(i <= columnCount) {
                    problem.add(rs.getString(i++));
                }
                problems.add(problem);
                problem = new ArrayList<>();

            }
            conn.close();
            return problems;
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no problems gotten");
            return null;
        }
    }

    /**
     * Gets all the problems current in the database
     * @return And ArrayList containg ArrayLists of the information of a problem.
     * Returns an empty list if there are no problems and null if there is an error in loading.
     */
    public ArrayList<ArrayList<String>> getUMLMetricsProblems(){
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT * FROM sql9601527.problems WHERE type = 3";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            ArrayList<ArrayList<String>> problems = new ArrayList<>();
            ArrayList<String> problem = new ArrayList<>();
            while (rs.next()) {
                int i = 1;
                while(i <= columnCount) {
                    problem.add(rs.getString(i++));
                }
                problems.add(problem);
                problem = new ArrayList<>();
            }
            conn.close();
            return problems;
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no problems gotten");
            return null;
        }
    }

    /**
     * Inserts the student's metrics into the database
     */
    public void insertUserMetrics() {
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            String sql = "INSERT INTO sql9601527.attempts VALUES (?, ?, ?, ?, ?);";
            conn.setAutoCommit(false);

            String userID = Blackboard.getInstance().getUserID();
            int problemID = Blackboard.getInstance().getProblemId();
            String submissionTime = Blackboard.getInstance().getMetrics().getCurrDateTime();
            String timeTaken = Blackboard.getInstance().getMetrics().getTimeTaken();

            try (PreparedStatement ps = conn.prepareStatement(sql)){
                ps.setObject(1, userID);
                ps.setObject(2, problemID);
                ps.setString(3, submissionTime);
                ps.setObject(4, 0);
                ps.setString(5, timeTaken);
                ps.executeUpdate();
            }
            conn.commit();
            conn.close();
        } catch (SQLException s) { JOptionPane.showMessageDialog(null, "Database error, not able to insert data");
            s.printStackTrace();}
    }

    /**
     * Adds or updates the student's solution for the problem they are working on
     * @param corrent An int that represents whether the student got the problem correct or not
     */
    public void addStudentSolution(int correct){
        String user = Blackboard.getInstance().getUserID();
        int problemID = Blackboard.getInstance().getProblemId();
        String solution = new TextPanel().recode();
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            String sql = "SELECT * FROM sql9601527.studentProblemSolutions WHERE studentID = ? AND problemID = ?;";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setObject(1, user);
            ps.setObject(2, problemID);
            ResultSet rs = ps.executeQuery();
            conn.setAutoCommit(false);

            if(rs.isBeforeFirst()){
                sql = "UPDATE sql9601527.studentProblemSolutions SET solution = ?, status = ? WHERE studentID = ? AND problemID = ?;";
                try (PreparedStatement pss = conn.prepareStatement(sql)){
                    pss.setObject(3, user);
                    pss.setObject(4, problemID);
                    pss.setString(1, solution);
                    pss.setInt(2, correct);
                    pss.executeUpdate();
                }
                conn.commit();
                return;
            }
            sql = "INSERT INTO sql9601527.studentProblemSolutions VALUES (?, ?, ?, ?);";
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);
            ps.setObject(1, user);
            ps.setObject(2, problemID);
            ps.setString(3, solution);
            ps.setInt(4, correct);
            ps.executeUpdate();
            conn.commit();
            conn.close();
        } catch (SQLException s) { JOptionPane.showMessageDialog(null, "Database error, not able to insert data");
            s.printStackTrace();}
    }

    /**
     * Inserts a new problem record into the 'problems' table in the SQL database.
     * The id of the inserted record will be returned.
     * @param difficulty : The selected difficulty from the option pane
     * @param problemType : The selected problem type from the option pane
     * @return The id of the newly inserted record in the 'problems' table.
     */
    public int insertNewProblem(int difficulty, int problemType) {
        int id = -1;
        String sql = "INSERT INTO sql9601527.problems (problemID, description, code, difficulty, type) VALUES (?, ?, ?, ?, ?);";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ResultSet temp = conn.prepareStatement("SELECT MAX(problemID) FROM sql9601527.problems;").executeQuery();
            temp.next();
            int pID = temp.getInt(1);
            ps.setInt(1, pID+1);
            ps.setString(2, Blackboard.getInstance().getProblemDescriptionForSubmit(problemType));
            ps.setString(3, (new TextPanel()).recode());
            ps.setInt(4, difficulty);
            ps.setInt(5, problemType);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) { id = rs.getInt(1); }
            conn.close();
        } catch (SQLException s){
            JOptionPane.showMessageDialog(null, "Database error, not able to insert data");
            System.out.println(s.getMessage());
        }
        return id;
    }

    /**
     * Updates a problem's difficulty and/or problem type
     * @param difficulty : The selected difficulty from the option pane
     * @param problemType : The selected problem type from the option pane
     * @return The boolean value of whether the problemm was successfully updated
     */
    public boolean updateProblem(int difficulty, int problemType) {
        String sql = "UPDATE sql9601527.problems SET description = ?, code = ?, difficulty = ?, type = ? WHERE problemID = ?;";
        try (Connection conn = ConnectionPool.getInstance().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, Blackboard.getInstance().getProblemDescriptionForSubmit(problemType));
            ps.setString(2, (new TextPanel()).recode());
            ps.setInt(3, difficulty);
            ps.setInt(4, problemType);
            ps.setInt(5, Blackboard.getInstance().getProblemId());
            ps.executeUpdate();
            conn.close();
        } catch (SQLException s){
            JOptionPane.showMessageDialog(null, "Database error, not able to insert data");
            System.out.println(s.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Deletes everything related to the problem from the database
     * @return boolean determining whether the deletion was successful or not
     */
    public boolean deleteProblem() {
        String SPSsql = "DELETE FROM sql9601527.studentProblemSolutions WHERE problemID = ?;";
        String Asql = "DELETE FROM sql9601527.attempts WHERE problemID = ?;";
        String Psql = "DELETE FROM sql9601527.problems WHERE problemID = ?;";
        try (Connection conn = ConnectionPool.getInstance().getConnection()) {
            PreparedStatement ps = conn.prepareStatement(SPSsql);
            ps.setInt(1, Blackboard.getInstance().getProblemId());
            ps.executeUpdate();
            ps = conn.prepareStatement(Asql);
            ps.setInt(1, Blackboard.getInstance().getProblemId());
            ps.executeUpdate();
            ps = conn.prepareStatement(Psql);
            ps.setInt(1, Blackboard.getInstance().getProblemId());
            ps.executeUpdate();
            conn.close();
        } catch (SQLException s){
            JOptionPane.showMessageDialog(null, "Database error, could not delete problem");
            System.out.println(s.getMessage());
            return false;
        }
        return true;
    }

    /**
     * Gets table of metrics of each user for that particular problem: total time taken, number of attempts, and status
     * @param problemId : The id of selected problem
     * @return A JTable from the resulting query
     * Returns an empty list if there are no data and null if there is an error in loading.
     */
    public JTable getAllStudentsProblemMetric(int problemId){
        try {
            Connection conn = ConnectionPool.getInstance().getConnection();
            String sql = "SELECT a.student, \n" +
                    "\tSEC_TO_TIME(SUM(TIME_TO_SEC(a.timeTaken))) AS total_time, \n" +
                    "    \tCOUNT(a.student) AS attempts_taken,\n" +
                    "    \tps.status\n" +
                    "FROM attempts a, problems p, studentProblemSolutions ps\n" +
                    "WHERE a.problemID = p.problemID\n" +
                    "\tAND ps.studentID = a.student\n" +
                    "    \tAND ps.problemID = p.problemID\n" +
                    "\tAND p.problemID = ?\n" +
                    "GROUP BY a.student;";

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setObject(1, problemId);
                ResultSet rs = ps.executeQuery();
                ResultSetMetaData md = rs.getMetaData();
                JTable tblData = new JTable();
                DefaultTableModel model = (DefaultTableModel) tblData.getModel();

                int cols = md.getColumnCount();
                String[] colName = new String[cols];
                for (int i = 0; i < cols; i++) {
                    colName[i] = md.getColumnName(i + 1);
                }
                model.setColumnIdentifiers(colName);
                String id, timeSum, attempts, status;
                while (rs.next()) {
                    id = rs.getString(1);
                    timeSum = rs.getString(2);
                    attempts = rs.getString(3);
                    status = rs.getString(4);
                    if(status.equals("0")){
                        status = "In progress";
                    }
                    if(status.equals("1")){
                        status = "Not Started";
                    }
                    if(status.equals("2")){
                        status = "Done";
                    }
                    String[] row = {id, timeSum, attempts, status};
                    model.addRow(row);
                }
                conn.close();
                return tblData;
            }
        } catch (SQLException e){
            JOptionPane.showMessageDialog(null, "Query issue, no data found");
            return null;
        }
    }
}
