
package com.example.studentskill;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL =
            "jdbc:mysql://localhost:3306/student_skill_db";

    private static final String USER = "root";
    private static final String PASSWORD = "Annu_9296";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    public static void main(String[] args) {
        try {
            Connection con = getConnection();

            if (con != null) {
                System.out.println("MySQL Connected Successfully!");
            }

            con.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
