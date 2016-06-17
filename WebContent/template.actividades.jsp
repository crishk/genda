<%@page import="Dao.ActividadDAO"%>
<%@page import="Modelo.Actividad"%>
<%@page import="Modelo.Nota"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- template.actividades es una plantilla que recibe como atributo en el request
  -- las listas de actividades clasificadas por activiades activas, realizadas y pendientes.
  -- Toma cada una de estas listas y las coloca en el atributo "actividades" y llama a la plantilla template.resumen.jsp
 -->
<div class="gendaActs">
	<div class="actsMenu">
		<ul class="tabActividades">			
			<li><a id="ActividadesActivas" class="selected" href="#ActividadesActivas">Actividades Activas</a></li>					
			<li><a id="ActividadesRealizadas" href="#ActividadesRealizadas">Actividades Realizadas</a></li>					
			<li><a id="ActividadesPendientes" href="#ActividadesPendientes">Actividades Pendientes</a></li>	
		</ul>
	</div>
	<div class="actContent">
		<!-- resumen de actividades activas -->
		<% request.setAttribute( "actividades", (ArrayList<Actividad>)request.getAttribute("actividades_activas") ); %>
		<div class="contenidoActividades  main ActividadesActivas">
			<c:import url="template.resumen.jsp"></c:import>
		</div>
		<!-- resumen de actividades realizadas -->
		<% request.setAttribute( "actividades", (ArrayList<Actividad>)request.getAttribute("actividades_realizadas") ); %>	
		<div class="contenidoActividades  ActividadesRealizadas">
			<c:import url="template.resumen.jsp"></c:import>
		</div>
		<!-- resumen de actividades pendientes -->
		<% request.setAttribute( "actividades", (ArrayList<Actividad>)request.getAttribute("actividades_pendientes") ); %>		
		<div class="contenidoActividades  ActividadesPendientes">
			<c:import url="template.resumen.jsp"></c:import>
		</div>	
	</div>
</div>
<!-- en el tab cuando se da click cambia la URL, al actualizar 
  -- la página debe colocar el tab donde especifica el hash de la URL -->
<script type="text/javascript">
$(document).ready( function() {
	var hash = window.location.hash;

	if (hash != null) {
		$(hash).click();
	}
});

</script>

