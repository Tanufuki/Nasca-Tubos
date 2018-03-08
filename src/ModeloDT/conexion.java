/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ModeloDT;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Angelo
 */
public class conexion {
    Connection conn;
    Statement instru;
    /*
    String baseDatos = "regtubos";
    String user = "nasca218";
    String pass="pL0mada$256";
    String servidor = "10.8.0.1:3306";
    */
    String baseDatos = "regtubos";
    String user = "root";
    String pass="";
    String servidor = "LocalHost:3306";
    
    public conexion() throws Exception
    {
        try
        {
          Class.forName("com.mysql.jdbc.Driver");
          conn = DriverManager.getConnection("jdbc:mysql://"+servidor+"/"+baseDatos,user,pass);
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
    }
    
    public Statement getStatement() throws Exception
    {
        try
        {
            instru = conn.createStatement();
        }catch(SQLException ex)
        {
            throw new Exception(ex.getMessage());
        }
        return instru;
    }
    
}
