<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="Modelo.Util,Dao.ObjetoDAO" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%

ObjetoDAO.conteoConexion = 0;


try {

	if (request.getRequestedSessionId() != null && !request.isRequestedSessionIdValid()) {
		System.out.println("Ir a index.jsp");
	   response.sendRedirect("index.jsp");
	}
	else
	if (session.getAttribute("codigo_usuario") == null && (request.getParameter("pagina").equals("agenda"))) {
		System.out.println("Intentando entrar en GENDA sin usuario...");
 		response.sendRedirect("index.jsp?pagina=iniciarSesion");
 	}
 	else if (request.getParameterMap().containsKey("accion")) {
 	System.out.println("Accion cerrar sesión...");
	 if (request.getParameter("accion").equals("cerrarSesion")) {
	 
	 	System.out.println("Cerrando sesión...");
	 	request.getSession().invalidate();
	 	response.sendRedirect("index.jsp");
	 }
	} 	
	else {

	%>
		<c:import url="mainGenda.jsp"/>
	<%
	}

 } catch (NullPointerException e) { 
 %>
		<c:import url="mainGenda.jsp"/>
	<%
 System.out.println("Ocurrio una excepcion...");
 
  }
 

 
 %>