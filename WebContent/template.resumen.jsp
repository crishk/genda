<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="Dao.ActividadDAO"%>
<%@page import="Modelo.Actividad"%>
<%@page import="Modelo.Nota"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!--  Representa una lista de las actividades que seran visualizadas en el resumen general (ver template.actividades) -->
<div class="actividades">
	<c:if test="${requestScope.actividades.size() == 0}">
		<div class="sinActividades">
			No hay actividades para mostrar en esta sección.
		</div>
	</c:if>
	<c:forEach var="row" items="${requestScope.actividades}">
		<div class="actividad act${row.getCodigo()}">
			<div class="definicion pad">
				<div class="desc">
					<h3>${row.nombre}</h3>
					
					<p class="descripcion">${row.getDescripcion()}</p>
				</div>
				<div class="tipo">
					<p class="tipo_actividad">
					<span class="${row.getTipoActividad()} tact">
					Tipo de actividad:
					<b>
					<c:choose>
					
					<c:when test="${row.getTipoActividad() == 'abierta'}">
						Abierta
					</c:when>
					<c:otherwise>
						Cerrada					
					</c:otherwise>
					</c:choose>
					</b>
					</span>
					</p>
				</div>
				<div class="tiempo">
					<div class="actt tiempoActividad">
						<div class="tipoTiempo inicia">Inicia</div>
						<div class="timestamp">
						<fmt:formatDate type="both" 
            dateStyle="medium" timeStyle="medium" 
            value="${row.getInicio()}" /></div> 
			
						<div class="tipoTiempo termina">Termina</div>
						<div class="timestamp"> 
						<fmt:formatDate type="both" 
            dateStyle="medium" timeStyle="medium" 
            value="${row.getFin()}" /></div> 
					</div>
				</div>
				<ul class="opcionesActividad"> <li><a href="index.jsp?pagina=agenda&seccion=nuevaActividad&codigo_actividad=${row.getCodigo()}">Editar</a></li>
	
					</ul>
			</div>
			<div class="notas  pad">
				<h3>Notas</h3>
				<c:forEach var="note" items="${row.getNotas()}">
					<div class="nota">
						${note.getNota()}<br/>
						<strong>${note.getFechaActualizacion()}</strong>
						<a href="index.jsp?pagina=agenda&seccion=nuevaNota&codigo_nota=${note.getCodigo()}">Editar nota</a>
					</div>
				</c:forEach>
				<div class="nota">
					<a href="http://localhost:8153/AgendaWeb/index.jsp?pagina=agenda&seccion=nuevaNota&codigo_actividad=${row.getCodigo()}">Agregar nota</a>
				</div>
			</div>
		</div>
	</c:forEach>
</div>