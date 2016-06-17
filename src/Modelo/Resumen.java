package Modelo;

import java.sql.Timestamp;

/**
 * Representa un resumen en el modelo de la base de datos.
 * @author CRISHK
 *
 */
public class Resumen {
	
	String codigo;			// Codigo del resumen
	Timestamp inicio;		// Cuando inicia
	Timestamp fin;			// Cuando termina
	
	/**
	 * GETTERs y SETTERs
	 * @param codigo
	 */
	public void setCodigo( String codigo )
	{
		this.codigo = codigo;
	}
	
	public String getCodigo()
	{
		return this.codigo;
	}
	
	public void setInicio( Timestamp inicio )
	{
		this.inicio = inicio;
	}
	
	public void setFin( Timestamp fin )
	{
		this.fin = fin;
	}
	
	public Timestamp getInicio()
	{
		return inicio;
	}
	
	public Timestamp getFin()
	{
		return fin;
	}
	
	/**
	 * Valida un resumen, las fechas deben ser validas, nunca la fecha de inicio podra ser mayor que la de finalización.
	 */
	public boolean validar()
	{
		return Util.validarRangoFechas( this.getInicio(), this.getFin() );
	}

}
