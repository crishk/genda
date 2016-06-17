<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="Modelo.Util" %>
<%@ page import="org.json.JSONException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.io.*,java.util.*,java.sql.*,Dao.UsuarioDAO"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%

/**
 * El codigo de recuperacion  permite recuperar la contraseña.
 */
UsuarioDAO accesoDAO = new UsuarioDAO();
String codigoRecuperacion = accesoDAO.getCodigoRecuperacion( request.getParameter("usuario") );
System.out.println("Codigo de recuperacion " + codigoRecuperacion );

// Si es nulo no puede hacer una recuperación de contraseña.
if (codigoRecuperacion == null) {
	System.out.println( "Usted no puede recuperar la contraseña" );
}
else {
	// En caso contrario coloca el codigo de recuperacion disponible para que 
	// el usuario vea el vinculo y sea redirigido a ingresar la nueva contraseña.
	request.setAttribute( "codigoRecuperacion", codigoRecuperacion );
}


%>    
<div>
	<c:choose>
		<c:when test="${codigoRecuperacion == null}">
			<h1>Error al intentar recuperar la contraseña</h1>
			<p>Usted no ha solicitado una recuperación de la contraseña. Para solicitar una nueva contraseña dirijase a <a href="index.jsp?pagina=olvidoPassword">Recuperar contraseña</a></p>
		</c:when>
		<c:otherwise>
			<h1>Simulación de correo electrónico</h1>
			<p>Usted ha solicitado una contraseña nueva desde GENDA.</p>
			<p>Para colocar su nueva contraseña haga click en el siguiente vínculo:</p>
			<p><a href="index.jsp?pagina=ingresarNuevoPassword&usuario=${param.usuario}&codigoRecuperacion=${requestScope.codigoRecuperacion}">?pagina=ingresarNuevoPassword&usuario=${param.usuario}&codigoRecuperacion=${requestScope.codigoRecuperacion}</a></p>
			
			<p>Gracias,</p>
			
			<p>Grupo GENDA</p>
		</c:otherwise>
	</c:choose>
</div>