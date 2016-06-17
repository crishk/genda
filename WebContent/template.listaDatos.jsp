<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.*" %>
<%@ page import="Modelo.Util" %>
<%@ page import="org.json.JSONException" %>
<%@ page import="org.json.JSONObject" %>
<%@ page import="java.io.*,java.util.*,java.sql.*"%>
<%@ page import="javax.servlet.http.*,javax.servlet.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>   
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
<%

/**
 * TEMPLATE template.listaDatos.jsp (ver ServletListaDatos)
 * La plantilla de listaDatos nos permite entregar un formulario al cliente
 * con los datos de una tabla de la base de datos y nos permite administrar estos datos
 * conforme a las necesidades de la aplicación:
 * 
 * Las listas de datos ofrecen las siguientes funcionalidades:
 * 
 * 1. Una lista de datos (las filas de la tabla).
 * 2. Un campo de busquedas sobre los datos.
 * 3. Unos botones de acción para realizar sobre los elementos de la lista de datos.
 * 4. Un checkbox por cada fila de la lista y uno general para seleccionar o deseleccionar todas las filas.
 * 
 * Se envian los parametros correspondientes a la lista de datos
 * que tienen la siguiente estructura en formato JSON y llegan a traves del atributo 'parametros':
 *
 * Estructura JSON de los parametros de la lista de datos:
 { 
		"servlet" : "ServletDeLaListaDeDatos",		// Este servlet debe heredar de ServletListaDatos
		
		// Son los atributos del objeto que queremos que la lista de datos muestre al usuario en la tabla
		// Se componen del nombre del atributo en la clase y comienza siempre con mayuscula y un texto cualquiera
		// que sera el nombre que aparecera en la columna de la tabla
		// así: "Atributo" : "Texto Atributo"
		//
		"atributos" : {				 
			"Attr1" : "Texto Attr1",
			"Attr2" : "Texto Attr2",
			"Attr3" : "Texto Attr3",
			...
		},

		// A través de estos se especifican los botones de acción que se podran realizar 
		// con los elementos de la lista de datos.
		// Se componen del nombre de la acción que esta esperando el servlet y un nombre
		// descriptivo para el botón de acción así:
		// "nombre_accion" : "Realizar acción"
		"acciones" : {
			"nombre_accion1" : "Descripción accion 1",
			"nombre_accion2" : "Descripción accion 2",
			"nombre_accion3" : "Descripción accion 3",
			...
		}
	}
 * 
 * Los datos de la tabla deben ser consultados previamente antes de invocar esta plantilla
 * y entregados en el atributo 'items'. (ver ejemplo en genda.actividades, genda.contactos y genda.notas)
 * La lista datos solo puede imprimir listas de mapas y por esta razón el objeto debe ser convertido previamente en un mapa
 * ya que con la especificación de atributos (en los parametros) se imprime la lista.
 *
 * Ejemplo:
 *
 * ObjetoDAO accesoDAO = new ObjetoDAO();
 * accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario").toString() );
 * ArrayList<ObjetoDAO> objetos = accesoDAO.consultarQuery(queryString);
 * ArrayList<Map<String,Object>> mapas = accesoDAO.convertirObjetosEnMapa( objetos );
 * request.setAttribute("items", mapas );
 * 
 */

// Tomamos los parametros
String parametros = new String( application.getAttribute("parametros").toString() );

// Convertimos los parametros en un objeto JSON
JSONObject t = new JSONObject( parametros );

// Obtenemos los atributos y las acciones
String atributos = new String( t.getJSONObject( "atributos" ).toString() );
String acciones = new String( t.getJSONObject( "acciones" ).toString() );

// Convertimos los atributos en un objeto LinkedHashMap 
LinkedHashMap<String,String> atributosMap = new LinkedHashMap<String,String>();
atributosMap = Util.jsonToMap( atributos );
application.setAttribute("atributos", atributosMap );

// Convertimos las acciones en un objeto LinkedHashMap
LinkedHashMap<String,String> accionesMap = new LinkedHashMap<String,String>();
accionesMap = Util.jsonToMap( acciones );
application.setAttribute("acciones", accionesMap ); 

%>

<!--  Mensaje de estado de la llamada al servidor -->
<div class="${requestScope.tipo_mensaje}">
	${requestScope.mensaje}
</div>

<form action="<%= t.get("servlet") %>" method="post" class="jsListaDatos">
<input type="hidden" name="referer" id="referer" value="<%= request.getRequestURL() + "?" + request.getQueryString() %>"/>
<input type="hidden" name="action" id="action" value=""/>
<div class="lista_datos">
<div class="template header">
	<div class="botonera">
		<!-- Acciones a realizar -->
		<ul class="acciones">		
			<c:forEach items="${acciones}" var="current">			
				<li><input type="button" class="botonAccion"  id="${current.key}" name="${current.key}" value="${current.value}"/></li>			
			</c:forEach>
		</ul>
	</div>
	<!-- Buscador de la lista de datos -->
	<div class="buscador">

		<input 			
			type="text" class="buscar textoBuscar" name="query" 
			placeholder="Buscar ..." 
			value="<c:if test="${requestScope.query != null}">${requestScope.query}</c:if>"
			 onfocus="this.placeholder = ''" 
			  />
		<input class="buscar botonBuscar " id="buscar" name="buscar" type="button" value="Buscar"/>
		<c:choose>
			<c:when test="${requestScope.query != null}"><input class="buscar botonBuscar botonAccion" type="button" name="cancelar_buscar" id="cancelar_buscar" value="Cancelar"/></c:when>
		</c:choose>
	</div>
</div>
<!-- Datos de la tabla desde la base de datos -->
<div class="template datos">
	<table width="100%" cellspacing="0" class="listaDatosTable">
		<thead>
			<!--  Columnas -->
			<tr>
				<th width="30" id="firstHeader"><center><input type="checkbox" class="selectorFilas" /></center></th>
				<c:forEach items="${atributos}" var="current">			
					<th id="${current.key}">${current.value}</th>			
				</c:forEach>
			</tr>
		</thead>
		<tbody>
			<!--  Filas de los datos -->
			<c:forEach var="row" items="${requestScope.items}">
			<tr>
				<td width="30" name="first">
					<center>
						<input type="checkbox" name="elementos" class="elementoFila" value="${row['Codigo']}" /></center></td>
				<c:forEach items="${atributos}" var="current">
								
					<td><c:out value="${row[current.key]}"/></td>		
				</c:forEach>
			</tr>
			</c:forEach>
		</tbody>
	</table>
</div>
</div>
</form>
<script type="text/javascript">
$(document).ready(function(){
    $('.listaDatosTable').DataTable( {
    	"columnDefs": [ {
    		"targets": 0,
    		"orderable": false
    		} ]
    });
    
});
</script>