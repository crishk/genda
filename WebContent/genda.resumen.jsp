<%@ page import="Modelo.Util" %>
<%@ page import="java.util.*,Modelo.Actividad,Dao.ActividadDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<h1>Resumen</h1>
<p>El resumen le permite visualizar el estado de la agenda a partir de una fecha o entre dos fechas seleccionadas.</p>
<%
/**
 * Se obtienen las fechas de inicio y de finalización.
 */
String fechaInicio = new String();
String fechaFin = new String();

if (request.getAttribute("fecha_inicio") != null)
	fechaInicio = (String)request.getAttribute("fecha_inicio");
else
	fechaInicio = Util.crearTimestampActual("08:00");

if (request.getAttribute("fecha_fin") != null)
	fechaFin = (String)request.getAttribute("fecha_fin");
else
	fechaFin = Util.crearTimestampActual("10:00");

/**
 * Se obtienen los valores de verdad de los checkboxes del inicio y fin de los tiempos.
 */
String sinInicio = "false";
String sinFin = "false";

if (request.getAttribute("sin_inicio") != null) {
	if (((String)request.getAttribute("sin_inicio")).equals("true"))
		sinInicio = "true";
}

if (request.getAttribute("sin_fin") != null) {
	if (((String)request.getAttribute("sin_fin")).equals("true"))
		sinFin = "true";
}


%>
<div class="${requestScope.tipo_mensaje}">
	${requestScope.mensaje}
</div>
<form action="ServletResumen" method="post" novalidate name="formResumen" id="formResumen">

	<div class="fechaResumen">
		<label>Fecha inicial</label>
		<input type="datetime-local" id="inicio" name="inicio" value="<%= fechaInicio %>" />
	</div>
	
	<label>
		<input type="checkbox" class="nodate" id="sin_inicio" name="sin_inicio" value="<%= sinInicio %>" <c:if test="${requestScope.sin_inicio == 'true'}">checked</c:if>/> Desde el inicio de los tiempos
	</label>
	
	<div class="fechaResumen">
		<label>Fecha final</label>
		<input type="datetime-local" id="fin" name="fin" value="<%= fechaFin %>" />
	</div>
	
	<label>
		<input type="checkbox" class="nodate" id="sin_fin" name="sin_fin" value="<%= sinFin %>" <c:if test="${requestScope.sin_fin == 'true'}">checked</c:if>/> Hasta el fin de los tiempos
	</label>
	
	<input type="submit" value="Mostrar resumen"/>
	
</form>
<script type="text/javascript">
$(document).ready( function() {
	
	// Permite que cuando el formulario sea enviado
	// y vuelto a cargar los selectores (checkboxes) se mantengan 
	// con los valores seleccionados previamente.
	function actualizarEstadoSelectores( checker )
	{
		if (checker.prop('checked')) {			
			checker.closest('label').prev('.fechaResumen').hide();
			checker.val(true);
		}
		else {
			checker.closest('label').prev('.fechaResumen').show();
			checker.val(false);
		}
	}
	
	$('.nodate').on('click', function() {
		actualizarEstadoSelectores( $(this) );
	});
	
	actualizarEstadoSelectores( $('#sin_inicio') );
	actualizarEstadoSelectores( $('#sin_fin') );
	
	
});
</script>
<br/><br/>
<%
try {
	ActividadDAO accesoDAO = new ActividadDAO();
	accesoDAO.setParametro( "codigo_usuario", session.getAttribute("codigo_usuario").toString() );
	ArrayList<ArrayList<Actividad>> actividades = new ArrayList<ArrayList<Actividad>>();
	actividades = (ArrayList<ArrayList<Actividad>>)request.getAttribute("actividades");

	request.setAttribute("actividades_realizadas",  (ArrayList<Actividad>)actividades.get(0) );
	request.setAttribute("actividades_activas",		(ArrayList<Actividad>)actividades.get(1) );
	request.setAttribute("actividades_pendientes", 	(ArrayList<Actividad>)actividades.get(2) );
	

%>
<c:import url="template.actividades.jsp"></c:import>
<% } catch (NullPointerException e) {} %>
