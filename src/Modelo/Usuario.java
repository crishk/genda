package Modelo;

import java.io.Serializable;

import java.sql.Timestamp;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import Dao.ContactoDAO;
import Dao.UsuarioDAO;


/**
 * The persistent class for the usuario database table.
 * 
 */

public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;
 

	private String codigo;				// codigo del usuario					
	private String apellidos;			// apellidos del usuario
	private String correo;				// correo electronico del usuario
	private String direccion;			// direccion del usuario
	private Timestamp fechaActualizacion;		// fecha de actualización
	private Timestamp fechaCreacion;			// fecha de creción
	private Timestamp fechaEliminacion;			// fecha de eliminación
	private String nombreUsuario;				// nombre de usuario
	private String nombres;						// nombres
	private String password;					// contraseña
	private String telefono;					// telefono
/*
	//bi-directional many-to-one association to Actividad
	private List<Actividad> actividades;			// lista de actividades

	//bi-directional many-to-one association to Contacto
	private List<Contacto> contactos;			// lista de contactos

	//bi-directional many-to-one association to Nota
	private List<Nota> notas;*/
	
	private String codigo_recuperacion;			// el codigo de recuperacion permite recuperar la contraseña

	/**
	 * Constructor
	 */
	public Usuario() {
	}
	
	/**
	 * GETTERS Y SETTERS.
	 * @return
	 */
	public String getCodigoRecuperacion() {
		return this.codigo_recuperacion;
	}
	
	public void setCodigoRecuperacion( String codigo_recuperacion ) {
		this.codigo_recuperacion = codigo_recuperacion;
	}

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
		if (this.validarApellidos(apellidos)) 
			this.apellidos = apellidos.trim();
	}

	public String getCorreo() {
		return this.correo;
	}

	public void setCorreo(String correo) {
		if (this.validarCorreo(correo))
			this.correo = correo;
	}

	public String getDireccion() {
		return this.direccion;
	}

	public void setDireccion(String direccion) {
		if (this.validarDireccion(direccion))
			this.direccion = direccion;	
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

	public String getNombreUsuario() {
		return this.nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		if (this.validarNombreUsuario( nombreUsuario ))
			this.nombreUsuario = nombreUsuario;
	}

	public String getNombres() {		
		return this.nombres;
	}

	public void setNombres(String nombres) {
		if (this.validarNombres( nombres ))
			this.nombres = nombres.trim();
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		if (this.validarNombreUsuario( password ))
			this.password = password.trim();
	}

	public String getTelefono() {
		return this.telefono;
	}

	public void setTelefono(String telefono) {
		if (this.validarTelefono( telefono ))
			this.telefono = telefono.trim();
	}
/*
	public List<Actividad> getActividads() {
		return this.actividades;
	}

	public void setActividads(List<Actividad> actividads) {
		this.actividades = actividads;
	}

	public Actividad addActividad(Actividad actividad) {
		getActividads().add(actividad);
		actividad.setUsuario(this);

		return actividad;
	}

	public Actividad removeActividad(Actividad actividad) {
		getActividads().remove(actividad);
		actividad.setUsuario(null);

		return actividad;
	}

	public List<Contacto> getContactos() {
		return this.contactos;
	}

	public void setContactos(List<Contacto> contactos) {
		this.contactos = contactos;
	}

	public Contacto addContacto(Contacto contacto) {
		getContactos().add(contacto);
		contacto.setUsuario(this);

		return contacto;
	}

	public Contacto removeContacto(Contacto contacto) {
		getContactos().remove(contacto);
		contacto.setUsuario(null);

		return contacto;
	}

	public List<Nota> getNotas() {
		return this.notas;
	}

	public void setNotas(List<Nota> notas) {
		this.notas = notas;
	}

	public Nota addNota(Nota nota) {
		getNotas().add(nota);
		nota.setUsuario(this);

		return nota;
	}

	public Nota removeNota(Nota nota) {
		getNotas().remove(nota);
		nota.setUsuario(null);

		return nota;
	}
	*/
	
	
	/**
	 * Valida los nombres del usuario.
	 * @param nombres
	 * @return
	 */
	public boolean validarNombres( String nombres )
	{
		return Util.validarNombres( nombres );
	}
	
	/**
	 * Valida los apellidos del usuario.
	 * @param apellidos
	 * @return
	 */
	public boolean validarApellidos( String apellidos )
	{
		return Util.validarApellidos( apellidos );
	}
	
	/**
	 * Valida la direccion del usuario.
	 * @param direccion
	 * @return
	 */
	public boolean validarDireccion( String direccion )
	{
		if (Util.esCampoNulo( direccion )) return true;
		System.out.println("No es direccion nula '" + direccion + "'" );
		return Util.validarDireccion( direccion );
	}
	
	/**
	 * Valida el telefono del usuario.
	 * @param direccion
	 * @return
	 */
	public boolean validarTelefono( String telefono )
	{
		System.out.println("No es telefono nulo '" + direccion + "'" );
		if (Util.esCampoNulo( telefono )) return true;		
		return Util.validarTelefono( telefono );
	}
	
	/**
	 * Valida el correo del usuario.
	 * @param direccion
	 * @return
	 */
	public boolean validarCorreo( String correo )
	{
		if (Util.esCampoNulo( correo )) return true;
		return Util.validarCorreo( correo );
	}
	
	/**
	 * Valida el correo del usuario.
	 * @param direccion
	 * @return
	 */
	public boolean validarNombreUsuario( String nombre_usuario )
	{
		return Util.validarNombreUsuario( nombre_usuario );
	}
	
	/**
	 * Valida el password del usuario.
	 * @param direccion
	 * @return
	 */
	public boolean validarPassword( String password )
	{
		return Util.validarPassword( password );
	}
	
	/**
	 * Valida el correo del usuario.
	 * @param direccion
	 * @return
	 */
	public boolean nombreUsuarioExiste( String nombre_usuario )
	{
		UsuarioDAO accesoDAO = new UsuarioDAO();
		return accesoDAO.nombreUsuarioExiste( nombre_usuario );
	}
	
	/**
	 * Valida el correo del usuario.
	 * @param direccion
	 * @return
	 */
	public boolean correoEstaEnUso( String correo )
	{
		UsuarioDAO accesoDAO = new UsuarioDAO();
		return accesoDAO.correoEstaEnUso( correo );
	}
	
	/**
	 * Validación devuelve un objeto JSON al cliente cuando se hace la validación por medio de AJAX.
	 * El objeto JSON se compone del nombre de la propiedad y el valor de verdad como resultado de la validación.
	 * @return
	 */
	public JSONObject jsonValidar()
	{
		System.out.println("Usuario.jsonValidar");
		JSONObject json = new JSONObject();
		try {
			
			json.append( "nombres", this.validarNombres(this.nombres) );
			json.append( "apellidos", this.validarApellidos(this.apellidos) );
			json.append( "direccion", this.validarDireccion(this.direccion) );
			json.append( "telefono", this.validarTelefono(this.telefono) );
			json.append( "correo", this.validarCorreo(this.correo) );
			json.append( "nombre_usuario", this.validarNombreUsuario(this.nombreUsuario) );
			json.append( "password", this.validarPassword(this.password) );
			json.append( "nombre_usuario_existe", this.nombreUsuarioExiste( this.nombreUsuario ) );
			json.append( "correo_esta_en_uso", this.correoEstaEnUso( this.correo ) );
			
		} catch (JSONException e) {}
		return json;
	}
	
	/**
	 * Hace una validación normal y devuelve un valor de verdad indicando si los valores de las propiedades
	 * en total son validas o no.
	 * @return
	 */
	public boolean validar()
	{
		return 	this.validarNombres(this.nombres) &&
				this.validarApellidos(this.apellidos) &&
				this.validarDireccion(this.direccion) &&
				this.validarTelefono(this.telefono) &&
				this.validarCorreo(this.correo) &&
				this.validarNombreUsuario(this.nombreUsuario) &&
				this.validarPassword(this.password) &&
				!this.nombreUsuarioExiste( this.nombreUsuario ) &&
				!this.correoEstaEnUso( this.correo );
	}

}