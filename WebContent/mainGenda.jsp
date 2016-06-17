<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Modelo.Util" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<!doctype html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>GENDA</title>
<link href="css/estilos.css" type="text/css" rel="stylesheet"/>
<link href="//cdn.datatables.net/1.10.11/css/jquery.dataTables.min.css" type="text/css" rel="stylesheet"/>

<script type="text/javascript"  src="js/jquery-2.2.1.min.js"></script>
<script type="text/javascript"  src="js/jquery.dataTables.js"></script>
<script type="text/javascript"  src="js/genda.validadores.js"></script>
<script type="text/javascript"  src="js/genda.script.js"></script>


</head>
<body>
	<div class="modulo header">
		<a href="index.jsp"><img src="images/genda-logo.png" width="300"/></a><br/>
	</div>
	<div class="modulo content">
		<div class="inside">
		<%
			/**
			 * Este codigo permite mantener la pagina que debe mantenerse en el vinculo
			 * inferior Regresar.
			 */
			String pagina = null;
			pagina = request.getParameter( "pagina" );
			if (pagina == null)
				pagina = "inicio";
			pagina = pagina + ".jsp";
			int anio = 2016;
			try {
				session.setAttribute( "pagina", "inicio" );
				anio = Util.getActualYear();
			}
		 	catch (NullPointerException e) {}
			catch (IllegalStateException e) {}
		
		%>
		<c:import url="<%= pagina %>"/>
		</div>
	</div>
	<div class="modulo footer">
		<h2>GENDA &copy; Todos los derechos reservados <%= anio %></h2>
		<% 
		if (!pagina.equals("inicio.jsp") && !pagina.equals("agenda.jsp")) {
			
			%>
			<ul>
				<li>
					<a href="index.jsp?pagina=<%= session.getAttribute( "pagina" ) %>">Regresar</a>
				</li>
			</ul>
			<%
		}
		%>
	</div>

</body>
</html>