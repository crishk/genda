<!-- formulario de registro -->
<h1>Registrarme</h1>
<div class="${requestScope.tipo_mensaje}">
	${requestScope.mensaje}
</div>
<form id="formRegistrarme" action="ServletRegistrarme" method="post" novalidate>
	<input type="hidden" value="registrarme" name="action" id="action"/>
	<label>Nombres</label>
	<input type="text" name="nombres" id="nombres" required/>
	<small class="error">Los nombres ingresados no son válidos.</small>
	
	<label>Apellidos</label>		
	<input type="text" name="apellidos" id="apellidos" required/>
	<small class="error">Los apellidos ingresados no son válidos.</small>
	
	<label>Dirección</label>	
	<input type="text" name="direccion" id="direccion"/>
	<small class="error">La dirección ingresada es incorrecta.</small>
	
	<label>Teléfono</label>	
	<input type="text" name="telefono" id="telefono"/>
	<small class="error">Verifique el teléfono ingresado.</small>
	
	<label>Correo</label>	
	<input type="text" name="correo" id="correo" required/>
	<small class="error">Correo electrónico en uso o no es válido.</small>
	
	<label>Nombre de usuario</label>	
	<input type="text" name="nombre_usuario" id="nombre_usuario" required/>
	<small class="error">Nombre de usuario en uso o no es válido.</small>
	
	<label>Password</label>	
	<input type="password" name="password" id="password" required/>
	<small class="error">Verifique la contraseña ingresada.</small>
	
	<input type="submit" value="Registrarme" id="registrarme" name="registrarme"/>
</form>