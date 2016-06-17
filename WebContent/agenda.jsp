<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<div class="modulo main_menu">
<ul>
	<li <c:if test="${param.seccion == 'inicio'}">class="selected"</c:if>>
		<a href="index.jsp?pagina=agenda&seccion=inicio">Inicio</a>
	<li <c:if test="${param.seccion == 'contactos'}">class="selected"</c:if>>
		<a href="index.jsp?pagina=agenda&seccion=contactos">Contactos</a></li>
	<li <c:if test="${param.seccion == 'actividades'}">class="selected"</c:if>>
		<a href="index.jsp?pagina=agenda&seccion=actividades">Actividades</a></li>
	<li <c:if test="${param.seccion == 'notas'}">class="selected"</c:if>>
		<a href="index.jsp?pagina=agenda&seccion=notas">Notas</a></li>
	<li <c:if test="${param.seccion == 'resumen'}">class="selected"</c:if>>
		<a href="index.jsp?pagina=agenda&seccion=resumen">Resumen</a></li>
	<li <c:if test="${param.seccion == ''}">class="selected"</c:if>>
		<a href="index.jsp?accion=cerrarSesion">Cerrar sesión</a></li>		
</ul>
</div>
${requestScope.seccion}
<%
String seccion = request.getParameter( "seccion" );


if (seccion == null)
	seccion = "inicio";

seccion = "genda." + seccion + ".jsp";
//out.println("Requiriendo " + seccion);
//out.println( "Valor de pagina = " + pagina );

//String linkRegresar = "index.jsp";
 %>
<jsp:include page="<%= seccion %>"/>
