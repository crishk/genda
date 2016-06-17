<%@page import="Dao.ActividadDAO"%>
<%@page import="Modelo.Actividad"%>
<%@page import="Modelo.Nota"%>
<%@page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%

/**
 * Realizamos la consulta de todas las actividades, pendientes, realizadas y activas.
 */
ActividadDAO accesoDAO = new ActividadDAO();

accesoDAO.setParametro( "codigo_usuario", session.getAttribute("codigo_usuario").toString() );


ArrayList<Actividad> actividadesPendientes = new ArrayList<Actividad>();  
ArrayList<Actividad> actividadesRealizadas = new ArrayList<Actividad>();  
ArrayList<Actividad> actividadesActivas = new ArrayList<Actividad>();

// Se envian como atributos para invocar a la plantilla de actividades template.actividades.jsp
request.setAttribute("actividades_realizadas", 	accesoDAO.consultarActividadesRealizadas(null,null) );
request.setAttribute("actividades_pendientes", 	accesoDAO.consultarActividadesPendientes(null,null) );
request.setAttribute("actividades_activas",		accesoDAO.consultarActividadesActivas(null,null) );

// También se podria usar la forma:
 // ArrayList<ArrayList<Actividad>> todas = accesoDAO.consultarActividadesResumen( null, null );
 // actividadesPendientes = todas.get(0);
 // actividadesRealizadas = todas.get(1);
 // actividadesActivas = todas.get(2);
 // 
 // Pero por cuestiones de legibilidad se deja de la forma anterior


%>
<h1>Inicio</h1>
<c:import url="template.actividades.jsp"></c:import>
