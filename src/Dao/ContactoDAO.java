package Dao;

import java.util.*;

import Modelo.Actividad;
import Modelo.Contacto;
import Modelo.Nota;
import Modelo.Util;

import java.sql.*;

/**
 * Clase para acceso a datos de Contactos
 * @author CRISHK
 *
 */
public class ContactoDAO extends ObjetoDAO<Contacto> {
	
	/**
	 * Constructor
	 */
	public ContactoDAO()
	{
		
	}
	
	/**
	 * El nombre personalizado no puede existir para el contacto ni para elementos que ya fueron creados y esten eliminados
	 * ni para los que esten activos.
	 */
	@Override
	public boolean insertar( Contacto objetoModelo ) 
	{
		boolean estado = false;
		// Nombre personalizado existe no puede ser insertado.
		if (this.nombrePersonalizadoExiste(objetoModelo.getNombrePersonalizado())) {
			return false;
		}
		
		try {

			conn = this.abrirConexion("insertar(" + objetoModelo.getNombres() + ")");
			
			sql = "INSERT "
					+ "INTO contacto(nombres, apellidos, nombre_personalizado, direccion, telefono, celular, correo, fecha_creacion, fecha_actualizacion, codigo_usuario)"
					+ "VALUES (?,?,?,?,?,?,?,now(),now(),?)";
			
			ptmt = conn.prepareStatement( sql );															
			ptmt.setString(1, objetoModelo.getNombres());
			ptmt.setString(2, objetoModelo.getApellidos());
			ptmt.setString(3, objetoModelo.getNombrePersonalizado());
			ptmt.setString(4, objetoModelo.getDireccion());
			ptmt.setString(5, objetoModelo.getTelefono());
			ptmt.setString(6, objetoModelo.getCelular());
			ptmt.setString(7, objetoModelo.getCorreo());
			ptmt.setInt(8, Integer.parseInt( objetoModelo.getUsuario().getCodigo() ) );
			System.out.println( ptmt );
			
			if (ptmt.executeUpdate() != 0)
				estado = true;


		}
		catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			this.cerrarConexion("insertar(" + objetoModelo.getNombres() + ")");
		}	
		
		return estado;
	}
	
	/**
	 * actualizar
	 */
	@Override
	public  boolean actualizar( Contacto objetoModelo ) 
	{		
		boolean estado = false;
		try {
			conn = this.abrirConexion("actualizar(" + objetoModelo.getNombres() + ")");
			
			sql = "UPDATE contacto SET nombres = ?, apellidos = ?, nombre_personalizado = ?, direccion = ?, telefono = ?, celular = ?, correo = ?, fecha_actualizacion = now()"
					+ " WHERE codigo = ?";
			
			ptmt = conn.prepareStatement( sql );															
			ptmt.setString(1, objetoModelo.getNombres());
			ptmt.setString(2, objetoModelo.getApellidos());
			ptmt.setString(3, objetoModelo.getNombrePersonalizado());
			ptmt.setString(4, objetoModelo.getDireccion());
			ptmt.setString(5, objetoModelo.getTelefono());
			ptmt.setString(6, objetoModelo.getCelular());
			ptmt.setString(7, objetoModelo.getCorreo());
			ptmt.setString(8, objetoModelo.getCodigo());
			
			if (ptmt.executeUpdate() != 0)
				estado = true;


		}
		catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			this.cerrarConexion("actualizar(" + objetoModelo.getNombres() + ")");
		}
		return estado;
	}
	
	/**
	 * eliminar
	 */
	@Override
	public boolean eliminar( String codigos )
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("eliminar(" + codigos + ")");
			
			sql = "UPDATE contacto SET fecha_eliminacion = now() WHERE codigo IN(" + codigos + ")";
			
			ptmt = conn.prepareStatement( sql );																		
			
			if (ptmt.executeUpdate() != 0)
				estado = true;

		}
		catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			this.cerrarConexion("eliminar(" + codigos + ")");
		}
		
		return estado;
	}
	
	/**
	 * consultar
	 */
	@Override
	public Contacto consultar( int codigo ) 
	{
		Contacto contacto = null;
		try {
			conn = this.abrirConexion("consultar(" + codigo + ")");
			stmt = conn.createStatement();	
			
			sql = "SELECT * FROM contacto WHERE codigo = " + codigo;	
			
			System.out.println( sql );
			
			rs = stmt.executeQuery( sql );
			while (rs.next()) {				
				contacto = new Contacto();
				
				contacto = this.construirObjeto(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("consultar(" + codigo + ")");
		}
		
		return contacto;
	}	
	
	/**
	 * consultar por codigos
	 */
	@Override
	public ArrayList<Contacto> consultar( String codigos )
	{
		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		String sqlUser = " codigo_usuario = " + codigo_usuario;
		ArrayList<Contacto> listaContactos = new ArrayList<Contacto>();
		try {
			conn = this.abrirConexion("consultar(" + codigos + ")");
			stmt = conn.createStatement();		

			if (codigos == null)
				sql = "SELECT * FROM contacto WHERE true ";			
			else
				sql = "SELECT * FROM contacto WHERE codigo IN( " + codigos + ") ";				
			
			if (!Util.esCampoNulo(codigo_usuario))
				sql += " AND " + sqlUser;

			
			System.out.println( sql );
			
			rs = stmt.executeQuery( sql );
			
			Contacto contacto = null;
			
			while (rs.next()) {				
				contacto = new Contacto();				
				contacto = this.construirObjeto(rs);							
				listaContactos.add( contacto );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("consultar(" + codigos + ")");
		}
		
		return listaContactos;
	}
	
	/**
	 * Consulta todos los contactos y los filtra por query.
	 */
	public ArrayList<Contacto> consultarQuery( String query )
	{
		return super.consultarQuery( query );
	}
	
	/**
	 * Construye un Contacto a partir de un ResultSet
	 */
	public Contacto construirObjeto( ResultSet rs ) throws SQLException
	{
		Contacto contacto = new Contacto(); 
		contacto.setNombres( rs.getString("nombres") );
		contacto.setApellidos( rs.getString("apellidos") );
		contacto.setNombrePersonalizado( rs.getString("nombre_personalizado") );
		contacto.setDireccion( rs.getString("direccion") );
		contacto.setTelefono( rs.getString("telefono") );
		contacto.setCelular( rs.getString("celular") );
		contacto.setCorreo( rs.getString("correo") );
		contacto.setCodigo( rs.getString("codigo") );
		contacto.setFechaCreacion( rs.getTimestamp("fecha_creacion") );
		contacto.setFechaActualizacion( rs.getTimestamp("fecha_creacion") );
		contacto.setFechaEliminacion( rs.getTimestamp("fecha_eliminacion") );	
		return contacto;
	}
	
	/**
	 * Verifica si un nombre personalizado ya existe en la tabla.
	 * @param nombre_personalizado
	 * @return
	 */
	public boolean nombrePersonalizadoExiste( String nombre_personalizado )
	{
		Contacto contacto = new Contacto();
		boolean existe = false;
		try {
			conn = this.abrirConexion("nombrePersonalizadoExiste(" + nombre_personalizado + ")");
			stmt = conn.createStatement();		
			sql = "SELECT * FROM contacto WHERE nombre_personalizado LIKE '" + nombre_personalizado + "'";																					
			System.out.println( sql );
			rs = stmt.executeQuery( sql );
			while (rs.next()) {
				existe = true;			
				contacto = this.construirObjeto(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("nombrePersonalizadoExiste(" + nombre_personalizado + ")");
		}
		
		return existe;
	}
	
	/**
	 * Verifica si un nombre personalizado ya existe en la base de datos a partir de un objeto Contacto.
	 * @param cnt
	 * @return
	 */
	public boolean nombrePersonalizadoExiste( Contacto cnt )
	{
		boolean existe = false;
		try {
			conn = this.abrirConexion("nombrePersonalizadoExiste(" + cnt.getNombres() + ")");
					
			sql = "SELECT * FROM contacto WHERE nombre_personalizado LIKE ?";	
			if (!Util.esCampoNulo( cnt.getCodigo() )) {
				sql = sql + " AND codigo != ?"; 			
			}
			ptmt = conn.prepareStatement( sql );
			
			ptmt.setString( 1, cnt.getNombrePersonalizado() );
			
			try {
				ptmt.setInt( 2, Integer.parseInt( cnt.getCodigo() ));
			}
			catch (NumberFormatException e) {
				existe = false;
			}
			
			System.out.println( ptmt );
			
			rs = ptmt.executeQuery();
			
			while (rs.next()) {
				existe = true;			
				System.out.println("nombre personalizado para contacto existe.." + sql);
				//contacto = this.construirObjeto(rs);
				break;
			}

		}
		
		
		catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("nombrePersonalizadoExiste(" + cnt.getNombres() + ")");
		}
		
		return existe;
	}
	
	/**
	 * Convierte una lista de contactos en un mapa <String,Object>.
	 */
	public ArrayList<Map<String,Object>> convertirObjetosEnMapa( ArrayList<Contacto> objetos )
	{
		return super.convertirObjetosEnMapa( objetos );
	}	

}
