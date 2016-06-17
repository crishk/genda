package Dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import Modelo.Util;

/**
 * Esta clase maneja la conexión a la base de datos.
 * @author CRISHK
 *
 */
public class DatabaseConnection {
	
	// Parametros globales de conexión
	public static final String JDBC_DRIVER="com.mysql.jdbc.Driver";  
    public static final String DB_URL="jdbc:mysql://localhost/genda?zeroDateTimeBehavior=convertToNull";
    public static final String DB_USER = "root";
    public static final String DB_PASS = "123456789";
    
    public static Connection conn;	// variable de conexión global
    
    // Obtiene la conexión a la base de datos.
    public static Connection getDBConnection()
    { 
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		DatabaseConnection.conn = DriverManager.getConnection(DatabaseConnection.DB_URL, DatabaseConnection.DB_USER, DatabaseConnection.DB_PASS);
    	} 
    	catch (SQLException e) {}
    	catch (ClassNotFoundException e) {}
    	
    	return DatabaseConnection.conn;
    }

}
