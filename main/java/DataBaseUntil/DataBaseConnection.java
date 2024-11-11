/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DataBaseUntil;

/**
 *
 * @Dylan Quiros
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    
      private static final String URL = "jdbc:mysql://localhost:3306/FqrCLients";
    private static final String USER = "DylanQuiros";
    private static final String PASSWORD = "19-03-2005Dd";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
}
