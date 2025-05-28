package education.center.management.system;

import java.sql.*;

public class Conn implements AutoCloseable {

    private Connection conn;
    private Statement stmt;

    public Conn() {
        try {
            // JDBC driver betöltése
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Adatbázis kapcsolat
            conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/educationcentermanagementsystem",
                "root",
                "12345"
            );

            // Statement létrehozása
            stmt = conn.createStatement();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Getterek
    public Connection getConnection() {
        return conn;
    }

    public Statement getStatement() {
        return stmt;
    }

    // Automatikus lezárás try-with-resources használathoz
    @Override
    public void close() {
        try {
            if (stmt != null && !stmt.isClosed()) stmt.close();
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
