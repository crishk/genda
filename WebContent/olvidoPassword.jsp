<!-- Formulario de recuperar la contrase�a -->
<% 
/**
 * Se guarda la pagina actual para que el link "Regresar" inferior lo mantenga actualizado.
 **/ 
session.setAttribute( "pagina", "iniciarSesion" ); %>
<h2>Recuperar contrase�a</h2>
<p>Por favor ingrese su nombre de usuario o correo electr�nico para recuperar su contrase�a.</p>
<div class="${requestScope.tipo_mensaje}">
	${requestScope.mensaje}
</div>
<form action="ServletRecuperarPassword" method="post" novalidate id="formRecuperarPassword" name="formRecuperarPassword">

	<label>Nombre de usuario o correo</label>
	
	<input type="text" name="nombre_usuario" id="nombre_usuario" required/>
	<small class="error">El nombre de usuario ingresado no es v�lido.</small>
	
	<input type="submit" name="recuperar" value="Recuperar"/>
</form>
