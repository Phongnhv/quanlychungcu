package com.example.qlcc;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    public static Connection connectDB()
    {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");

            Connection connect = DriverManager.getConnection("jdbc:mysql://localhost/quanlychungcu","root","");
            return connect;
        }catch (Exception e) {e.printStackTrace();}
        return null;
    }
}
