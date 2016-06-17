package Dao;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Modelo.Resumen;
public class ResumenDAO extends ObjetoDAO<Resumen> {


/**
	 * Inserta un objeto en la base de datos.
	 * @param objetoModelo
	 * @throws SQLException
	 */
	public boolean insertar( Resumen objetoModelo )
	{
		return false;
	}
	
	/**
	 * Eliminar una serie de objetos en base a una lista de codigos separada por comas.
	 * Ejemplo: 1,2,4,7,5 
	 * @param codigos
	 * @throws SQLException
	 */
	public  boolean eliminar(  String codigos ) 
	{
		return false;
	}
	
	/**
	 * En base al codigo del objetoModelo actualiza los atributos del objeto en la base de datos.
	 * @param objetoModelo
	 * @throws SQLException
	 */
	public  boolean actualizar(  Resumen objetoModelo ) 
	{
		return false;
	}
	
	
	/**
	 * Cada ObjetoDAO debe proporcionar esta funcion que recibiendo un ResultSet devuelve un objeto
	 * con las propiedades directamente desde la tabla. De esta manera evitamos estar duplicando el mismo codigo en las diferentes consultas.
	 * @param rs
	 * @return
	 */
	public  Resumen construirObjeto( ResultSet rs ) throws SQLException
	{
		return null;
	}

	/**
	 * Consultar resumen
	 */
	public  Resumen consultar( int codigo ) 
	{
		return null;
	}
	
	/**
	 * Consultar query
	 */
	public  ArrayList<Resumen> consultarQuery( String query )
	{
		return null;
	}
	
	/**
	 * Consultar codigos
	 */
	public  ArrayList<Resumen> consultar( String codigos )
	{
		return null;
	}
}
