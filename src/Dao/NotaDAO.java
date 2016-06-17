package Dao;

import java.util.*;

import Modelo.Actividad;
import Modelo.Contacto;
import Modelo.Nota;
import Modelo.Usuario;
import Modelo.Util;

import java.sql.*;

/**
 * Clase DAO de acceso a datos de Notas.
 * @author CRISHK
 *
 */
public class NotaDAO extends ObjetoDAO<Nota> {
	/**
	 * Constructor.
	 */
	public NotaDAO()
	{
		
	}
	
	/**
	 * Inserta una nueva nota en la tabla Nota.
	 */
	@Override
	public  boolean insertar( Nota objetoModelo ) 
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("insertar(" + objetoModelo.getNota() + ")");
			
			sql = "INSERT INTO nota(nota,codigo_actividad,codigo_usuario,fecha_creacion,fecha_actualizacion)"
					+ "VALUES (?,?,?,now(),now())";
			
			ptmt = conn.prepareStatement( sql );
			
			ptmt.setString(1, objetoModelo.getNota());
			ptmt.setInt(2, Integer.parseInt( objetoModelo.getActividad().getCodigo() ) );
			ptmt.setInt(3, Integer.parseInt( objetoModelo.getUsuario().getCodigo() ) );


			System.out.println( ptmt );
			
			if (ptmt.executeUpdate() != 0)
				estado = true;
		}
		catch (SQLException e) {
			estado = false;
			e.printStackTrace();
		}  finally {
			
			this.cerrarConexion("insertar(" + objetoModelo.getNota() + ")");
			

		}	
		return estado;
	}
	
	/**
	 * Actualiza una nota.
	 */
	@Override
	public  boolean actualizar(  Nota objetoModelo )
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("actualizar(" + objetoModelo.getNota() + ")");
			
			sql = "UPDATE nota SET nota = ?, codigo_actividad = ?, fecha_actualizacion = now() WHERE codigo = ?";
			
			ptmt = conn.prepareStatement( sql );
			
			ptmt.setString(1, objetoModelo.getNota());
			ptmt.setInt(2, Integer.parseInt( objetoModelo.getActividad().getCodigo() ) );
			ptmt.setInt(3, Integer.parseInt( objetoModelo.getCodigo() ) );


			System.out.println( ptmt );
			
			if (ptmt.executeUpdate() != 0)
				estado = true;
		}
		catch (SQLException e) {
			estado = false;
			e.printStackTrace();
		}  finally {
			
			this.cerrarConexion("actualizar(" + objetoModelo.getNota() + ")");
			

		}	
		return estado;
	}
	
	/**
	 * Elimina todas las notas listadas en codigos (lista de codigos separados por comas)
	 */
	@Override
	public  boolean eliminar( String codigos )
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("eliminar(" + codigos + ")");
			
			sql = "UPDATE nota SET fecha_eliminacion = now() WHERE codigo IN(" + codigos + ")";
			System.out.println( sql );
			ptmt = conn.prepareStatement( sql );																		
			//ptmt.setString(1,  codigos );
			
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
	 * Consulta todas las notas y las filtra por query.
	 */
	public ArrayList<Nota> consultarQuery( String query )
	{
		return super.consultarQuery(query);
	}
	
	/**
	 * Consulta una nota basado en el código.
	 */
	@Override
	public Nota consultar( int codigo )
	{
		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		String sqlUser = " codigo_usuario = " + codigo_usuario;
		
		Nota nota = null;
		try {
			conn = this.abrirConexion("consultar(" + codigo + ")");
			stmt = conn.createStatement();		
			sql = "SELECT * FROM nota WHERE codigo = " + codigo;	
			
			if (codigo_usuario != null)
				sql += " AND " + sqlUser;
			
			System.out.println( "Consultar nota por codigo " + sql );
			rs = stmt.executeQuery( sql );
			
			while (rs.next()) {
				nota = new Nota();
				nota = this.construirObjeto(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("consultar(" + codigo + ")");
		}
		
		return nota;
	}	
	
	/**
	 * Consulta todas las notas especificadas por codigos en la variable codigos, 
	 * lista de codigos separados por comas.
	 */
	@Override
	public ArrayList<Nota> consultar( String codigos )
	{
		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		String sqlUser = " codigo_usuario = " + codigo_usuario;
		
		ArrayList<Nota> notas = new ArrayList<Nota>();
		try {
			conn = this.abrirConexion("consultar(" + codigos + ")");
			stmt = conn.createStatement();		
			
			if (codigos == null)
				sql = "SELECT * FROM nota WHERE true ";			
			else
				sql = "SELECT * FROM nota WHERE codigo IN( " + codigos + ") ";				
			
			if (!Util.esCampoNulo(codigo_usuario))
				sql += " AND " + sqlUser;
			
			System.out.println( sql );
			
			rs = stmt.executeQuery( sql );
			while (rs.next()) {
				Nota nota = new Nota();
				nota = this.construirObjeto(rs);				
				notas.add( nota );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("consultar(" + codigos + ")");
		}
		
		return notas;
	}		
	
	/**
	 * Consulta las notas que corresponden a un actividad especificada por el parametro codigo.
	 * Devuelve una lista de las notas.
	 * @param codigo
	 * @return
	 */
	public List<Nota> consultarPorActividad( int codigo )
	{
		ArrayList<Nota> notas = new ArrayList<Nota>();
		try {
			conn = this.abrirConexion("consultarPorActividad(" + codigo + ")");
			stmt = conn.createStatement();		
			sql = "SELECT * FROM nota WHERE fecha_eliminacion IS NULL and codigo_actividad = " + codigo;																					
			
			rs = stmt.executeQuery( sql );
			while (rs.next()) {				
				Nota nota = new Nota();
				nota = this.construirObjeto(rs);
				notas.add( nota );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("consultarPorActividad(" + codigo + ")");
		}
		
		return notas;
	}	
	
	/**
	 * Construye una Nota a partir de un ResultSet.
	 */
	public Nota construirObjeto( ResultSet rs ) throws SQLException
	{
		Nota nota = new Nota();
		nota.setCodigo( rs.getString("codigo" ) );
		nota.setNota( rs.getString("nota") );		
		
		
		Actividad actividad = new Actividad();
		actividad.setCodigo( rs.getString("codigo_actividad") );

		
		ActividadDAO actDAO = new ActividadDAO();
		actividad = actDAO.consultar( Integer.parseInt( rs.getString("codigo_actividad") ));
		
		nota.setDescripcionActividad( actividad.getNombre() );
		nota.setActividad(actividad);
		
		Usuario usuario = new Usuario();
		usuario.setCodigo( rs.getString("codigo_usuario") );
		
		UsuarioDAO usrDAO = new UsuarioDAO();
		usuario = usrDAO.consultar( Integer.parseInt( rs.getString("codigo_actividad") ));
		
		nota.setUsuario(usuario);
		
		nota.setFechaCreacion( rs.getTimestamp("fecha_creacion") );
		nota.setFechaActualizacion( rs.getTimestamp("fecha_actualizacion") );
		nota.setFechaEliminacion( rs.getTimestamp("fecha_eliminacion") );

		return nota;
	}
	
	/**
	 * Obtiene en el nombre de la tabla, en este caso particular para las notas
	 * se redefine el metodo para que devuelva el nombre de la vida y no el nombre de 
	 * la tabla, ya que se necesita el campo descripcion_actividad.
	 */
	public String getTableName()
	{
		return "vista_nota";
	}
	
	/**
	 * Convierte los objetos en mapas para poder acceder facilmente a sus valores desde JSP usando JSTL dinámico.
	 */
	public ArrayList<Map<String,Object>> convertirObjetosEnMapa( ArrayList<Nota> objetos )
	{
		return super.convertirObjetosEnMapa( objetos );
	}	
}