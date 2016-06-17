package Dao;

import java.util.*;

import Modelo.Actividad;
import Modelo.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.sql.*;

/**
 * Define un objeto DAO, todos los objetos de esta capa deben heredar de ObjetoDAO 
 * y definir los metodos insertar, actualizar, eliminar, consultar y construirObjeto
 * @author CRISHK
 *
 * @param <T>
 */

public abstract class ObjetoDAO<T> {
	
	/**
	 * Esta variable nos permite saber a que profundidad se encuentra el uso de la conexion
	 * a la base de datos. Para cerrarla el valor de esta variable debe ser 0.
	 */
	public static int conteoConexion = 0;
	
	public static Connection conn = null;			// Conexión a la base de datos
	protected Statement stmt = null;			// Query
	protected PreparedStatement ptmt = null;	// Query con parametros
	protected String sql = null;				// sql que sera ejecutado en las consultas
	protected ResultSet rs = null;				// resultados de la consulta
	public static HashMap<String, Object> parametros = new HashMap<String,Object>();	// Parametros DAO
	
	
	/**
	 * Constructor.
	 */
	public ObjetoDAO()
	{
		//this.abrirConexion();
	}
	
	/**
	 * Establece un parametro DAO a enviar a los metodos durante las consultas.
	 * @param nombre
	 * @param object
	 */
	public void setParametro( String nombre, Object object )
	{
		parametros.put( nombre, object ); 
	}
	
	/**
	 * Obtiene un parametro DAO para realizar las consultas.
	 * @param nombre
	 * @return
	 */
	public Object getParametro( String nombre )
	{
		return parametros.get(nombre);
	}
	
	/**
	 * Abre la conexión a la base de datos.
	 * @return
	 */
	public Connection abrirConexion( String caller )
	{		
		++conteoConexion;
		System.out.println("#######" + caller + "### Abriendo conexión " + conteoConexion );
		try {
			if (conn == null) {
				conn = DatabaseConnection.getDBConnection();				
			}
			else if (conn.isClosed()) {
				conn = DatabaseConnection.getDBConnection();
			}
		}
		catch (SQLException e) {}
		return conn;
	}
	
	/**
	 * Cierra la conexión.
	 */
	public void cerrarConexion( String caller )
	{
		--conteoConexion;
		System.out.println( "#######" + caller + "### Cerrando conexión " + conteoConexion );
		if (conteoConexion == 0) {			
			try {
				if (rs != null)
					rs.close();
				if (stmt != null)
					stmt.close();
				if (ptmt != null)
					ptmt.close();			
				if (conn != null)
					conn.close();		
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}
	
	/**
	 * Inserta un objeto en la base de datos.
	 * @param objetoModelo
	 * @throws SQLException
	 */
	public abstract boolean insertar( T objetoModelo );
	
	/**
	 * Eliminar una serie de objetos en base a una lista de codigos separada por comas.
	 * Ejemplo: 1,2,4,7,5 
	 * @param codigos
	 * @throws SQLException
	 */
	public abstract boolean eliminar(  String codigos ) ;
	
	/**
	 * En base al codigo del objetoModelo actualiza los atributos del objeto en la base de datos.
	 * @param objetoModelo
	 * @throws SQLException
	 */
	public abstract boolean actualizar(  T objetoModelo ) ;
	
	
	/**
	 * Cada ObjetoDAO debe proporcionar esta funcion que recibiendo un ResultSet devuelve un objeto
	 * con las propiedades directamente desde la tabla. De esta manera evitamos estar duplicando el mismo codigo en las diferentes consultas.
	 * @param rs
	 * @return
	 */
	public abstract T construirObjeto( ResultSet rs ) throws SQLException;
	
	/**
	 * Cada ObjetoDAO debe proporcionar esta funcion que recibiendo un ResultSet devuelve una lista con 
	 * los objetos devueltos por una consulta. Esta funcion deberia usar siempre la funcion construirObjeto (ver arriba.)
	 * @param rs
	 * @return
	 */
	public ArrayList<T> construirLista( ResultSet rs ) throws SQLException
	{
		ArrayList<T> listaObjetos = new ArrayList<T>();
		while (rs.next()) {
			T objeto = this.construirObjeto( rs );
			listaObjetos.add( objeto );
		}
		return listaObjetos;
	}
	
	
	/**
	 * Devuelve un objeto con los datos a partir de su código.
	 * @param objetoModelo
	 * @return
	 * @throws SQLException
	 */
	public abstract T consultar( int codigo ) ;
	

	/**
	 * Devuelve una lista con las actividades especificadas en los codigos.
	 * @param codigos
	 * @return
	 */
	
	public abstract ArrayList<T> consultar( String codigos );
	

	
	/**
	 * Convertir los objetos en mapa.
	 * @param objetos
	 * @return
	 */
	public ArrayList<Map<String,Object>> convertirObjetosEnMapa( ArrayList<T> objetos )
	{
		ArrayList<Map<String,Object>> lista = new ArrayList<Map<String,Object>>();
		try {			
			for (Object objeto : objetos) {
				lista.add( Util.ConvertObjectToMap( objeto ));
			}
		}
		catch (NullPointerException e) {}
		catch (InvocationTargetException e) {}
		catch (IllegalAccessException e) {}
		return lista;
	}
	
	/**
	 * Por defecto el nombre de la tabla es el nombre de la clase T en cuestión. 
	 * @return
	 */
	public String getTableName() {
		String table = ((Class<T>)((ParameterizedType)getClass().getGenericSuperclass()).getActualTypeArguments()[0]).toString();		
		return table.toLowerCase().replaceAll("class modelo.", "" ); 
	}
	
	/**
	 * Realiza una consulta de todos los elementos de la tabla en general, el parametro query sirve para realizar busquedas en los
	 * elementos de la tabla.
	 * @param query
	 * @return
	 */
	public ArrayList<T> consultarQuery( String query )	
	{
		ArrayList<T> listaT = new ArrayList<T>();
		
		String codigo_usuario = (String)this.getParametro("codigo_usuario");
		String sqlUser = " codigo_usuario = " + codigo_usuario;
				
		String columnasClausulaWhere = new String();
		
		// Si query no es nulo, se consultan las columnas de la tabla
		// y se agrega a la consulta la clausulaWhere que permite filtrar las filas de la tabla
		// así la clausula where quedaria:
		// 
		// OR columna1 LIKE '%query%'
		// OR columna2 LIKE '%query%'
		// OR columna3 LIKE '%query%'
		// ...
		
		if (!Util.esCampoNulo(query)) {		
			try {
				// abre la conexión
				conn = this.abrirConexion("consultarQuery( " + query + ")");
				stmt = conn.createStatement();
				
				ResultSet rs = stmt.executeQuery("SELECT * FROM " + this.getTableName());				
				ResultSetMetaData rsmd = rs.getMetaData();			
				
				for (int i = 1; i <= rsmd.getColumnCount(); i++) {	
					String name = rsmd.getColumnName(i);
					
					// columna(i) LIKE '%query%'
			        columnasClausulaWhere += " OR " + name + " LIKE '%" + query + "%'";
				}				
				// Cerramos la clausula where creada anteriormente con
				// AND (false ... columnasClausulaWhere ... )
				columnasClausulaWhere = " AND (false " + columnasClausulaWhere + ")";
			} catch (SQLException e) { e.printStackTrace();}
			
			finally {
				this.cerrarConexion("consultarQuery( " + query + ")");
			}
		}
		
		try {
			conn = this.abrirConexion("consultarQuery( " + query + ")");
			stmt = conn.createStatement();
			
     		// Creamos la consula final con el filtro			
			sql = "SELECT * FROM " + this.getTableName()  + " WHERE fecha_eliminacion IS NULL  " + columnasClausulaWhere;										
			
			if (!Util.esCampoNulo(codigo_usuario))
				sql += " AND " + sqlUser;
			
			System.out.println( "Ejecutando " + sql );			
			rs = stmt.executeQuery( sql );
			
			T objeto = null;
			
			while (rs.next()) {
				objeto = this.construirObjeto(rs);		
				listaT.add( objeto );
			}
		
		} catch (SQLException e) { e.printStackTrace();}
		
		finally {
			this.cerrarConexion("consultarQuery( " + query + ")");
		}
		return listaT;
	}

}
