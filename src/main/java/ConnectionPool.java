import java.sql.*;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * CSC 309 UML Tutor
 * Class to pool database connections for resource sustainability
 * 
 * @author Mario Vuletic
 */
public class ConnectionPool {
    private static BasicDataSource ds = new BasicDataSource();
    private static ConnectionPool cp;
    
    /**
     * ConnectionPool constructor
     */
    private ConnectionPool() {
        ds.setUrl("jdbc:mysql://sql9.freesqldatabase.com/sql9601527");
        ds.setUsername("sql9601527");
        ds.setPassword("sVpWmf6ypC");
    }

    /**
     * This method checks if a connection pool object is initially created.
     * If not, it creates a new object of the class ConnectionPool and returns it. 
     * @return cp : an instance of ConnectionPool.
     */
    public static ConnectionPool getInstance(){
            if (cp == null) { cp = new ConnectionPool(); }
            return cp;
    }
    
    /**
     * Returns a Connection object instance for the underlying connection pool.
     * This may create a new connection, or re-use an existing one.
     * @return a Connection instance that can be used to interact with the database
     * @throws SQLException
     */
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
    
    /**
     * This method closes the connection parameter passed to it.
     * @param conn a Connection object to be closed
     */
    public void closeConnection(Connection conn) {
        if (conn != null) {
            try { conn.close(); } 
            catch (SQLException e) { e.printStackTrace(); }
        }
    }
}
