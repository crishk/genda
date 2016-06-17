package Modelo;
import Dao.ActividadDAO;
import java.io.Serializable;
//import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Clase que representa una actividad de la agenda.
 * 
 */

public class Actividad implements Serializable {
	private static final long serialVersionUID = 1L;


	private String codigo;					// codigo de la actividad
	private String descripcion;				// descripcion
	private Timestamp fechaActualizacion;	// fecha de actualización
	private Timestamp fechaCreacion;		// fecha de creación
	private Timestamp fechaEliminacion;		// fecha de eliminación
	private Timestamp fin;					// cuando termina la actividad
	private Timestamp inicio;				// cuando inicia la actividad
	private String nombre;					// nombre de la actividad
	private String tipoActividad;			// tipo de actividad
	private Usuario usuario;				// usuario
	private List<Nota> notas;				// notas de la actividad

	/**
	 * Constructor.
	 */
	public Actividad() {
	}

	/**
	 * ----------------- GETTERS y SETTERS de la clase.
	 * @return
	 */
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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

	public Timestamp getFin() {
		return this.fin;
	}

	public void setFin(Timestamp fin) {
		this.fin = fin;
	}

	public Timestamp getInicio() {
		return this.inicio;
	}

	public void setInicio(Timestamp inicio) {
		this.inicio = inicio;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getTipoActividad() {
		return this.tipoActividad;
	}

	public void setTipoActividad(String tipoActividad) {
		this.tipoActividad = tipoActividad;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * Devuelve todas las notas de la actividad.
	 * @return
	 */
	public List<Nota> getNotas() {
		return this.notas;
	}

	/**
	 * Agrega todas las notas de la actividad desde una lista.
	 * @param notas
	 */
	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	/**
	 * Agrega una nota a la actividad.
	 * @param nota
	 * @return
	 */
	public Nota addNota(Nota nota) {
		getNotas().add(nota);
		nota.setActividad(this);

		return nota;
	}

	/**
	 * Remueve las notas de la actividad.
	 * @param nota
	 * @return
	 */
	public Nota removeNota(Nota nota) {
		getNotas().remove(nota);
		nota.setActividad(null);

		return nota;
	}
	
	/**
	 * Imprime la actividad.
	 */
	public void imprimir()
	{
		System.out.println( "ACTIVIDAD " + this.nombre );
		if (this.notas != null && !this.notas.isEmpty()) {
			for (Nota t: this.notas) {
				t.imprimir();
			}
		}
	
	
	}
	
	/**
	 * Valida el nombre de la actividad.
	 * @param nombre
	 * @return
	 */
	public boolean validarNombreActividad( String nombre )
	{
		return Util.validarNombreActividad( nombre );
	}
	
	/**
	 * Valida la descripcion de la actividad, la descripcion puede ser nula.
	 * @param descripcion
	 * @return
	 */
	public boolean validarDescripcion( String descripcion )
	{
		if (Util.esCampoNulo( descripcion )) return true;
		return Util.validarDescripcion( descripcion );
	}
	
	/**
	 * Verifica si el tipo de actividad es valido.
	 * @param tipo_actividad
	 * @return
	 */
	public boolean validarTipoActividad( String tipo_actividad )
	{
		return Util.validarTipoActividad( tipo_actividad );
	}

	/**
	 * Verifica si una fecha es válida.
	 * 
	 * @param fecha
	 * @return
	 */
	public boolean validarFecha( Timestamp fecha )
	{				
		if (fecha == null) return false;
		System.out.println( "Actividad.validarFecha = " + fecha ); 
		return Util.validarFecha( fecha.toString() );
	}

	/**
	 * validarRangoFechas
	 * Verifica si la fecha de inicio es menor que la fecha de finalización
	 * @param inicio
	 * @param fin
	 * @return
	 */
	public boolean validarRangoFechas( Timestamp inicio, Timestamp fin )
	{
		return Util.validarRangoFechas( inicio,  fin );
	}
	
	/**
	 * actividadRestringidaGeneraConflicto
	 * Verifica si una actividad restringida genera un conflicto para ser agregada.
	 * @param actividad
	 * @return
	 */	
	public boolean actividadRestringidaGeneraConflicto( Actividad actividad )
	{
		if (this.validarRangoFechas(inicio, fin)) {
			ActividadDAO accesoDAO = new ActividadDAO();
			return accesoDAO.actividadRestringidaGeneraConflicto( actividad );
		}
		return true; // Si el rango de fechas no es valido por obvias razones esta actividad genera un conflicto.
	}
	
	/**
	 * actividadAbiertaGeneraConflicto
	 * Verifica si una actividad genera un conflicto para ser agregada.
	 * @param actividad
	 * @return
	 */
	public boolean actividadAbiertaGeneraConflicto( Actividad actividad )
	{
		if (this.validarRangoFechas(inicio, fin)) {
			ActividadDAO accesoDAO = new ActividadDAO();
			return accesoDAO.actividadAbiertaGeneraConflicto( actividad );
		}
		return true; // Si el rango de fechas no es valido por obvias razones esta actividad genera un conflicto.
	}
	
	/**
	 * 
	 * @param actividad
	 * @return
	 */
	public boolean actividadGeneraConflicto( Actividad actividad )
	{
		
		if (tipoActividad.equalsIgnoreCase("abierta")) {
			// Genera conflicto si hay tan solo una actividad restringida en el rango de tiempo inicio-fin
			return this.actividadAbiertaGeneraConflicto( actividad );
		}
		else {
			// Genera conflicto si hay tan solo una actividad abierta o restringida en el rango de tiempo inicio-fin
			return this.actividadRestringidaGeneraConflicto( actividad );
		}
	}

	/**
	 * Validación devuelve un objeto JSON al cliente cuando se hace la validación por medio de AJAX.
	 * El objeto JSON se compone del nombre de la propiedad y el valor de verdad como resultado de la validación.
	 * @return
	 */
	public JSONObject jsonValidar()
	{
		System.out.println( "Contacto.jsonValidar" );
		JSONObject json = new JSONObject();
		try {
			
			json.append( "nombre_actividad", this.validarNombreActividad(this.nombre) );
			json.append( "descripcion", this.validarDescripcion(this.descripcion) );
			json.append( "tipo_actividad", this.validarTipoActividad(this.tipoActividad) );
			json.append( "fecha_inicio", this.validarFecha(this.inicio) );
			json.append( "fecha_fin", this.validarFecha(this.fin) );
			json.append( "fechas_son_validas", this.validarRangoFechas( this.inicio, this.fin ) );
			json.append( "actividad_genera_conflicto", this.actividadGeneraConflicto( this ) );
			
		} catch (JSONException e) {}
		return json;
	}
	
	/**
	 * Validación normal de una actividad.
	 * @return
	 */
	public boolean validar()
	{
		System.out.println( "Contacto.validar" );
		return this.validarNombreActividad(this.nombre) &&
				this.validarDescripcion(this.descripcion) &&
				this.validarTipoActividad(this.tipoActividad) &&
				this.validarFecha(this.inicio) &&
				this.validarFecha(this.fin) &&
				this.validarRangoFechas(this.inicio, this.fin) &&
				!this.actividadGeneraConflicto( this );
	}

}