<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="Modelo.Util,Modelo.Actividad,Dao.ActividadDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<h1>Actividades</h1>

<%
/**

Parametros para la lista de actividades:

{ 
	"servlet" : "ServletActividades",
	"atributos" : { 
		"Nombre" : "Actividad",
		"Descripcion" : "Descripción",
		"TipoActividad" : "Tipo",
		"Inicio" : "Comienza",
		"Fin" : "Finaliza",
		"FechaActualizacion" : "Última actualización"	},

	"acciones" : {
		"nueva_actividad" : "+ Nueva actividad",
		"borrar" : "Borrar",
		"actualizar" : "Actualizar"
	}
}

*/

application.setAttribute("parametros", 
		"{\"servlet\":\"ServletActividades\",\"atributos\":{\"Nombre\":\"Actividad\",\"Descripcion\":\"Descripción\",\"TipoActividad\":\"Tipo\",\"Inicio\":\"Comienza\",\"Fin\":\"Finaliza\",\"FechaActualizacion\":\"Última actualización\"},\"columnas\":{\"nombre\":\"Actividad\",\"descripcion\":\"Descripción\",\"tipo_actividad\":\"Tipo\",\"inicio\":\"Comienza\",\"fin\":\"Finaliza\",\"fecha_actualizacion\":\"Última actualización\"},\"acciones\":{\"nueva_actividad\":\"+ Nueva actividad\",\"borrar\":\"Borrar\",\"actualizar\":\"Actualizar\"}}");
%>
<% 
// Obtenemos el queryString dijitado en la lista de datos y realizamos la consulta de los elementos
// basado en este.
String queryString = new String();
if (null == request.getAttribute("query")) {
	queryString = "";
}
else {
	queryString = (String)request.getAttribute("query");
}
%>
<%

// Obtenemos las actividades y las convertimos en un mapa.
ActividadDAO accesoDAO = new ActividadDAO();
accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario").toString() );
System.out.println("Before calling");
ArrayList<Actividad> actividadesFromDAO = accesoDAO.consultarQuery(queryString);
ArrayList<Map<String,Object>> actividades = accesoDAO.convertirObjetosEnMapa( actividadesFromDAO );
request.setAttribute("items", actividades );


%>
<c:import url="template.listaDatos.jsp"/>
