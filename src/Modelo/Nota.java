package Modelo;
import Dao.NotaDAO;
import Dao.ActividadDAO;
import java.io.Serializable;

import java.sql.Timestamp;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Representa la clase nota del modelo.
 * 
 */

public class Nota  implements Serializable {
	private static final long serialVersionUID = 1L;


	private String codigo;	// codigo de la nota
	private Timestamp fechaActualizacion;	// fecha de actualizacion de la nota
	private Timestamp fechaCreacion;		// fecha de creacion de la nota
	private Timestamp fechaEliminacion;		// fecha de eliminacion de la nota
	private String nota;					// nota

	//bi-directional many-to-one association to Actividad
	private Actividad actividad;
	private String descripcionActividad;

	//bi-directional many-to-one association to Usuario

	private Usuario usuario;

	public Nota() {
	}
	
	/**
	 * GETTERS Y SETTERS.
	 * @param descActividad
	 */
	public void setDescripcionActividad( String descActividad )
	{
		this.descripcionActividad = descActividad;
	}
	
	public String getDescripcionActividad()
	{
		return this.descripcionActividad;
	}

	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Timestamp getFechaActualizacion() {
		return this.fechaActualizacion;
	}

	public void setFechaActualizacion(Timestamp fechaActualizacion) {
		this.fechaActualizacion = fechaActualizacion;
	}

	public Timestamp getFechaCreacion() {
		return this.fechaCreacion;
	}

	public void setFechaCreacion(Timestamp fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Timestamp getFechaEliminacion() {
		return this.fechaEliminacion;
	}

	public void setFechaEliminacion(Timestamp fechaEliminacion) {
		this.fechaEliminacion = fechaEliminacion;
	}

	public String getNota() {
		return this.nota;
	}

	public void setNota(String nota) {
		this.nota = nota;
	}

	public Actividad getActividad() {
		return this.actividad;
	}

	public void setActividad(Actividad actividad) {
		this.actividad = actividad;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	/**
	 * Imprimir.
	 */
	public void imprimir()
	{
		System.out.println( "NOTA: " + nota );
	
	}
	
	/**
	 * Valida la nota.
	 * @param nota
	 * @return
	 */
	public boolean validarNota( String nota )
	{
		return Util.validarNota( nota );
	}
	
	/**
	 * No puede ser nula y debe existir.
	 * @param codigoActividad
	 * @return
	 */
	public boolean validarActividad( String codigoActividad )
	{
		if (!Util.esCampoNulo( codigoActividad )) {
			ActividadDAO accesoDAO = new ActividadDAO();
			try {
				Actividad actividad = accesoDAO.consultar( Integer.parseInt( codigoActividad ) );
				return actividad != null;
			}
			catch (NumberFormatException e) {
				return false;
			}
			//return n.size() != 0;
		}
		return false;
	}

	/**
	 * Validación devuelve un objeto JSON al cliente cuando se hace la validación por medio de AJAX.
	 * El objeto JSON se compone del nombre de la propiedad y el valor de verdad como resultado de la validación.
	 * @return
	 */
	public JSONObject jsonValidar()
	{
		System.out.println( "Nota.jsonValidar" );
		JSONObject json = new JSONObject();
		try {
			
			json.append( "nota", this.validarNota(this.nota) );
			json.append( "actividad", this.validarActividad(this.actividad.getCodigo()) );
			
		} catch (JSONException e) {}
		return json;
	}
	
	/**
	 * Validación normal de un contacto.
	 * @return
	 */
	public boolean validar()
	{
		System.out.println( "Nota.validar" );
		return this.validarNota(this.nota) &&
				this.validarActividad(this.actividad.getCodigo());
	}

}