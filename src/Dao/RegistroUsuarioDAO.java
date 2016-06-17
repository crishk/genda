package Dao;

import java.sql.SQLException;

import Modelo.Actividad;
import Modelo.Usuario;

/**
 * Insertar un usuario, por cuestiones de legibilidad se creo esta clase.
 * @author CRISHK
 *
 */
public class RegistroUsuarioDAO extends UsuarioDAO {
	/**
	 * Constructor.
	 */
	public RegistroUsuarioDAO()
	{
		 
	}
	
	/**
	 * insertar
	 */
	@Override
	public  boolean insertar( Usuario usuario ) 
	{
		return super.insertar( usuario );					
	}
	
	

}
