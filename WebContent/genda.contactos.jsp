<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="Modelo.Util,Modelo.Contacto,Dao.ContactoDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<h1>Contactos</h1>

<%	

	/**
	
	Parametros para la lista de contactos:
	
	{ 
		"servlet" : "ServletContactos",
		"atributos" : { 
			"Nombres" : "Nombres",
			"Apellidos" : "Apellidos",
			"NombrePersonalizado" : "Alias",
			"Direccion" : "Dirección",
			"Telefono" : "Teléfono",
			"Celular" : "Celular",
			"Correo" : "Correo",
			"FechaActualizacion" : "Última actualización"
		},
		"acciones" : {
			"nuevo_contacto" : "+ Nuevo contacto",
			"borrar" : "Borrar",
			"actualizar" : "Actualizar"
		}
	}
	
	*/

	application.setAttribute("parametros", 
			"{ \"servlet\" : \"ServletContactos\","+
			"\"atributos\" : { "+
			"\"Nombres\" : \"Nombres\","+
			"\"Apellidos\" : \"Apellidos\","+
			"\"NombrePersonalizado\" : \"Alias\","+
			"\"Direccion\" : \"Dirección\","+
			"\"Telefono\" : \"Teléfono\","+
			"\"Celular\" : \"Celular\","+
			"\"Correo\" : \"Correo\","+
			"\"FechaActualizacion\" : \"Última actualización\"},"+
			"\"acciones\" : {"+
				"\"nuevo_contacto\" : \"+ Nuevo contacto\","+
				"\"borrar\" : \"Borrar\","+
				"\"actualizar\" : \"Actualizar\"}}"
	);



%>
<% 
//Obtenemos el queryString dijitado en la lista de datos y realizamos la consulta de los elementos
//basado en este.
String queryString = new String();
if (null == request.getAttribute("query")) {
	queryString = "";
}
else {
	queryString = (String)request.getAttribute("query");
}
%>
<%
//Obtenemos los contactos y las convertimos en un mapa.
ContactoDAO accesoDAO = new ContactoDAO();
accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario").toString() );
System.out.println("Before calling");
ArrayList<Contacto> contactosFromDAO = accesoDAO.consultarQuery(queryString);
ArrayList<Map<String,Object>> contactos = accesoDAO.convertirObjetosEnMapa( contactosFromDAO );
request.setAttribute("items", contactos );


%>
<c:import url="template.listaDatos.jsp"/>