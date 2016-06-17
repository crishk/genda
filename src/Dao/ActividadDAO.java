package Dao;

import java.util.*;
import Modelo.Actividad;
import Modelo.Contacto;
import Modelo.Usuario;
import Modelo.Util;

import java.sql.*;

/**
 * 
 * java.util.Date dt = new java.util.Date();

java.text.SimpleDateFormat sdf = 
     new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

String currentTime = sdf.format(dt);
 * DAO
 * @author CRISHK
 *
 */

public class ActividadDAO extends ObjetoDAO<Actividad> {
	
	/**
	 * Constructor.
	 */
	public ActividadDAO()
	{ 
	}
	
	/**
	 * Inserta una actividad.
	 */
	@Override
	public  boolean insertar( Actividad objetoModelo )
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion( "insertar(" + objetoModelo.getNombre() + ")" );
			
			sql = "INSERT INTO actividad(nombre,descripcion, tipo_actividad, inicio, fin,fecha_creacion,fecha_actualizacion,codigo_usuario)"
					+ "VALUES (?,?,?,?,?,now(),now(),?)";
			
			ptmt = conn.prepareStatement( sql );															
			ptmt.setString(1, objetoModelo.getNombre());
			ptmt.setString(2, objetoModelo.getDescripcion());
			ptmt.setString(3, objetoModelo.getTipoActividad());
			ptmt.setTimestamp(4, objetoModelo.getInicio() );
			ptmt.setTimestamp(5, objetoModelo.getFin() );
			ptmt.setInt(6, Integer.parseInt( objetoModelo.getUsuario().getCodigo() ) );
			
			System.out.println( ptmt );
			
			if (ptmt.executeUpdate() != 0)
				estado = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			this.cerrarConexion("insertar(" + objetoModelo.getNombre() + ")");
		}	
		
		return estado;
	}
	
	/**
	 * Actualiza una actividad.
	 */
	@Override
	public  boolean actualizar(  Actividad objetoModelo )
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("actualizar(" + objetoModelo.getNombre() + ")");
			
			sql = "UPDATE actividad SET nombre = ?, descripcion = ?, tipo_actividad = ?, inicio = ?, fin = ?, fecha_actualizacion = now()"
					+ " WHERE codigo = ?";
			
			ptmt = conn.prepareStatement( sql );															
			ptmt.setString(1, objetoModelo.getNombre());
			ptmt.setString(2, objetoModelo.getDescripcion());
			ptmt.setString(3, objetoModelo.getTipoActividad());
			ptmt.setTimestamp(4, objetoModelo.getInicio());
			ptmt.setTimestamp(5, objetoModelo.getFin());			
			ptmt.setString(6, objetoModelo.getCodigo());
			
			if (ptmt.executeUpdate() != 0)
				estado = true;
		}
		catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			this.cerrarConexion("insertar(" + objetoModelo.getNombre() + ")");
		}
		return estado;
	}
	
	/**
	 * Elimina una actividad.
	 */
	@Override
	public  boolean eliminar( String codigos )
	{
		boolean estado = false;
		try {
			conn = this.abrirConexion("eliminar(" + codigos + ")");
			
			sql = "UPDATE actividad SET fecha_eliminacion = now() WHERE codigo IN(" + codigos + ")";
			System.out.println( sql );
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
	 * Consulta una actividad por codigo.
	 */
	@Override
	public Actividad consultar( int codigo )
	{
		
		Actividad actividad = null;

		try {
			this.abrirConexion("consultar(" + codigo + ")");
			stmt = conn.createStatement();		
			sql = "SELECT * FROM actividad WHERE codigo = " + codigo;																					
			
			rs = stmt.executeQuery( sql );
			
			while (rs.next()) {				
				actividad = new Actividad();
				actividad = this.construirActividadSinNotas(rs);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("consultar(" + codigo + ")");
		}
		
		return actividad;
	}	
	

	
	/**
	 * Consulta las actividades hasta hoy incluidas las anteriores a hoy.
	 * @return
	 */
	public ArrayList<Actividad> consultarActividadesHastaHoy()
	{
		return null;
	}
	
	/**
	 * Consulta las actividades resumen entre una fecha de inicio y una fecha de finalización.
	 * 
	 *  
	 * @return
	 */
	public ArrayList<ArrayList<Actividad>> consultarActividadesResumen( Timestamp inicio, Timestamp fin )
	{
		ArrayList<ArrayList<Actividad>> actividades = new ArrayList<ArrayList<Actividad>>();
		String codigo_usuario = this.getParametro("codigo_usuario").toString();
		System.out.println( "Codigo del usuario " + codigo_usuario );

		String sqlUser = " codigo_usuario = " + codigo_usuario;
		
		ArrayList<Actividad> actividadesRealizadas = new ArrayList<Actividad>();
		ArrayList<Actividad> actividadesActivas = new ArrayList<Actividad>();
		ArrayList<Actividad> actividadesPendientes = new ArrayList<Actividad>();
		
		try {
			conn = this.abrirConexion("consultarActividadesResumen(" + inicio + "," + fin + ")");
			
			sql = "SELECT * , "
					+ "IF ((inicio <= now() AND fin <= now()), 'realizada', "
					+ "IF (inicio <= now() AND fin >= now(), 'activa','pendiente')) AS 'estado_actividad' "
					+ "FROM actividad "
					+ "WHERE codigo_usuario = " + codigo_usuario + " AND fecha_eliminacion IS NULL ";
					

			// La consulta especifica fecha de inicio y de finalizacion
			if (inicio != null && fin != null) {
				sql = sql + "AND (inicio >= ? AND fin <= ?) ";
			}
			
			else
			if (inicio != null && fin == null) {	// La consulta especifica fecha de inicio pero no de finalizacion
				sql = sql + "AND inicio >= ? ";
			}
			else
			if (inicio == null && fin != null) {	// La consulta especifica fecha de finalizacion pero no de inicio
				sql = sql + "AND fin <= ? ";
			}
			
			// Se ordena por fecha de inicio y fin en orden ascendente
			sql = sql +  " ORDER BY inicio ASC, fin ASC";

			ptmt = conn.prepareStatement( sql );	
			
			
			// Se agregan los parametros segun las fechas indicadas.
			if (inicio != null && fin != null) {
				ptmt.setTimestamp(1, inicio);
				ptmt.setTimestamp(2, fin );
			}
			else
			if (inicio != null && fin == null) {
				ptmt.setTimestamp(1, inicio);	
			}
			else
			if (inicio == null && fin != null) {			
				ptmt.setTimestamp(1, fin );
			}

			System.out.println( ptmt );
			
			rs = ptmt.executeQuery();

			// Se llena la lista con las actividades obtenidas
			while (rs.next()) {
				Actividad actividad = new Actividad();
				actividad = this.construirObjeto(rs);
				
				// Se llenan la notas de la actividad consultandolas a traves de NotaDAO.
				NotaDAO notaDAO = new NotaDAO();
				actividad.setNotas( 
						notaDAO.consultarPorActividad( 
						Integer.parseInt( actividad.getCodigo() ) ) );
								
				String estadoActividad = rs.getString("estado_actividad");
				
				// Se asigna la actividad correspondiente a la lista indicada basandose en el estado de la actividad.
				switch (estadoActividad) {
				case "activa":
					actividadesActivas.add( actividad );
					break;
				case "realizada":
					actividadesRealizadas.add( actividad );
					break;
				case "pendiente":
					actividadesPendientes.add(actividad);
					break;
				}			
			} 
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion("consultarActividadesResumen(" + inicio + "," + fin + ")");
		}
		
		// Se agregan las listas a la lista general.
		actividades.add( actividadesRealizadas );		// Actividades realizadas en posición 0
		actividades.add( actividadesActivas );			// Actividades activas en posición 0
		actividades.add( actividadesPendientes );		// Actividades pendientes en posición 0
				
		return actividades;

	}
	
	/**
	 * Consulta las actividades activas.
	 * El valor que devuelve el metodo consultarActividadesResumen siempre retorna 
	 * las actividades activas en la posición 1 del la lista.
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public ArrayList<Actividad> consultarActividadesActivas(  Timestamp inicio, Timestamp fin )
	{
		ArrayList<ArrayList<Actividad>> actividades = this.consultarActividadesResumen( inicio, fin );
		return actividades.get(1);
	}

	/**
	 * Consulta las actividades realizadas.
	 * El valor que devuelve el metodo consultarActividadesResumen siempre retorna 
	 * las actividades realizadas en la posición 0 del la lista.
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public ArrayList<Actividad> consultarActividadesRealizadas(  Timestamp inicio, Timestamp fin )
	{
		ArrayList<ArrayList<Actividad>> actividades = this.consultarActividadesResumen( inicio, fin );
		return actividades.get(0);
	}
	
	/**
	 * Consulta las actividades pendientes.
	 * El valor que devuelve el metodo consultarActividadesPendientes siempre retorna 
	 * las actividades pendientes en la posición 2 del la lista.
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public ArrayList<Actividad> consultarActividadesPendientes(  Timestamp inicio, Timestamp fin )
	{
		ArrayList<ArrayList<Actividad>> actividades = this.consultarActividadesResumen( inicio, fin );
		return actividades.get(2);	
	}
	
	
	/**
	 * Realiza una consulta de todas las actividades verificando si el valor query esta presente
	 * en una de sus columnas LIKE %query%, si query es null devuelve todas las actividades.
	 */
	public ArrayList<Actividad> consultarQuery( String query )	
	{
		return super.consultarQuery(query);
	}

	/**
	 * Devuelve una lista de todas las actividades que tengan el codigo presente en el string separado
	 * por comas "codigos".
	 */
	@Override
	public ArrayList<Actividad> consultar( String codigos )
	{
		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		String sqlUser = " codigo_usuario = " + codigo_usuario;
		
		ArrayList<Actividad> listaActividades = new ArrayList<Actividad>();
		
		try {
			conn = this.abrirConexion( "consultar(" + codigos + ")" );
			stmt = conn.createStatement();	
			
			if (codigos == null)
				sql = "SELECT * FROM actividad WHERE true ";			
			else
				sql = "SELECT * FROM actividad WHERE codigo IN( " + codigos + ") ";				
			
			if (!Util.esCampoNulo(codigo_usuario))
				sql += " AND " + sqlUser;

			rs = stmt.executeQuery( sql );
			
			Actividad actividad = null;
			
			while (rs.next()) {
				actividad = new Actividad();
				
				System.out.println( "Agregando actividad " + rs.getString("nombre"));
				actividad = this.construirObjeto(rs);		
				
				
				NotaDAO notaDAO = new NotaDAO();
				actividad.setNotas( 
						notaDAO.consultarPorActividad( 
						Integer.parseInt( actividad.getCodigo() ) ) );
				
				listaActividades.add( actividad );
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		finally {
			this.cerrarConexion(  "consultar(" + codigos + ")"  );
		}
		
		return listaActividades;
	}	
	
	/**
	 * Construye una actividad a partir de un ResultSet sin rellenar las notas.
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	public Actividad construirActividadSinNotas( ResultSet rs ) throws SQLException 
	{
		Actividad nuevaActividad = new Actividad();
		nuevaActividad.setCodigo( rs.getString("codigo") );
		//System.out.println( "Agregando actividad " + rs.getString("nombre") + " Estado: " + rs.getString("estado_actividad"));
		nuevaActividad.setNombre( rs.getString("nombre") );
		nuevaActividad.setDescripcion( rs.getString("descripcion") );
		nuevaActividad.setTipoActividad( rs.getString("tipo_actividad") );			
		nuevaActividad.setInicio( (rs.getTimestamp("inicio")) );
		nuevaActividad.setFin( (rs.getTimestamp("fin")) );		
		nuevaActividad.setFechaCreacion( rs.getTimestamp("fecha_creacion") );
		nuevaActividad.setFechaActualizacion( rs.getTimestamp("fecha_actualizacion") );
		nuevaActividad.setFechaEliminacion( rs.getTimestamp("fecha_eliminacion") );	
		
		return nuevaActividad;
	}
	
	/**
	 * Construye una actividad a partir de un ResultSet rellenando las notas.
	 */
	public Actividad construirObjeto( ResultSet rs ) throws SQLException
	{ 
		Actividad nuevaActividad = this.construirActividadSinNotas( rs );

		NotaDAO notaDAO = new NotaDAO();
		nuevaActividad.setNotas( 
				notaDAO.consultarPorActividad( 
				Integer.parseInt( nuevaActividad.getCodigo() ) ) );
		
		return nuevaActividad;
	}
	
	/**
	 * El intentar ingresar una actividad de cualquier tipo no puede ubicarse o ingresarse
	 * si existe tan solo una actividad en ese mismo periodo de tiempo (inicio, fin) y que sea restringida.
	 * Lo normal es que este metodo sea solo usado para verificar actividades abiertas.
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public boolean actividadAbiertaGeneraConflicto( Actividad actividad )
	{
		/**
		 * public void rangoGeneraConflicto( int aI, int aF, int ptmt.setTimestamp(1, actividad.getInicio());, int ptmt.setTimestamp(2, actividad.getFin()); ) 
{

1. Si (ptmt.setTimestamp(1, actividad.getInicio()); < aI && ptmt.setTimestamp(2, actividad.getFin()); > aI && ptmt.setTimestamp(2, actividad.getFin()); < aF && ptmt.setTimestamp(1, actividad.getInicio()); < aF && aF > aI && ptmt.setTimestamp(1, actividad.getInicio()); > ptmt.setTimestamp(2, actividad.getFin());) Entonces
     return true;

2. Si (ptmt.setTimestamp(1, actividad.getInicio()); > aI && ptmt.setTimestamp(2, actividad.getFin()); > aI && ptmt.setTimestamp(2, actividad.getFin()); < aF && ptmt.setTimestamp(1, actividad.getInicio()); < aF && aF > aI && ptmt.setTimestamp(1, actividad.getInicio()); > ptmt.setTimestamp(2, actividad.getFin());) Entonces
     return true;

3. Si (ptmt.setTimestamp(1, actividad.getInicio()); > aI && ptmt.setTimestamp(2, actividad.getFin()); > aI && ptmt.setTimestamp(2, actividad.getFin()); > aF && ptmt.setTimestamp(1, actividad.getInicio()); > aI && aF > aI && ptmt.setTimestamp(1, actividad.getInicio()); > ptmt.setTimestamp(2, actividad.getFin());) Entonces
     return true;

4. Si (ptmt.setTimestamp(1, actividad.getInicio()); = aI && ptmt.setTimestamp(2, actividad.getFin()); = aF)
     return true;

}

		 */
		
		
		boolean generaConflicto = false;

		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		String sqlUser = " codigo_usuario = " + codigo_usuario;
		
		try {
			conn = this.abrirConexion("actividadAbiertaGeneraConflicto(" + actividad.getNombre() + ")");

			sql = "SELECT * FROM actividad "
					+ "WHERE fecha_eliminacion IS NULL "
					+ "AND (	(? <= inicio AND ? <= ? AND ? <= fin AND inicio <= ? AND inicio <= fin AND ? <= fin AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (inicio <= ? AND inicio <= ? AND	inicio <= fin AND ? <= ? AND ? <= fin AND ? <= fin AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (inicio <= ? AND inicio <= ? AND inicio <= fin AND ? <= ? AND fin <= ? AND ? <= fin AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (? <= inicio AND ? <= ? AND ? <= fin AND inicio <= fin AND inicio <= ? AND fin <= ? AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (inicio = ? AND fin = ? AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin))) )	"
					+ "AND codigo_usuario = ? "
					+ "AND tipo_actividad LIKE 'restringida' ";
			
			if (!Util.esCampoNulo(actividad.getCodigo()))
				sql = sql + " AND codigo != ?";
						
			ptmt = conn.prepareStatement( sql );	
			
			ptmt.setTimestamp(1, actividad.getInicio());
			ptmt.setTimestamp(2, actividad.getInicio());
			ptmt.setTimestamp(3, actividad.getFin());
			ptmt.setTimestamp(4, actividad.getInicio());
			ptmt.setTimestamp(5, actividad.getFin());
			ptmt.setTimestamp(6, actividad.getFin());
			ptmt.setTimestamp(7, actividad.getFin());
			ptmt.setTimestamp(8, actividad.getInicio());
			ptmt.setTimestamp(9, actividad.getInicio());
			ptmt.setTimestamp(10, actividad.getFin());	 
			ptmt.setTimestamp(11, actividad.getInicio());
			ptmt.setTimestamp(12, actividad.getFin());
			ptmt.setTimestamp(13, actividad.getInicio());
			ptmt.setTimestamp(14, actividad.getFin());
			ptmt.setTimestamp(15, actividad.getInicio());
			ptmt.setTimestamp(16, actividad.getFin());
			ptmt.setTimestamp(17, actividad.getFin());
			ptmt.setTimestamp(18, actividad.getInicio());
			ptmt.setTimestamp(19, actividad.getInicio());
			ptmt.setTimestamp(20, actividad.getFin());	 
			ptmt.setTimestamp(21, actividad.getInicio());
			ptmt.setTimestamp(22, actividad.getFin());
			ptmt.setTimestamp(23, actividad.getInicio());
			ptmt.setTimestamp(24, actividad.getFin());
			ptmt.setTimestamp(25, actividad.getFin());
			ptmt.setTimestamp(26, actividad.getInicio());
			ptmt.setTimestamp(27, actividad.getFin());
			ptmt.setTimestamp(28, actividad.getInicio());
			ptmt.setTimestamp(29, actividad.getInicio());
			ptmt.setTimestamp(30, actividad.getFin());	 
			ptmt.setTimestamp(31, actividad.getInicio());
			ptmt.setTimestamp(32, actividad.getInicio());
			ptmt.setTimestamp(33, actividad.getFin());
			ptmt.setTimestamp(34, actividad.getInicio());
			ptmt.setTimestamp(35, actividad.getFin());
			ptmt.setTimestamp(36, actividad.getFin());
			ptmt.setTimestamp(37, actividad.getFin());
			ptmt.setTimestamp(38, actividad.getInicio());
			ptmt.setTimestamp(39, actividad.getInicio());
			ptmt.setTimestamp(40, actividad.getFin());	 
			ptmt.setTimestamp(41, actividad.getInicio());
			ptmt.setTimestamp(42, actividad.getFin());
			ptmt.setTimestamp(43, actividad.getFin());
			ptmt.setTimestamp(44, actividad.getInicio());
			ptmt.setTimestamp(45, actividad.getInicio());
			ptmt.setTimestamp(46, actividad.getFin());	
			ptmt.setInt(47, Integer.parseInt( codigo_usuario ));
									
			
			if (!Util.esCampoNulo(actividad.getCodigo()))
				ptmt.setInt(4, Integer.parseInt( actividad.getCodigo() ));
			
			System.out.println( ptmt );
			
			rs = ptmt.executeQuery();

			while (rs.next()) {
				System.out.println("Genera conflicto...");
				generaConflicto = true;
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		finally {
			this.cerrarConexion("actividadAbiertaGeneraConflicto(" + actividad.getNombre() + ")");
		}
		
		return generaConflicto;
	}

	/**
	 * Caso contrario al del metodo anterior (actividadAbiertaGeneraConflicto), este comprueba que una 
	 * actividad cualquiera (normalmente debe ser una actividad restringida) pueda ser ingresada a 
	 * menos que exista tan solo una actividad de cualquier tipo en el rango de tiempo especificado.
	 *  
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public boolean actividadRestringidaGeneraConflicto( Actividad actividad )
	{
		boolean generaConflicto = false;

		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		String sqlUser = " codigo_usuario = " + codigo_usuario;

		
		try {
			conn = this.abrirConexion("actividadAbiertaGeneraConflicto(" + actividad.getNombre() + ")");
				
			
			sql = "SELECT * FROM actividad "
					+ "WHERE fecha_eliminacion IS NULL "
					+ "AND (	(? <= inicio AND ? <= ? AND ? <= fin AND inicio <= ? AND inicio <= fin AND ? <= fin AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (inicio <= ? AND inicio <= ? AND	inicio <= fin AND ? <= ? AND ? <= fin AND ? <= fin AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (inicio <= ? AND inicio <= ? AND inicio <= fin AND ? <= ? AND fin <= ? AND ? <= fin AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (? <= inicio AND ? <= ? AND ? <= fin AND inicio <= fin AND inicio <= ? AND fin <= ? AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin)))	"
					+ "OR (inicio = ? AND fin = ? AND !((inicio = ? AND ? < inicio) OR (fin = ? AND ? > fin))) )	"
					+ "AND codigo_usuario = ? "	;
					
			
			if (!Util.esCampoNulo( actividad.getCodigo() )) {
				sql = sql + " AND codigo != ?";
			}
	
			ptmt = conn.prepareStatement( sql );				
			
			
			ptmt.setTimestamp(1, actividad.getInicio());
			ptmt.setTimestamp(2, actividad.getInicio());
			ptmt.setTimestamp(3, actividad.getFin());
			ptmt.setTimestamp(4, actividad.getInicio());
			ptmt.setTimestamp(5, actividad.getFin());
			ptmt.setTimestamp(6, actividad.getFin());
			ptmt.setTimestamp(7, actividad.getFin());
			ptmt.setTimestamp(8, actividad.getInicio());
			ptmt.setTimestamp(9, actividad.getInicio());
			ptmt.setTimestamp(10, actividad.getFin());	 
			ptmt.setTimestamp(11, actividad.getInicio());
			ptmt.setTimestamp(12, actividad.getFin());
			ptmt.setTimestamp(13, actividad.getInicio());
			ptmt.setTimestamp(14, actividad.getFin());
			ptmt.setTimestamp(15, actividad.getInicio());
			ptmt.setTimestamp(16, actividad.getFin());
			ptmt.setTimestamp(17, actividad.getFin());
			ptmt.setTimestamp(18, actividad.getInicio());
			ptmt.setTimestamp(19, actividad.getInicio());
			ptmt.setTimestamp(20, actividad.getFin());	 
			ptmt.setTimestamp(21, actividad.getInicio());
			ptmt.setTimestamp(22, actividad.getFin());
			ptmt.setTimestamp(23, actividad.getInicio());
			ptmt.setTimestamp(24, actividad.getFin());
			ptmt.setTimestamp(25, actividad.getFin());
			ptmt.setTimestamp(26, actividad.getInicio());
			ptmt.setTimestamp(27, actividad.getFin());
			ptmt.setTimestamp(28, actividad.getInicio());
			ptmt.setTimestamp(29, actividad.getInicio());
			ptmt.setTimestamp(30, actividad.getFin());	 
			ptmt.setTimestamp(31, actividad.getInicio());
			ptmt.setTimestamp(32, actividad.getInicio());
			ptmt.setTimestamp(33, actividad.getFin());
			ptmt.setTimestamp(34, actividad.getInicio());
			ptmt.setTimestamp(35, actividad.getFin());
			ptmt.setTimestamp(36, actividad.getFin());
			ptmt.setTimestamp(37, actividad.getFin());
			ptmt.setTimestamp(38, actividad.getInicio());
			ptmt.setTimestamp(39, actividad.getInicio());
			ptmt.setTimestamp(40, actividad.getFin());	 
			ptmt.setTimestamp(41, actividad.getInicio());
			ptmt.setTimestamp(42, actividad.getFin());
			ptmt.setTimestamp(43, actividad.getFin());
			ptmt.setTimestamp(44, actividad.getInicio());
			ptmt.setTimestamp(45, actividad.getInicio());
			ptmt.setTimestamp(46, actividad.getFin());	
			ptmt.setInt(47, Integer.parseInt( codigo_usuario ));
			
			System.out.println("SQL statement here: " + ptmt);
			if (!Util.esCampoNulo( actividad.getCodigo() )) {
				ptmt.setInt(4, Integer.parseInt( actividad.getCodigo() ));
			}
			
			System.out.println( ptmt );
			
			rs = ptmt.executeQuery();

			while (rs.next()) {
				System.out.println("Genera conflicto...");
				generaConflicto = true;
				break;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		
		finally {
			this.cerrarConexion("actividadAbiertaGeneraConflicto(" + actividad.getNombre() + ")");
		}
		
		return generaConflicto;
	}
	
	/**
	 * Convierte una lista de actividades en una lista de mapas de datos de actividades
	 * para que pueda ser indexado por el JSP de la forma:
	 * objeto.propiedad, por ejemplo si en JSP requerimos imprimir la lista basta con referirse al atributo 
	 * de la actividad sin necesidad de llamar al metodo get que devuelve el valor de esta:
	 * 
	 * Por ejemplo, para obtener el valor de la propiedad nombre, escribimos en el JSP:
	 * ${actividad.Nombre}
	 */
	public ArrayList<Map<String,Object>> convertirObjetosEnMapa( ArrayList<Actividad> objetos )
	{
		return super.convertirObjetosEnMapa( objetos );
	}	
	
}
