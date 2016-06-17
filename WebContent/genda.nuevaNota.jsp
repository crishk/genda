<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="java.util.Date" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Calendar" %>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ page import="Dao.ActividadDAO,Modelo.Actividad,Dao.NotaDAO" %>
<%@ page import="Modelo.Nota" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%
ActividadDAO accesoDAO = new ActividadDAO();
accesoDAO.setParametro( "codigo_usuario", session.getAttribute("codigo_usuario").toString() );
ArrayList<Actividad> actividades = new ArrayList<Actividad>();
ArrayList<Actividad> actividadesActivas = accesoDAO.consultarActividadesActivas( null, null );
ArrayList<Actividad> actividadesPendientes = accesoDAO.consultarActividadesPendientes( null, null);
ArrayList<Actividad> actividadesRealizadas = accesoDAO.consultarActividadesRealizadas( null, null);


request.setAttribute( "actividadesActivas", actividadesActivas ); 
request.setAttribute( "actividadesPendientes", actividadesPendientes );
request.setAttribute( "actividadesRealizadas", actividadesRealizadas );
%>

<h1>
<c:choose>
	<c:when test="${requestScope.nota != null}">
		Actualizar
	</c:when>
	<c:otherwise>
		Nueva
	</c:otherwise>
</c:choose> nota</h1>
<%
// Formatea la fecha actual en un Timestamp
DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
Date date = new Date();
String fecha = dateFormat.format(date);

Nota nota = (Nota)request.getAttribute("nota");

String codigo_nota = request.getParameter("codigo_nota");

// Si el codigo de la nota esta en los parametros quiere decir que se debe realizar una actualización
if (request.getParameterMap().containsKey("codigo_nota") && codigo_nota != "") {	
	NotaDAO accesoNotas = new NotaDAO();
	nota = accesoNotas.consultar(Integer.parseInt(codigo_nota));	
	request.setAttribute( "nota", nota );
}

// Si la nota viene como atributo en el request se debe cargar el formulario para una actualización
if (nota != null) {
	System.out.println(  "Codigo nota aqui " + nota.getCodigo() );
	System.out.println( "Codigo actividad de nota aqui " + nota.getActividad().getCodigo() );
	request.setAttribute("codigoActividad", nota.getActividad().getCodigo());
}
%>

<p>La fecha de la nota sera aproximadamente <strong><%= fecha %></strong>.</p>
<form action="ServletNuevaNota" method="post" id="formNuevaNota" novalidate>
	<input type="hidden" name="codigo" value="${requestScope.nota.getCodigo()}" id="codigo" />
	<input type="hidden" value="nueva_nota" name="action" id="action"/>
	<label>Nota</label>
	<textarea id="nota" name="nota">${requestScope.nota.getNota()}</textarea>
	<small class="error">Por favor, escriba la nota que desea ingresar.</small>
	
	<label>Selecciona a que actividad deseas asignar la nota</label>
	<select name="actividad" id="actividad">		
		<option value="">Ningúna</option>
		<optgroup label="Actividades Activas">	
			<c:forEach var="row" items="${requestScope.actividadesActivas}">
				<option <c:if test="${row.getCodigo() == requestScope.codigoActividad || row.getCodigo() == param['codigo_actividad']}">selected</c:if> value="${row.getCodigo()}">&nbsp;&nbsp;${row.getNombre()} - (${row.getInicio().toString()} / ${row.getFin().toString()})</option>
			</c:forEach> 
		</optgroup>
		<optgroup label="Actividades Pendientes">
		<c:forEach var="row" items="${requestScope.actividadesPendientes}">
			<option <c:if test="${row.getCodigo() == requestScope.codigoActividad || row.getCodigo() == param['codigo_actividad']}">selected</c:if> value="${row.getCodigo()}">&nbsp;&nbsp;${row.getNombre()} - (${row.getInicio().toString()} / ${row.getFin().toString()})</option>
		</c:forEach>
		</optgroup>
		<optgroup label="Actividades Realizadas">		
		<c:forEach var="row" items="${requestScope.actividadesRealizadas}">
			<option <c:if test="${row.getCodigo() == requestScope.codigoActividad || row.getCodigo() == param['codigo_actividad']}">selected</c:if> value="${row.getCodigo()}">&nbsp;&nbsp;${row.getNombre()} - (${row.getInicio().toString()} / ${row.getFin().toString()})</option>
		</c:forEach>	
		</optgroup>
	</select>
	<small class="error">Seleccione una actividad de la lista.</small>
	
	<c:if test="${requestScope.nota != null}">
		<label>Última actualización</label>
		<input type="datetime-local" name="fecha_actualizacion" id="fecha_actualizacion" value="${requestScope.nota.getFechaActualizacion().toString().replace(' ','T').replace('.0','')}" readonly />
	</c:if>
	<c:choose>
		<c:when test="${requestScope.nota == null}">
			<input type="submit" value="Crear nota" name="crear" id="crear"/>
		</c:when>
		<c:otherwise>
			<input type="submit" value="Actualizar" name="actualizar" id="actualizar"/>
		</c:otherwise>
	</c:choose>
</form>