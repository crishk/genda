package Modelo;

import Dao.ContactoDAO;

import java.io.Serializable;

import java.sql.Timestamp;

import org.json.JSONException;
import org.json.JSONObject;
/**
 * The persistent class for the contacto database table.
 * 
 */

public class Contacto  implements Serializable  {
	private static final long serialVersionUID = 1L;

	private String codigo;					// codigo del contacto
	private String apellidos;				// apellidos del contacto
	private String celular;					// celular del contacto
	private String correo;					// correo del contacto
	private String direccion;				// direccion del contacto
	private Timestamp fechaActualizacion;	// fecha de actualizacion
	private Timestamp fechaCreacion;		// fecha de creacion
	private Timestamp fechaEliminacion;		// fecha de eliminacion
	private String nombrePersonalizado;		// nombre personalizado del contacto
	private String nombres;					// nombres del contacto
	private String telefono;				// el telefono
	private Usuario usuario;				// y el usuario (Objeto)

	public Contacto() {
	}

	/**
	 * GETTERS y SETTERS del Usuario
	 * @return
	 */
	public String getCodigo() {
		return this.codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getApellidos() {
		return this.apellidos;
	}

	public void setApellidos(String apellidos) {
		if (this.validarApellidos( apellidos ))
			this.apellidos = apellidos;
	}
	
	/**
	 * Valida los apellidos.
	 * @param apellidos
	 * @return
	 */
	public boolean validarApellidos( String apellidos ) {
		return Util.validarApellidos( apellidos );
	}

	public String getCelular() {
		return this.celular;
	}

	public void setCelular(String celular) {
		if (this.validarCelular(celular))
			this.celular = celular;
	}
	
	/**
	 * Valida el numero de celular. Puede ser nulo.
	 * @param celular
	 * @return
	 */
	public boolean validarCelular( String celular ) {
		if (Util.esCampoNulo(celular)) return true;
		return Util.validarTelefono( celular );
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		if (this.validarCorreo(correo))
			this.correo = correo;
	}
	
	/**
	 * Valida el correo.
	 * @param correo
	 * @return
	 */
	public boolean validarCorreo( String correo ) {
		return Util.validarCorreo(correo);
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		if (this.validarDireccion(direccion))
			this.direccion = direccion;
	}
	
	/**
	 * Valida la dirección.
	 * @param correo
	 * @return
	 */
	public boolean validarDireccion( String direccion ) {
		System.out.println("Valor de direccion de contacto '" + direccion + "'" );
		if (Util.esCampoNulo(direccion)) return true;
		
		return Util.validarDireccion(direccion);
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

	public String getNombrePersonalizado() {
		return this.nombrePersonalizado;
	}

	public void setNombrePersonalizado(String nombrePersonalizado) {	
		if (this.validarNombrePersonalizado(nombrePersonalizado))
			this.nombrePersonalizado = nombrePersonalizado;
	}
	
	/**
	 * Valida el nombre personalizado.
	 * @param correo
	 * @return
	 */
	public boolean validarNombrePersonalizado( String nombre_personalizado ) {
		System.out.println( "Nombre personalizado '" + nombre_personalizado + "'" );
		if (Util.esCampoNulo(nombre_personalizado)) return true;
		return Util.validarNombrePersonalizado( nombre_personalizado );
	}
	
	/**
	 * Valida desde el contacto si el nombre personalizado existe.
	 * @param nombre_personalizado
	 * @return
	 */
	public boolean nombrePersonalizadoExiste( Contacto contacto )
	{
		ContactoDAO accesoDAO = new ContactoDAO();
		return accesoDAO.nombrePersonalizadoExiste( contacto );
	}

	public String getNombres() {
		return this.nombres;
	}

	public void setNombres(String nombres) {
		if (this.validarNombres(nombres))
			this.nombres = nombres;
	}
	
	/**
	 * Valida los nombres del contacto.
	 * @param nombres
	 * @return
	 */
	public boolean validarNombres( String nombres )
	{		
		return Util.validarNombres( nombres );
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		if (this.validarTelefono(telefono))
			this.telefono = telefono;
	}
	
	/**
	 * Valida el telefono.
	 * @param telefono
	 * @return
	 */
	public boolean validarTelefono( String telefono )
	{
		if (Util.esCampoNulo(telefono)) return true;
		return Util.validarTelefono( telefono );
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
			
			json.append( "nombres", this.validarNombres(this.nombres) );
			json.append( "apellidos", this.validarApellidos(this.apellidos) );
			json.append( "nombre_personalizado", this.validarNombrePersonalizado(this.nombrePersonalizado) );
			json.append( "direccion", this.validarDireccion(this.direccion) );
			json.append( "telefono", this.validarTelefono(this.telefono) );
			json.append( "celular", this.validarCelular(this.celular) );
			json.append( "correo", this.validarCorreo(this.correo) );
			json.append( "nombre_personalizado_existe", this.nombrePersonalizadoExiste( this ) );
			
		} catch (JSONException e) {}
		return json;
	}
	
	/**
	 * Validación normal de un contacto.
	 * @return
	 */
	public boolean validar()
	{
		System.out.println( "Contacto.validar" );
		return this.validarNombres(this.nombres) &&
				this.validarApellidos(this.apellidos) &&
				this.validarNombrePersonalizado(this.nombrePersonalizado) &&
				this.validarDireccion(this.direccion) &&
				this.validarTelefono(this.telefono) &&
				this.validarCelular(this.celular) &&
				this.validarCorreo(this.correo) &&
				!this.nombrePersonalizadoExiste( this );
	}

}