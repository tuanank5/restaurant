package org.openjfx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class TestSQLServerConnection {
    public static void main(String[] args) {
        String url = "jdbc:sqlserver://localhost:1433;databaseName=QLNH;encrypt=true;trustServerCertificate=true";
        String user = "sa";
        String password = "123";

        try {
            // Load driver JDBC SQL Server
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

            // Tạo kết nối
            Connection conn = DriverManager.getConnection(url, user, password);

            if (conn != null) {
                System.out.println("Kết nối thành công!");
                conn.close(); // Đóng kết nối
            } else {
                System.out.println("Thất bại khi kết nối.");
            }
        } catch (ClassNotFoundException e) {
            System.out.println("Không tìm thấy driver JDBC SQL Server.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Lỗi SQL khi kết nối.");
            e.printStackTrace();
        }
    }
}
