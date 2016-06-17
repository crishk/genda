/**
 * Valida si un campo es válido con funcionalidad de usuario.
 * 
 * Bajo tres aspectos esta función realiza su tarea:
 * 
 * 1. El campo es requerido.
 * 2. El campo es nulo.
 * 3. El campo es valido.
 * 
 * Para que el campo sea mostrado como erroneo debe cumplirse:
 * 
 * 1. El campo es requerido, no es  nulo y es válido.
 * 
 * 2. El campo no es requerido, no es nulo y es válido.
 * 
 * En estos dos casos se ocultara el error que indica que el campo no es válido, en caso contrario
 * se mostrara el error.
 * 
 * @param campo
 * @param resultadoValidacion
 * @returns {Boolean}
 */

function validarCampo( campo, resultadoValidacion )
{
	console.log( "-----------Validando " + campo.prop('id')  );
	var esRequerido = campo.prop('required');
	if (esRequerido) console.log( campo.prop('id') + "> es requerido." );
	else
		console.log( campo.prop('id') + "> NO es requerido." );
	
	var esNulo = campo.val().trim() == "";	
	if (esNulo) console.log( campo.prop('id') + "> es nulo." );
	else
		console.log( campo.prop('id') + " NO es nulo." );
	
	var esValido = resultadoValidacion;
	if (esValido) console.log( campo.prop('id') + " es valido." );
	else
		console.log( campo.prop('id') + " NO es valido." );
	
	if (esRequerido) {
		if (!esNulo) {
			if (esValido) 
				{
				campo.next('small').hide();
				return true;
				}
			else
				campo.next('small').show();
		}
	}
	
	if (!esRequerido) {
		if (!esNulo) {
			if (esValido) {
				campo.next('small').hide();
				return true;
			}
			else
				campo.next('small').show();
		}
		else {
			return true;
		}
	}
	campo.next('small').show();
	return false;
}

/**
 * Valida que los nombres tengan un formato valido.
 * @param nombres
 * @returns {Boolean}
 */
function validarNombres( nombres )
{
	if (!esVariableNula( nombres ))
		return true;		
	return false;
}

/**
 * Valida que los apellidos tengan un formato valido.
 * @param apellidos
 * @returns {Boolean}
 */
function validarApellidos( apellidos )
{	
	if (!esVariableNula( apellidos ))
		return true;	
	return false;
}

/**
 * Valida que el nombre personalizado tenga un formato válido.
 * @param nombrePersonalizado
 * @returns {Boolean}
 */
function validarNombrePersonalizado( nombrePersonalizado )
{
	if (!esVariableNula( nombrePersonalizado ))
		return true;	
	return false;
}

/**
 * Valida que la dirección sea correcta.
 * @param direccion
 * @returns {Boolean}
 */
function validarDireccion( direccion )
{
	if (!esVariableNula( direccion ))
		return true;	
	return false;
}

/**
 * Valida que el teléfono este compuesto por numeros, - o ., parentesis y/o un espacio.
 * @param telefono
 * @returns {Boolean}
 */
function validarTelefono( telefono )
{
	if (!esVariableNula( telefono )) {
		var re = /^[(]{0,1}[0-9]{3}[)]{0,1}[-\s\.]{0,1}[0-9]{3}[-\s\.]{0,1}[0-9]{4}$/;
		return re.test(telefono);
	}
	
	/*if (!esVariableNula( telefono ))
		return true;*/	
	return false;
}

/**
 * Valida que el celular sea correcto (llama a funcion validarTelefono).
 * @param celular
 * @returns {Boolean}
 */
function validarCelular( celular )
{
	return validarTelefono( celular );
}

/**
 * Valida que el correo sea válido.
 * @param correo
 * @returns {Boolean}
 */
function validarCorreo( correo )
{
	if (!esVariableNula( correo )) {
	 var re = /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;
	 	return re.test(correo);
	}
	/*if (!esVariableNula( correo ))
		return true;*/	
	return false;
}

/**
 * Valida que un password sea válido, basta con que no sea nulo.
 * @param password
 * @returns {Boolean}
 */
function validarPassword( password )
{
	if (!esVariableNula( password ))
		return true;	
	return false;
}

/**
 * Valida que un nombre de usuario solo este compuesto por letras y numeros.
 * @param nombre_usuario
 * @returns {Boolean}
 */
function validarNombreUsuario( nombre_usuario )
{	

	if (!esVariableNula( nombre_usuario )) {
		 var re = /^[a-zA-Z0-9]+$/;
		 return re.test(nombre_usuario);		
	}
	//	return true;	

	return false;
}

/**
 * Valida el nombre de una actividad, basta con que no sea nula.
 * @param nombre_actividad
 * @returns {Boolean}
 */
function validarNombreActividad( nombre_actividad )
{
	if (!esVariableNula( nombre_actividad ))
		return true;	
	return false;
}

/**
 * Valida que la descripción tenga un contenido válido.
 * @param descripcion
 * @returns {Boolean}
 */
function validarDescripcion( descripcion )
{
	if (!esVariableNula( descripcion ))
		return true;	
	return false;
}

/**
 * Valida que el tipo de actividad sea correcto.
 * @param tipo_actividad
 * @returns {Boolean}
 */
function validarTipoActividad( tipo_actividad )
{
	if (!esVariableNula( tipo_actividad ))
		return true;	
	return false;
}

/**
 * Validación de una fecha desde que no sea nula.
 * @param fecha
 * @returns {Boolean}
 */
function validarFecha( fecha )
{
	if (!esVariableNula( fecha ))
		return true;	
	return false;
}

/**
 * Valida que la nota sea correcta.
 * @param nota
 * @returns {Boolean}
 */
function validarNota( nota )
{
	if (!esVariableNula( nota ))
		return true;	
	return false;
}

/**
 * Valida que una variable especifica no sea nula.
 * @param variable
 * @returns {Boolean}
 */
function esVariableNula( variable )
{
	if (variable == null && variable.trim() == "" && typeof( variable ) == "undefined")
		return true;	
	return false;
}

/**
 * Valida que la variable n sea un entero.
 * @param n
 * @returns {Boolean}
 */
function isInt(n){
	return n % 1 === 0;;
}

/**
 * Valida el codigo de una actividad, no puede ser nulo y debe ser entero.
 * @param codigo_actividad
 * @returns {Boolean}
 */
function validarCodigoActividad( codigo_actividad )
{
	if (!esVariableNula( codigo_actividad)) {
		if (isInt( codigo_actividad )) {
			return true;
		}
	}
	
	return false;
}