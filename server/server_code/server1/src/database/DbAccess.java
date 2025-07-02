package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DbAccess {
    private String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver"; 
    private final String DBMS = "jdbc:mysql"; 
    private final String SERVER = "localhost";
    private final String DATABASE = "MapDB"; 
    private final String PORT = "3306"; 
    private final String USER_ID = "MapUser";
    private final String PASSWORD = "map"; 
    private Connection conn; 

    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            String connectionString = DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE
                + "?user=" + USER_ID + "&password=" + PASSWORD + "&serverTimezone=UTC";

            conn = DriverManager.getConnection(connectionString);
        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseConnectionException("Errore di connessione al database", e);
        }
    }

    public Connection getConnection() {
        return conn;
    }

    public void closeConnection() throws DatabaseConnectionException {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Errore nella chiusura della connessione", e);
        }
    }
}
