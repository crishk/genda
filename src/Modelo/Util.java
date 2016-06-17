package Modelo;
import java.io.*;
import java.lang.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.util.Date;
import java.util.regex.Pattern;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.net.*;

import org.apache.commons.lang3.time.DateUtils;
import org.json.*;

/**
 * Clase util donde alojamos la mayoria de metodos que nos permiten realizar operaciones comunes sobre los datos.
 * @author CRISHK
 *
 */
public class Util {
	
	/**
	 * Valida si el valor pasado es nulo.
	 * @param valor
	 * @return
	 */
	public static boolean esCampoNulo( String valor )
	{
		return valor != null && valor.trim().isEmpty() && valor.length() == 0;
	}
	
	/**
	 * Valida un nombre de usuario para las cuentas de usuario.
	 * @param nombre_usuario
	 * @return
	 */
	public static boolean validarNombreUsuario( String nombre_usuario )
	{
		if (nombre_usuario != null && !nombre_usuario.trim().isEmpty()) {
			Pattern p = Pattern.compile("^[a-zA-Z0-9]+$");
			if (p.matcher(nombre_usuario).find()) {
				return true;
			}
		}
		return false; 
		//return nombre_usuario != null && !nombre_usuario.trim().isEmpty();
	}
	
	/**
	 * Valida un password para las cuentas de usuario.
	 * @param password
	 * @return
	 */
	public static boolean validarPassword( String password )
	{
		
		return password != null && !password.trim().isEmpty();
	}
	
	/**
	 * Valida un numero de telefono.
	 * Solo debe contener caracteres ' 123456789()+-'
	 * @param telefono
	 * @return
	 */
	public static boolean validarTelefono( String telefono )
	{		
		if (telefono != null && !telefono.trim().isEmpty()) {
			Pattern p = Pattern.compile("^[(]{0,1}[0-9]{3}[)]{0,1}[-\\s\\.]{0,1}[0-9]{3}[-\\s\\.]{0,1}[0-9]{4}$");
			if (p.matcher(telefono).find()) {
				return true;
			}
		}
		return false; 
	}
	
	/**
	 * Valida los nombres debe ser diferente de nulo.
	 * @param nombres
	 * @return
	 */
	public static boolean validarNombres( String nombres )
	{
		
		return nombres != null && !nombres.trim().isEmpty();
	}
	
	/**
	 * Valida los apellidos debe ser diferente de nulo.
	 * @param apellidos
	 * @return
	 */
	public static boolean validarApellidos( String apellidos )
	{
		return apellidos != null && !apellidos.trim().isEmpty();
	}
	
	/**
	 * Valida una direccion de domicilio.
	 * @param direccion
	 * @return
	 */
	public static boolean validarDireccion( String direccion )
	{
		return direccion != null && !direccion.trim().isEmpty();
	}
	
	/**
	 * Valida un correo electronico.
	 * @param correo
	 * @return
	 */
	public static boolean validarCorreo( String correo )
	{
		//return correo != null && !correo.trim().isEmpty();
		if (correo != null && !correo.trim().isEmpty()) {
			Pattern p = Pattern.compile("^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$");
			if (p.matcher(correo).find()) {
				return true;
			}
		}
		return false; 
		
	}
	
	/**
	 * Valida un nombre personalizado.
	 * @param nombre_personalizado
	 * @return
	 */
	public static boolean validarNombrePersonalizado( String nombre_personalizado )
	{
		return nombre_personalizado != null && !nombre_personalizado.trim().isEmpty();
	}
	
	/**
	 * Valida el nombre de la actividad.
	 * @param nombre
	 * @return
	 */
	public static boolean validarNombreActividad( String nombre )
	{
		return nombre != null && !nombre.trim().isEmpty();
	}
	
	/**
	 * Valida la descripcion.
	 * @param descripcion
	 * @return
	 */
	public static boolean validarDescripcion( String descripcion )
	{
		return descripcion != null && !descripcion.trim().isEmpty();
	}
	
	/**
	 * Valida que el tipo de actividad sea abierta o restringida unicamente.
	 * @param tipoActividad
	 * @return
	 */
	public static boolean validarTipoActividad( String tipoActividad )
	{
		return tipoActividad.equalsIgnoreCase("abierta") || tipoActividad.equalsIgnoreCase("restringida"); 
	}
	
	/**
	 * Valida que una fecha sea valida.
	 * @param fecha
	 * @return
	 */
	public static boolean validarFecha( String fecha )
	{
		if (fecha == null) { System.out.println( "Fecha es nula.");  return false; }
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		    sdf.setLenient(false);
		    try {
		        sdf.parse(fecha);
		        return true;
		    } catch (ParseException ex) {
		        return false;
		    }
	}
	
	/**
	 * Valida que la nota sea valida.
	 * @param nota
	 * @return
	 */
	public static boolean validarNota( String nota )
	{
		return nota != null && !nota.trim().isEmpty();
	}
	
	/**
	 * Valida que la fecha de inicio sea menor a la fecha de finalización.
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public static boolean validarRangoFechas( Timestamp inicio, Timestamp fin )
	{
		if (inicio == null || fin == null) return false;
		return inicio.compareTo( fin ) < 0; 
	}

	/**
	 * getActualYear
	 * Obtiene el año actual.
	 * @return
	 */
	public static int getActualYear()
	{
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	/**
	 * jsonToMap
	 * Convierte un String JSON en un LinkedHashMap para ser usado en Java.
	 * @param t
	 * @return
	 * @throws JSONException
	 */	
	public static LinkedHashMap<String, String> jsonToMap(String t) throws JSONException {

		LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
        JSONObject jObject = new JSONObject(t);
        Iterator<?> keys = jObject.keys();

        while( keys.hasNext() ){
            String key = (String)keys.next();
            String value = jObject.getString(key); 
            map.put(key, value);

        }

        return map;
    }
	
	/**
	 * getRefererURI
	 * Obtiene el URI de la direccion del referer.
	 * @param refererURL
	 * @return
	 */
	public static String getRefererURI( String refererURL )
	{
		
		
		String uri = new String();
		try {
			URL url = new URL( refererURL );
			String queryStr = url.getQuery();
			uri = new URI(refererURL).getPath();
			File f = new File(uri);
			String fileName = f.getName();
			uri = fileName + "?" + queryStr;
		}
		catch (URISyntaxException e) {
		}
		catch (MalformedURLException e) {
		}
		
		return uri;
	}
	
	/**
	 * Convierte un timestamp sin nanosegundos.
	 * @param t
	 * @return
	 */
	public static Timestamp convertDate( Timestamp t )
	{
		String ts = t.toString();
		ts = ts.substring(0, ts.indexOf('.'));
		return Timestamp.valueOf(ts);
	}
	
	/**
	 * Convierte un array de valores en un String de valores separados por comas.
	 * @param strs
	 * @return
	 */
	public static String ArrayStringToCSV( String[] strs )
	{
		String output = new String();
		for (int i = 0; i < strs.length; i++) { 
			output += strs[i];
			if (i != strs.length - 1)
				output += ",";	
		}
		return output;
	}
	
	/**
	 * Formatea una fecha especifica en un formato valido para Timestamp.
	 * @param fecha
	 * @return
	 */
	public static Timestamp formatearFecha( String fecha )
	{
		System.out.println( "Fecha a formatear " + fecha );
		Timestamp timeStamp;
		try {
			// Si la fecha esta incompleta, este metodo se encarga de hacer que sea posible crear un Timestamp
			String[] dateFormats = new String[]{"yyyy-MM-dd", "yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss"};
			java.util.Date date = DateUtils.parseDate(fecha, dateFormats);
			timeStamp = new Timestamp(date.getTime());			
			System.out.println( timeStamp );
		} catch (ParseException e){ System.out.println("Ocurrio una excepción con el formato" ); return null; }		
		return timeStamp;
	}
	
	/**
	 * Crea una fecha en formato actual para colocarla en un campo datetime-local de html5
	 * Recibe un string hora en formato: HH:mm, donde HH son horas y mm minutos.
	 * La idea es usar esta función en los formularios donde se piden dos Timestamp (fecha y hora),
	 * lo que permite es crear la fecha a partir de la actual con una hora especificada, normalmente las actividades 
	 * duran 2 horas en general y la idea seria llamar a esta función para rellenar los campos de fecha y hora del formulario así:
	 * 
	 * String fechaInicio = Util.createTimestampActual( "08:00" );
	 * String fechaFin = Util.createTimestampActual( "10:00" );
	 * 
	 * @param hora
	 * @return
	 */
	public static String crearTimestampActual( String hora )
	{
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		
		String fecha = dateFormat.format(date) + "T" + hora;
		
		return fecha;
	}
	
	/**
	 * Convierte un objeto cualquiera en un mapa.
	 * @param obj
	 * @return
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	public static Map<String, Object> ConvertObjectToMap(Object obj) throws 
	    IllegalAccessException, 
	    IllegalArgumentException, 
	    InvocationTargetException {
	        Class<?> pomclass = obj.getClass();
	        pomclass = obj.getClass();
	        Method[] methods = obj.getClass().getMethods();
	
	
	        Map<String, Object> map = new HashMap<String, Object>();
	        for (Method m : methods) {
	           if (m.getName().startsWith("get") && !m.getName().startsWith("getClass")) {
	              Object value = (Object) m.invoke(obj);
	              map.put(m.getName().substring(3), (Object) value);
	           }
	        }
	    return map;
	}
	
	

}