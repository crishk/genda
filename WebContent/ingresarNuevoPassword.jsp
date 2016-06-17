<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page import="java.util.*"  %>
<%@ page import="java.text.*"  %>
<%@ page import="Modelo.Usuario,Dao.UsuarioDAO" %>
<%
/**
 * El codigo de recuperacion  permite recuperar la contraseña.
 */
UsuarioDAO accesoDAO = new UsuarioDAO();
String codigoRecuperacion = accesoDAO.getCodigoRecuperacion( request.getParameter("usuario") );
request.setAttribute("codigoRecuperacion",codigoRecuperacion);

%>
<div>
	<c:choose>
		<c:when test="${param.codigoRecuperacion == requestScope.codigoRecuperacion}">
			<h1>Ingresar nueva contraseña</h1>
			<form method="post" action="ServletIngresarNuevoPassword" novalidate id="formIngresarNuevoPassword" name="formIngresarNuevoPassword">
				
				<input type="hidden" value="${param.codigoRecuperacion}" id="codigo_recuperacion" name="codigo_recuperacion" />
				<input type="hidden" value="${param.usuario}" id="nombre_usuario" name="nombre_usuario" />
					
				<label>Nueva contraseña</label>		
				<input type="password" id="nuevo_password" name="nuevo_password" required />
				<small class="error">La contraseña ingresada no es válida.</small>
				
				<label>Confirme su nueva contraseña</label>
				<input type="password" id="confirmar_password" name="confirmar_password" required/>		
				<small class="error">La confirmación de la contraseña no es válida.</small>
				
				<input type="submit" name="crear" id="crear" value="Crear nueva contraseña"/>
			</form>
		</c:when>
		<c:otherwise>
			<h1>No se puede reestablecer la contraseña</h1>
			<p>El vínculo proporcionado no contiene un código válido de recuperación, por favor intente realizar una nueva solicitud.</p>
		</c:otherwise>
	</c:choose>	
</div>
