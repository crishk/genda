<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="Modelo.Util,Modelo.Nota,Dao.NotaDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<h1>Notas</h1>

<%
/**
Parametros para la lista de notas:
{ 
	"servlet" : "ServletNotas",
	"atributos" : { 
		"Nota" : "Nota",
		"FechaCreacion" : "Fecha",
		"DescripcionActividad" : "Actividad"		
	},
	"acciones" : {
		"nueva_actividad" : "+ Nueva nota",
		"borrar" : "Borrar",
		"actualizar" : "Actualizar"
	}
}

*/

application.setAttribute("parametros", 
		"{ \"servlet\" : \"ServletNotas\",\"atributos\" : { \"Nota\" : \"Nota\",\"FechaCreacion\" : \"Fecha\",\"DescripcionActividad\" : \"Actividad\"},\"acciones\" : {\"nueva_nota\" : \"+ Nueva nota\",\"borrar\" : \"Borrar\",\"actualizar\" : \"Actualizar\"}}");


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
//Obtenemos las notas y las convertimos en un mapa.
NotaDAO accesoDAO = new NotaDAO();
accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario").toString() );

ArrayList<Nota> notasFromDAO = accesoDAO.consultarQuery(queryString);
ArrayList<Map<String,Object>> notas = accesoDAO.convertirObjetosEnMapa( notasFromDAO );

request.setAttribute("items", notas );


%>
<c:import url="template.listaDatos.jsp"/>