<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ page import="java.util.*"  %>
<%@ page import="java.text.*"  %>
<%@ page import="Modelo.Actividad,Dao.ActividadDAO" %>
<%



DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
Date date = new Date();

// Formatea la fecha actual como valores predeterminados
// para el formulario con dos horas de diferencia entre las dos.
String fechaInicio = dateFormat.format(date) + "T08:00";
String fechaFin = dateFormat.format(date) + "T10:00";

Actividad actividad = (Actividad)request.getAttribute( "actividad" );

String codigo_actividad = request.getParameter("codigo_actividad");

// Si la actividad llega como parametro a traves de la URL se consulta y se reemplaza por la que venga como atributo
if (request.getParameterMap().containsKey("codigo_actividad") && codigo_actividad != "") {	
	ActividadDAO accesoDAO = new ActividadDAO();
	actividad = accesoDAO.consultar(Integer.parseInt(codigo_actividad));	
	request.setAttribute( "actividad", actividad );	
}

// Se obtienen las fechas de la actividad para colocarlas en los campos de inicio y fin del formulario
// En caso de realizar una actualización
if (actividad != null) {
	fechaInicio = actividad.getInicio().toString().replace(' ','T').replace(".0","");
	fechaFin = actividad.getFin().toString().replace(' ','T').replace(".0","");
}


%>

<h1>
<c:choose>
	<c:when test="${requestScope.actividad != null}">
		Actualizar
	</c:when>
	<c:otherwise>
		Nueva
	</c:otherwise>
</c:choose> actividad</h1>
<div class="${requestScope.tipo_mensaje}">
	${requestScope.mensaje}
</div>
<form action="ServletNuevaActividad" method="post" id="formNuevaActividad" novalidate>
	<input type="hidden" name="codigo" value="${requestScope.actividad.getCodigo()}" id="codigo" />
	<label>Nombre de la actividad</label>
	<input type="text" name="nombre_actividad" id="nombre_actividad" value="${requestScope.actividad.getNombre()}" required/>
	<small class="error">Escriba un nombre válido para la actividad.</small>
	
	<label>Descripción actividad</label>
	<textarea  name="descripcion" id="descripcion">${requestScope.actividad.getDescripcion()}</textarea>
	<small class="error">La descripción escrita no es válida.</small>
	
	<label>Tipo de actividad</label>
	<select name="tipo_actividad" id="tipo_actividad">
		<option value="abierta" <c:if test="${requestScope.actividad.getTipoActividad() == 'abierta'}">selected</c:if>>Abierta</option>
		<option value="restringida" <c:if test="${requestScope.actividad.getTipoActividad() == 'restringida'}">selected</c:if>>Restringida</option>
	</select>
	<small class="error">Por favor seleccione un tipo de actividad.</small>
	
	
	<label>Hora y fecha de inicio</label>
	<input type="datetime-local" name="fecha_inicio" id="fecha_inicio" value="<%= fechaInicio %>" required/>
	<small class="error">La hora y fecha de inicio especificadas no son válidas.</small>
	<small class="error fechas_no_validas">
		Las fechas seleccionadas para la actividad son incorrectas, la fecha de inicio no puede ser mayor a la de finalización.
	</small>
	
	<label>Hora y fecha de finalización</label>
	<input  type="datetime-local" name="fecha_fin"  id="fecha_fin" value="<%= fechaFin %>" required/>
	<small class="error">La hora y fecha de finalización especificadas no son válidas.</small>
	<small class="error actividad_genera_conflicto">
		La actividad que usted desea ingresar genera conflicto con otra actividad existente en el mismo rango de tiempo, por favor elija otro rango de tiempo para crear la actividad. 
		También puede que el rango de tiempo especificado no sea válido y esto genere un conflicto. Asegurese de corregir las fechas para continuar. 
	</small>
	 <c:if test="${requestScope.actividad != null}">
		<label>Última actualización</label>
		<input type="datetime-local" name="fecha_actualizacion" id="fecha_actualizacion" value="${requestScope.actividad.getFechaActualizacion().toString().replace(' ','T').replace('.0','')}" readonly />
	</c:if>
	
	<input type="hidden" name="action" id="action" value="crear"  />
	<c:choose>
		<c:when test="${requestScope.actividad == null}">
			<input type="submit" value="Crear actividad" name="crear" id="crear"/>
		</c:when>
		<c:otherwise>
			<input type="submit" value="Actualizar" name="actualizar" id="actualizar"/>
		</c:otherwise>
	</c:choose>

</form>