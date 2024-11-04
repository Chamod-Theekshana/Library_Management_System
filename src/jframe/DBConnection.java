/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jframe;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author Chamod
 */
public class DBConnection {
    
    static Connection con=null;
    
public static Connection getconnection(){

     try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con=DriverManager.getConnection("jdbc:mysql://localhost:3306/lms","root","Chamodt20");
        } catch (Exception e) {
            e.printStackTrace();
        }
     return con;
}
}
