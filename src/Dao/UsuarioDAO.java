package Dao;

import java.util.*;

import Modelo.Actividad;
import Modelo.Contacto;
import Modelo.Usuario;
import java.sql.*;
/**
 * Clase de acceso a datos para manejar los usuarios en la base de datos.
 * 
 * @author CRISHK
 *
 */
public class UsuarioDAO extends ObjetoDAO<Usuario> { 
	
	/**
	 * Constructor.
	 */
	public UsuarioDAO()
	{
		 
	}
	
	/**
	 * Inserta un usuario a partir de un objeto modelo.
	 */
	@Override
	public  boolean insertar( Usuario objetoModelo )  
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("insertar( " +   objetoModelo.getNombres() + " )");
			
			sql = "INSERT INTO usuario(nombres,apellidos,direccion,telefono,correo,nombre_usuario,password,fecha_creacion,fecha_actualizacion)"
					+ "VALUES (?,?,?,?,?,?,md5(?),now(),now())";
			
			ptmt = conn.prepareStatement( sql );
			
			// asignamos los valores al query
			ptmt.setString(1, objetoModelo.getNombres());
			ptmt.setString(2, objetoModelo.getApellidos());
			ptmt.setString(3, objetoModelo.getDireccion());
			ptmt.setString(4, objetoModelo.getTelefono());
			ptmt.setString(5, objetoModelo.getCorreo());
			ptmt.setString(6, objetoModelo.getNombreUsuario());
			ptmt.setString(7, objetoModelo.getPassword());

			System.out.println( ptmt );
			
			if (ptmt.executeUpdate() == 0)
				estado = false;
			else
				estado = true;

		}
		catch (SQLException e) {
			estado = false;
			e.printStackTrace();
		}  finally {
			
			this.cerrarConexion("insertar( " +   objetoModelo.getNombres() + " )");
			

		}	
		return estado;
	}
	
	/**
	 * Actualiza un usuario a partir de un objeto.
	 */
	@Override
	public  boolean actualizar(  Usuario objetoModelo )
	{
		return false;
	}
	
	/**
	 * Elimina usuarios por una lista de codigos.
	 */
	@Override
	public  boolean eliminar( String codigos )
	{
		return false;	
	}
	
	/**
	 * Consulta un usuario especificando el codigo.
	 */
	@Override
	public  Usuario consultar( int codigo )
	{
		return null;
	}
	
	/**
	 * Consulta un usuario en la base de datos.
	 */
	@Override
	public ArrayList<Usuario> consultar( String codigos ) 
	{
		
		return null;
	}	
	
	/**
	 * Construye un objeto usuario a partir de un ResultSet.
	 */
	public Usuario construirObjeto( ResultSet rs ) throws SQLException
	{
		Usuario nuevoUsuario = new Usuario(); 
		nuevoUsuario.setCodigo( rs.getString("codigo") );
		//System.out.println( "Agregando actividad " + rs.getString("nombre") + " Estado: " + rs.getString("estado_actividad"));
		nuevoUsuario.setNombres( rs.getString("nombres") );
		nuevoUsuario.setApellidos( rs.getString("apellidos") );
		nuevoUsuario.setDireccion( rs.getString("direccion") );			
		nuevoUsuario.setTelefono( (rs.getString("telefono")) );
		nuevoUsuario.setCorreo( (rs.getString("correo")) );		
		nuevoUsuario.setNombreUsuario( rs.getString("nombre_usuario") );
		nuevoUsuario.setPassword( rs.getString("password") );
		nuevoUsuario.setFechaCreacion( rs.getTimestamp("fecha_creacion") );	
		nuevoUsuario.setFechaActualizacion( rs.getTimestamp("fecha_actualizacion") );
		nuevoUsuario.setFechaEliminacion( rs.getTimestamp("fecha_eliminacion") );
		nuevoUsuario.setCodigoRecuperacion( rs.getString("codigo_recuperacion") );
		
		return nuevoUsuario;
	}
	

	/**
	 * Consulta los usuarios en la base de datos filtrandolos por el parametro query.
	 */
	public ArrayList<Usuario> consultarQuery( String query )
	{
		return super.consultarQuery( query );
	}
	
	/**
	 * Verifica si un usuario especificado como objeto existe en la base de datos por nombre de usuario o su correo electrónico.
	 * @param usuario
	 * @return
	 */
	public boolean verificarUsuario( Usuario usuario )
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("verificarUsuario( " +   usuario.getNombres() + " )");
			
			sql = "SELECT * FROM usuario WHERE nombre_usuario = ? OR correo = ?";
			
			ptmt = conn.prepareStatement( sql );
			
			// asignamos los valores al query
			ptmt.setString(1, usuario.getNombreUsuario() );
			ptmt.setString(2, usuario.getCorreo() );

			System.out.println( ptmt );
			
			rs = ptmt.executeQuery();
			
			while (rs.next()) {								
				usuario = this.construirObjeto( rs );			
			}
			
			if (usuario.getCodigo() != null)
				return true;

		}
		catch (SQLException e) {
			estado = false;
			e.printStackTrace();
		}  finally {
			
			this.cerrarConexion("verificarUsuario( " +   usuario.getNombres() + " )");
			

		}	
		return estado;
	}
	

	
	
	/**
	 * Verifica si un nombre de usuario esta en uso por otro usuario.
	 * @param nombreUsuario
	 * @return
	 */
	public boolean nombreUsuarioExiste( String nombreUsuario )
	{
		boolean existe = false;
		try {
			conn = this.abrirConexion("verificarUsuario( " +  nombreUsuario + " )");
			stmt = conn.createStatement();		
			sql = "SELECT * FROM usuario WHERE nombre_usuario LIKE '" + nombreUsuario + "' or correo LIKE '" + nombreUsuario + "'";																					
			System.out.println( sql );
			rs = stmt.executeQuery( sql );
			while (rs.next()) {
				existe = true;			
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("verificarUsuario( " +   nombreUsuario + " )");
		}
		
		return existe;
	}

	/**
	 * Verifica si un correo electrónico esta en uso por otro usuario.
	 * @param correo
	 * @return
	 */
	public boolean correoEstaEnUso( String correo )
	{		
		boolean estaEnUso = false;
		try {
			conn = this.abrirConexion("correoEstaEnUso( " +  correo + " )");
			stmt = conn.createStatement();		
			sql = "SELECT * FROM usuario WHERE correo LIKE '" + correo + "'";																					
			rs = stmt.executeQuery( sql );
			while (rs.next()) {
				estaEnUso = true;			
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("correoEstaEnUso( " +  correo + " )");
		}
		
		return estaEnUso;
	}
	

	
	
	
	/**
	 * Genera un código de recuperación de contraseña en la base de datos para
	 * que el usuario pueda reestablecer su contraseña usando un vínculo de recuperación válido
	 * el cual es enviado a su correo.
	 * @param nombreUsuario
	 * @return
	 */
	public boolean generarCodigoRecuperacion( String nombreUsuario )
	{
		boolean generoCodigo = false;
		String codigoRecuperacion = "md5(now())";
		
		try {
			conn = this.abrirConexion("generarCodigoRecuperacion( " +   nombreUsuario + " )");			
			
			
			sql = "UPDATE usuario SET codigo_recuperacion = " 
					+ codigoRecuperacion 
					+ " WHERE nombre_usuario = ?";
			
			ptmt = conn.prepareStatement( sql );
			ptmt.setString(1, nombreUsuario);
			
			generoCodigo = (ptmt.executeUpdate() != 0 ? true : false);

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("generarCodigoRecuperacion( " +   nombreUsuario + " )");
		}
		
		return generoCodigo;		
		
	}
	
	/**
	 * Obtiene un codigo de recuperación basado en el nombre de usuario.
	 * @param nombreUsuario
	 * @return
	 */
	public String getCodigoRecuperacion( String nombreUsuario )
	{
		String codigoRecuperacion = null;
		try {
			conn = this.abrirConexion("generarCodigoRecuperacion( " +   nombreUsuario + " )");			
			stmt = conn.createStatement();
			
			sql = "SELECT codigo_recuperacion FROM usuario WHERE nombre_usuario = '" + nombreUsuario + "'";
			
			rs = stmt.executeQuery( sql );
			
			while (rs.next()) {
				codigoRecuperacion = rs.getString("codigo_recuperacion");						
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("generarCodigoRecuperacion( " +   nombreUsuario + " )");
		}
		
		return codigoRecuperacion;
	}
	
	/**
	 * Valida un inicio de sesión del usuario, recibe el nombre del usuario y la contraseña de la cuenta.
	 * @param loginName
	 * @return
	 */
	public Usuario validarLogin( String nombreUsuario, String password )
	{
		Usuario usuario = null;
		
		try {
			conn = this.abrirConexion("validarLogin( " +   nombreUsuario + ","  + password + " )");			
			
			
			sql = "SELECT * FROM usuario "
					+ " WHERE (nombre_usuario LIKE ? OR correo LIKE ?)"
					+ " AND password LIKE md5(?) "
							+ " AND fecha_eliminacion IS NULL";
			
			ptmt = conn.prepareStatement( sql );
			ptmt.setString(1, nombreUsuario );
			ptmt.setString(2, nombreUsuario );
			ptmt.setString(3, password);
			
			System.out.println( ptmt );
			
			rs = ptmt.executeQuery();
			
			while (rs.next()) {
				System.out.println("Encontro un usuario..");
				usuario = this.construirObjeto(rs);			
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("generarCodigoRecuperacion(" +   nombreUsuario + ","  + password + " )");
		}
		
		return usuario;
	}
	
	
	/**
	 * Recupera un password del nombre de usuario especificado.
	 * @param nombreUsuario
	 * @return
	 */
	public boolean recuperarPassword( String nombreUsuario )
	{
		
		boolean recuperacionValida = false;
		try {
			conn = this.abrirConexion("recuperarPassword( " +   nombreUsuario + " )");			
			stmt = conn.createStatement();
			
			sql = "SELECT * FROM usuario WHERE nombre_usuario = '" + nombreUsuario + "'";
			System.out.println( sql );
			rs = stmt.executeQuery( sql );
			
			while (rs.next()) {
				this.generarCodigoRecuperacion( nombreUsuario );
				
				recuperacionValida = true;			
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("recuperarPassword( " +   nombreUsuario + " )");
		}
		
		return recuperacionValida;
	}
	
	/**
	 * Permite reestablecer el nuevo password.
	 * @param nuevoPassword
	 * @return
	 */
	public boolean reestablecerPassword( String nuevoPassword, String codigo_recuperacion, String nombre_usuario )
	{		
		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		boolean estado = false;
		try {
			conn = this.abrirConexion("reestablecerPassword( " +   nuevoPassword + ", " + codigo_recuperacion + "," + nombre_usuario + " )");
			
			sql = "UPDATE usuario SET password = md5(?), codigo_recuperacion = null, fecha_actualizacion = now() WHERE nombre_usuario = ? AND codigo_recuperacion = ?";
			
			ptmt = conn.prepareStatement( sql );
			
			// asignamos los valores al query
			ptmt.setString(1, nuevoPassword);
			ptmt.setString(2, nombre_usuario );
			ptmt.setString(3, codigo_recuperacion);


			System.out.println( ptmt );
			
			if (ptmt.executeUpdate() == 0)
				estado = false;
			else
				estado = true;

		}
		catch (SQLException e) {
			estado = false;
			e.printStackTrace();
		}  finally {
			
			this.cerrarConexion("reestablecerPassword( " +   nuevoPassword + ", " + codigo_recuperacion + "," + nombre_usuario + " )");
			

		}	
		return estado;
		
		
	}
	
	
}