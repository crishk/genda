<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page import="Modelo.Util"  %>
<h1><c:choose>
	<c:when test="${requestScope.contacto != null}">
		Actualizar
	</c:when>
	<c:otherwise>
		Nuevo
	</c:otherwise>
</c:choose>  contacto</h1>
<div class="${requestScope.tipo_mensaje}">
	${requestScope.mensaje}
</div>
<form action="ServletNuevoContacto" method="post"  id="formNuevoContacto" novalidate>
	<input type="hidden" value="crear" name="action" id="action" />
	<!--  nombre personalizado actual, no varia durante una actualizaci�n y sirve para compararlo. -->
	<input type="hidden" value="${requestScope.contacto.getNombrePersonalizado()}" name="np_actual" id="np_actual" />
	<input type="hidden" value="${requestScope.contacto.getCodigo()}" id="codigo_usuario" name="codigo_usuario"/>
	<label class="required">Nombres</label>
	<input type="text" id="nombres" name="nombres" value="${requestScope.contacto.getNombres()}" required/>
	<small class="error">Escriba los nombres del contacto.</small>
	
	<label class="required">Apellidos</label>	
	<input type="text" id="apellidos" name="apellidos" value="${requestScope.contacto.getApellidos()}" required/>
	<small class="error">Escriba los apellidos del contacto.</small>
	
	<label>Nombre personalizado</label>	
	<input type="text" id="nombre_personalizado" name="nombre_personalizado" value="${requestScope.contacto.getNombrePersonalizado()}"/>
	<small class="error">El nombre personalizado no es correcto.</small>
	
	<label>Direcci�n</label>	
	<input type="text" id="direccion" name="direccion" value="${requestScope.contacto.getDireccion()}"/>
	<small class="error">La direcci�n ingesada no es v�lida.</small>
	
	<label class="required">Tel�fono</label>	
	<input type="text" id="telefono" name="telefono" value="${requestScope.contacto.getTelefono()}" required/>
	<small class="error">Escriba un telefono v�lido.</small>
	
	<label>Celular</label>	
	<input type="text" id="celular" name="celular" value="${requestScope.contacto.getCelular()}"/>
	<small class="error">El n�mero de celular no es correcto.</small>
	
	<label class="required">Correo</label>	
	<input type="text" id="correo" name="correo" value="${requestScope.contacto.getCorreo()}" required/>
	<small class="error">Verifique la direcci�n de correo electr�nico.</small>
	
	<c:if test="${requestScope.contacto != null}">
		<label>�ltima actualizaci�n</label>
		<input type="datetime-local" id="fecha_actualizacion" name="fecha_actualizacion" value="${requestScope.contacto.getFechaActualizacion().toString().replace(' ','T').replace('.0','')}" readonly />
	</c:if>
	
	<c:choose>
		<c:when test="${requestScope.contacto == null}">
			<input type="submit" value="Crear contacto" name="crear" id="crear" />
		</c:when>
		<c:otherwise>
			<input type="submit" value="Actualizar" name="actualizar" id="actualizar"/>
		</c:otherwise>
	</c:choose>
</form>