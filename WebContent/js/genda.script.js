$(document).ready( function() {
	
	/**
	 * Permite seleccionar o deseleccionar todas las filas de una lista de datos.
	 */
	$('.selectorFilas').on('click', function() {
		var value = !this.checked;
			
		$.each( $(this).closest('form').find(".elementoFila"), function() {
			if (this.checked == value)
				$(this).trigger('click');
				//this.checked = value;
		});
	});
	
	/**
	 * Permite seleccionar elementos de las listas de datos al hacer click.
	 */
	$(".elementoFila").on( "click", function() {
		if (this.checked)
			$(this).closest('tr').addClass('selected');
		else
			$(this).closest('tr').removeClass('selected');
	});
	
	/**
	 * 
	 */
	$('form.jsListaDatos .botonAccion').on('click keydown keypress', function() {		
		submitButton( this );
	});
	
	/**
	 * 
	 */
	$('form.jsListaDatos .botonBuscar').on('click', function() {
		var valor = $(this).closest('form').find('.textoBuscar').val();
	
		if (valor == "") {
			alert('Por favor ingresa un valor a buscar para continuar');
			return false;
		}
		else {
			submitButton( this );
		}
	});
	
	/**
	 * Envia un formulario, pero antes cambia el valor de #action al id del botón
	 * para que el Servlet conozca la funcionalidad que debe realizar.
	 */
	function submitButton( button )
	{
		$(button).closest('form').find('#action').val( button.id );
		$(button).closest('form').submit();
	}
	
	/**
	 * 
	 */
	$('form.jsListaDatos .textoBuscar').on('keydown keypress', function() {
		doNothing(); // No hacer nada.
	
	});
	
	/**
	 * 
	 */
	$('form.jsListaDatos .textoBuscar').on('blur', function() {
		if ($(this).val() == "") 
			this.placeholder = 'Buscar...';
	
	});
	
	/**
	 * Selector de tabs de actividades.
	 */
	$('.gendaActs li a').on('click', function() {
		$(this).closest('ul').find('a').removeClass('selected');
		$(this).addClass('selected');
		$(this).closest('.gendaActs').find('.contenidoActividades').removeClass('main');
		$(this).closest('.gendaActs').find('.contenidoActividades.'+this.id).addClass('main');
	});
	
	
	/**
	 * Submit para formulario Nuevo contacto.
	 */
	$("form#formNuevoContacto input[type='submit']").on('click', function(event) {

		var $form = $("form#formNuevoContacto");
		var $accionActual = this.id;
		$form.find('#action').val('validate');
		var nombrePersonalizadoActual = $form.find('#np_actual').val();
		var validacionLocal = validarFormularioNuevoContacto();
		
		$.post(  $form.attr("action"), $form.serialize(), function (respuesta) {		
			
			// Si la acción que se va a realizar es una actualización debe omitirse si el nombre actual del usuario 
			// ya esta registrado.
			if (nombrePersonalizadoActual == $form.find('#nombre_personalizado').val() && $accionActual == "actualizar") {
				respuesta.nombre_personalizado_existe = "false";
			}
			
			if (respuesta.nombres == "true" &&
				respuesta.apellidos  == "true" &&
				respuesta.nombre_personalizado  == "true" &&
				respuesta.direccion  == "true" &&
				respuesta.telefono  == "true" &&
				respuesta.celular  == "true" &&
				respuesta.correo  == "true" &&
				respuesta.nombre_personalizado_existe == "false") {

				$form.find('#action').val($accionActual);
				$form.submit();
				
			}
			else {
				if (respuesta.nombre_personalizado_existe != "false") {
					$form.find('#nombre_personalizado').next('small').show();
				}
			
			}
				
		});

		event.preventDefault();
	});
	
	
	
	/**
	 * Submit para formulario registrarme.
	 */
	$("form#formRegistrarme input[type='submit']").on('click', function(event) {
		
		var $form = $("form#formRegistrarme");
		var $accionActual = this.id;
		$form.find('#action').val('validate');
		
		var validacionLocal = validarFormularioRegistrarme();
		
		$.post(  $form.attr("action"), $form.serialize(), function (respuesta) {		
			if (respuesta.nombres == "true" &&
				respuesta.apellidos  == "true" &&
				respuesta.direccion  == "true" &&
				respuesta.telefono  == "true" &&
				respuesta.correo  == "true" &&
				respuesta.nombre_usuario  == "true" &&
				respuesta.password  == "true" &&
				respuesta.nombre_usuario_existe == "false" &&
				respuesta.correo_esta_en_uso == "false") {
				$form.find('#action').val($accionActual);
				$form.submit();				
			}
			else {
				if (respuesta.nombre_usuario_existe != "false") {
					$form.find('#nombre_usuario').next('small').show();
				}
				
				if (respuesta.correo_esta_en_uso != "false") {
					$form.find('#correo').next('small').show();
				}
			}
				
		});

		event.preventDefault();
	});
	
	

	/**
	 * Submit para formulario registrarme.
	 */
	$("form#formNuevaActividad input[type='submit']").on('click', function(event) {
		
		var $form = $("form#formNuevaActividad");
		var $accionActual = this.id;
		$form.find('#action').val('validate');
		
		var validacionLocal = validarFormularioNuevaActividad();
		
		$.post(  $form.attr("action"), $form.serialize(), function (respuesta) {		
			if (respuesta.nombre_actividad == "true" &&
				respuesta.descripcion  == "true" &&
				respuesta.tipo_actividad  == "true" &&
				respuesta.fecha_inicio  == "true" &&
				respuesta.fecha_fin  == "true" &&
				respuesta.fechas_son_validas  == "true" &&
				respuesta.actividad_genera_conflicto  == "false") {
				$form.find('#action').val($accionActual);
				$form.submit();				
			}
			else {
				// Para que la fecha de la actividad sea valida tiene que cumplirse
				// que la fecha de inicio es menor que la fecha de finalización de
				// la actividad.
				if (respuesta.fechas_son_validas == "false") {
					$form.find('.error.fechas_no_validas').show();
				}
				else {
					$form.find('.error.fechas_no_validas').hide();
				}
				
				// Una actividad genera conflicto si en el rango de tiempo en el que se programa
				// (fecha de inicio y fecha de finalización) se encuentra otra actividad programada
				// solo si esa actividad es restringida o si se pretende que la nueva actividad sea restringida
				// no puede existir otra actividad ocupando el rango de tiempo especificado.
				if (respuesta.actividad_genera_conflicto != "false") {
					$form.find('.error.actividad_genera_conflicto').show();
				}
				else {
					$form.find('.error.actividad_genera_conflicto').hide();
				}
			}
				
		});

		event.preventDefault();
	});
	
	/**
	 * Submit para formulario nueva nota.
	 */
	$("form#formNuevaNota input[type='submit']").on('click', function(event) {
		

		
		var $form = $("form#formNuevaNota");
		var $accionActual = this.id;
		$form.find('#action').val('validate');
		
		var validacionLocal = validarFormularioNuevaNota();

		
		$.post(  $form.attr("action"), $form.serialize(), function (respuesta) {		
			if (respuesta.nota == "true" &&
				respuesta.actividad  == "true") {
				$form.find('#action').val($accionActual);
				$form.submit();				
			}
			else {
				if (respuesta.nota == "false") {
					$form.find('#nota').next('small').show();
				}
				else {
					$form.find('#nota').next('small').hide();
				}
				
				if (respuesta.actividad == "false") {
					$form.find('#actividad').next('small').show();
				}
				else {
					$form.find('#actividad').next('small').hide();
				}
			}
				
		});

		event.preventDefault();
	});
	
	
	/**
	 * Submit para formulario recuperar password.
	 */
	$("form#formRecuperarPassword input[type='submit']").on('click', function(event) {
				
		var $form = $("form#formRecuperarPassword");		
		
		var validacionLocal = validarFormularioRecuperarPassword();
		
		if (validacionLocal) {
			$form.submit();
		}
		else {
			$form.find('#nombre_usuario').next('small').show();
		}			

		event.preventDefault();
	});
	
	/**
	 * Submit para formulario establecer nuevo password.
	 */
	$("form#formIngresarNuevoPassword input[type='submit']").on('click', function(event) {
				
		var $form = $("form#formIngresarNuevoPassword");		
		
		var validacionLocal = validarFormularioIngresarNuevoPassword();
		
		if (validacionLocal) {
			$form.submit();
		}
		else {
			var nuevo_password = $form.find('#nuevo_password');
			var confirmacion_password = $form.find('#confirmacion_password');
			if (confirmacion_password != nuevo_password) {
				$form.find('#confirmar_password').next('small').show();
			}
		}			

		event.preventDefault();
	});
	
	
	/**
	 * Eventos 'keyup', cuando se esta escribiendo en los campos independientemente se 
	 * hace la validación por campo.
	 */
	
	$('form#formNuevoContacto input, textarea, select').on('keyup', function() {
		validarFormularioNuevoContacto( this.id );
	});

	// keyup
	$('form#formRegistrarme input, textarea, select').on('keyup', function() {
		validarFormularioRegistrarme( this.id );
	});
	
	// keyup
	$('form#formNuevaActividad input, textarea, select').on('keyup', function() {
		validarFormularioNuevaActividad( this.id );
	});
	
	// keyup
	$('form#formNuevaNota input, textarea, select').on('keyup', function() {
		validarFormularioNuevaNota( this.id );
	});
	
	// keyup
	$('form#formRecuperarPassword input, textarea, select').on('keyup', function() {
		validarFormularioRecuperarPassword( this.id );
	});
	
	$('form#formIngresarNuevoPassword input, textarea, select').on('keyup', function() {
		validarFormularioIngresarNuevoPassword( this.id );
	});
	
	
	
	/**
	 * Valida el formulario nuevo contacto.
	 */
	function validarFormularioNuevoContacto( nombreCampo )
	{
		var form = $('form#formNuevoContacto');
		// Validación por campo en caso de que la variable nombreCampo no sea nula.				
		if (nombreCampo != "" && typeof( nombreCampo ) != 'undefined') {	
			var campo = form.find('#' + nombreCampo);
			switch (nombreCampo) {
				case "nombres":
					return validarCampo( campo, validarNombres( campo.val() ) );
					break;
				case "apellidos":
					return validarCampo( campo, validarApellidos( campo.val() ) );
					break;
				case "nombre_personalizado":
					return validarCampo( campo, validarNombrePersonalizado( campo.val() ) );
					break;
				case "direccion":
					return validarCampo( campo, validarDireccion( campo.val() ) );
					break;
				case "telefono":
					return validarCampo( campo, validarTelefono( campo.val() ) );
					break;
				case "celular":
					return validarCampo( campo, validarCelular( campo.val() ) );
					break;
				case "correo":
					return validarCampo( campo, validarCorreo( campo.val() ) );
					break;					
			}
		}
		else {
			/**
			 * Validación general de todo el formulario cuando nombreCampo es nulo.
			 */
			var nombres = form.find('#nombres');
			var apellidos = form.find('#apellidos');
			var nombre_personalizado = form.find('#nombre_personalizado');
			var direccion = form.find('#direccion');
			var telefono = form.find('#telefono');
			var celular = form.find('#celular');
			var correo = form.find('#correo');

			var val1 = validarCampo( nombres, validarNombres( nombres.val() ) );
			var val2 = validarCampo( apellidos, validarApellidos( apellidos.val() ) );
			var val3 = validarCampo( nombre_personalizado, validarNombrePersonalizado( nombre_personalizado.val() ) );
			var val4 = validarCampo( direccion, validarDireccion( direccion.val() ) );
			var val5 = validarCampo( telefono, validarTelefono( telefono.val() ) );
			var val6 = validarCampo( celular, validarCelular( celular.val() ) );
			var val7 = validarCampo( correo, validarCorreo( correo.val() ) );
			
			// Todas las validaciones deben ser verdaderas para que el formulario sea válido.
			if (val1 && val2 && val3 && val4 && val5 && val6 && val7)
				return true;
		}
		return false;
	}
	
	/**
	 * Valida el formulario de registro.
	 */
	function validarFormularioRegistrarme( nombreCampo )
	{
		var form = $('form#formRegistrarme');
		// Validación por campo en caso de que la variable nombreCampo no sea nula.
		if (nombreCampo != "" && typeof( nombreCampo ) != 'undefined') {	
			var campo = form.find('#' + nombreCampo);
			switch (nombreCampo) {
				case "nombres":
					return validarCampo( campo, validarNombres( campo.val() ) );
					break;
				case "apellidos":
					return validarCampo( campo, validarApellidos( campo.val() ) );
					break;
				case "direccion":
					return validarCampo( campo, validarDireccion( campo.val() ) );
					break;
				case "telefono":
					return validarCampo( campo, validarTelefono( campo.val() ) );
					break;
				case "correo":
					return validarCampo( campo, validarCorreo( campo.val() ) );
					break;
				case "nombre_usuario":
					return validarCampo( campo, validarNombreUsuario( campo.val() ) );
					break;
				case "password":
					return validarCampo( campo, validarPassword( campo.val() ) );
					break;					
			}
		}
		else {
			/**
			 * Validación general de todo el formulario cuando nombreCampo es nulo.
			 */
			var nombres = form.find('#nombres');
			var apellidos = form.find('#apellidos');
			var direccion = form.find('#direccion');
			var telefono = form.find('#telefono');
			var correo = form.find('#correo');
			var nombre_usuario = form.find('#nombre_usuario');
			var password = form.find('#password');
			
			
			var val1 = validarCampo( nombres, validarNombres( nombres.val() ) );
			var val2 = validarCampo( apellidos, validarApellidos( apellidos.val() ) );
			var val3 = validarCampo( direccion, validarDireccion( direccion.val() ) );
			var val4 = validarCampo( telefono, validarTelefono( telefono.val() ) );
			var val5 = validarCampo( correo, validarCorreo( correo.val() ) );
			var val6 = validarCampo( nombre_usuario, validarNombreUsuario( nombre_usuario.val() ) );
			var val7 = validarCampo( password, validarPassword( password.val() ) );
			
			// Todas las validaciones deben ser verdaderas para que el formulario sea válido.
			if (val1 && val2 && val3 && val4 && val5 && val6 && val7)
				return true;
		}
		return false;
	}
	
	/**
	 * Valida el formulario nueva actividad.
	 */
	function validarFormularioNuevaActividad( nombreCampo )
	{
		var form = $('form#formNuevaActividad');
				
		// Validación por campo en caso de que la variable nombreCampo no sea nula.
		if (nombreCampo != "" && typeof( nombreCampo ) != 'undefined') {	
			var campo = form.find('#' + nombreCampo);
			switch (nombreCampo) {
				case "nombre_actividad":
					return validarCampo( campo, validarNombreActividad( campo.val() ) );
					break;
				case "descripcion":
					return validarCampo( campo, validarDescripcion( campo.val() ) );
					break;
				case "tipo_actividad":
					return validarCampo( campo, validarTipoActividad( campo.val() ) );
					break;
				case "fecha_inicio":
					return validarCampo( campo, validarFecha( campo.val() ) );
					break;
				case "fecha_fin":
					return validarCampo( campo, validarFecha( campo.val() ) );
					break;				
			}
		}
		else {
		
			/**
			 * Validación general de todo el formulario cuando nombreCampo es nulo.
			 */
			var nombre_actividad = form.find('#nombre_actividad');
			var descripcion = form.find('#descripcion');
			var tipo_actividad = form.find('#tipo_actividad');
			var fecha_inicio = form.find('#fecha_inicio');
			var fecha_fin = form.find('#fecha_fin');


			var val1 = validarCampo( nombre_actividad, validarNombreActividad( nombre_actividad.val() ) );
			var val2 = validarCampo( descripcion, validarDescripcion( descripcion.val() ) );
			var val3 = validarCampo( tipo_actividad, validarTipoActividad( tipo_actividad.val() ) );
			
			var valueFechaInicio = document.getElementById("fecha_inicio").value;
			var valueFechaFin = document.getElementById("fecha_inicio").value;
			
			var val4 = validarCampo( fecha_inicio, validarFecha( valueFechaInicio ) );
			var val5 = validarCampo( fecha_fin, validarFecha( valueFechaFin ) );

			
			// Todas las validaciones deben ser verdaderas para que el formulario sea válido.
			if (val1 && val2 && val3 && val4 && val5)
				return true;
		}
		return false;
	}
	
	/**
	 * Valida el formulario nueva nota.
	 */
	function validarFormularioNuevaNota( nombreCampo )
	{
		var form = $('form#formNuevaNota');
				
		// Validación por campo en caso de que la variable nombreCampo no sea nula.
		if (nombreCampo != "" && typeof( nombreCampo ) != 'undefined') {	
			var campo = form.find('#' + nombreCampo);
			switch (nombreCampo) {
				case "nota":
					return validarCampo( campo, validarNota( campo.val() ) );
					break;
				case "actividad":
					return validarCampo( campo, validarCodigoActividad( campo.val() ) );
					break;				
			}
		}
		else {
		
			/**
			 * Validación general de todo el formulario cuando nombreCampo es nulo.
			 */
			var nota = form.find('#nota');
			var actividad = form.find('#actividad');
			
			var val1 = validarCampo( nota, validarNota( nota.val() ) );
			var val2 = validarCampo( actividad, validarCodigoActividad( actividad.val() ) );
			
			// Todas las validaciones deben ser verdaderas para que el formulario sea válido.
			if (val1 && val2)
				return true;
		}
		return false;
	}
	
	
	/**
	 * Valida el formulario recuperar password que permite hacer una solicitud a la cuenta 
	 * para realizar la recuperación del password.
	 */
	function validarFormularioRecuperarPassword( nombreCampo )
	{
		var form = $('form#formRecuperarPassword');
				
		// Validación por campo en caso de que la variable nombreCampo no sea nula.
		if (nombreCampo != "" && typeof( nombreCampo ) != 'undefined') {	
			var campo = form.find('#' + nombreCampo);
			switch (nombreCampo) {
				case "nombre_usuario":
					return validarCampo( campo, validarNombreUsuario( campo.val() ) );
					break;	
			}
		}
		else {
		
			/**
			 * Validación general de todo el formulario cuando nombreCampo es nulo.
			 */
			var nombre_usuario = form.find('#nombre_usuario');
			
			var val1 = validarCampo( nombre_usuario, validarNombreUsuario( nombre_usuario.val() ) );

			
			// Todas las validaciones deben ser verdaderas para que el formulario sea válido.
			return val1;
		}
		return false;
	}
	
	
	/**
	 * Valida el formulario recuperar password que permite hacer una solicitud a la cuenta 
	 * para realizar la recuperación del password.
	 */
	function validarFormularioIngresarNuevoPassword( nombreCampo )
	{
		var form = $('form#formIngresarNuevoPassword');
				
		// Validación por campo en caso de que la variable nombreCampo no sea nula.
		if (nombreCampo != "" && typeof( nombreCampo ) != 'undefined') {	
			var campo = form.find('#' + nombreCampo);
			switch (nombreCampo) {
				case "nuevo_password":
				case "confirmar_password":
					return validarCampo( campo, validarPassword( campo.val() ) ) ;
					break;	
			}
		}
		else {
		
			/**
			 * Validación general de todo el formulario cuando nombreCampo es nulo.
			 */
			var nuevo_password = form.find('#nuevo_password');
			var confirmar_password = form.find('#confirmar_password');
			
			var val1 = validarCampo( nuevo_password, validarPassword( nuevo_password.val() ) );
			var val2 = validarCampo( confirmar_password, validarPassword( confirmar_password.val() ) );
			
			// Todas las validaciones deben ser verdaderas para que el formulario sea válido.
			// en este caso se agrega una validación adicional que especifica que ambas contraseñas deben ser iguales.
			return val1 && val2 && (nuevo_password.val() == confirmar_password.val());
		}
		return false;
	}

	/**
	 * Esta función permite que cuando se este escribiendo en el campo buscar
	 * de las listas de datos y el usuario presione 'enter' no se envie el formulario
	 * lo cual obliga al usuario a hacer click en el botón Buscar.
	 */
	function doNothing() {  
		var keyCode = event.keyCode ? event.keyCode : event.which ? event.which : event.charCode;
		    if( keyCode == 13 ) {


			if(!e) var e = window.event;

			e.cancelBubble = true;
			e.returnValue = false;

			if (e.stopPropagation) {
				e.stopPropagation();
				e.preventDefault();
			}
		}
	}
});