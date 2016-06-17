package Controlador;
import java.util.*;
import Dao.ActividadDAO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.ContactoDAO;
import Modelo.Actividad;
import Modelo.Util;
import java.sql.*;

/**
 * Servlet implementation class ServletActividades
 * Extiende o hereda de la clase ServletListaDatos
 */
@WebServlet("/ServletActividades")
public class ServletActividades extends ServletListaDatos {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see ServletListaDatos#ServletListaDatos()
     */
    public ServletActividades() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	         
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
		ActividadDAO accesoDAO = new ActividadDAO();
		accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario"));
		
        try {
        	switch (request.getParameter("action")){
        		// Borrar una actividad de la base de datos
        		// basada en la lista de elementos seleccionados de la lista (ver template.listaDatos)
        		// El nombre del parametro es elementos.
	        	case "borrar":
	        		System.out.println("ServletActividades: borrar");
	        		
	        		String codigos = Util.ArrayStringToCSV( request.getParameterValues("elementos") );
	        		
	        		System.out.println( "Codigos " + codigos );
	        		
	        		// Si la eliminación de las actividades es correcta envie un mensaje de confirmación.
	        		if (accesoDAO.eliminar( codigos )) {
	        			request.setAttribute("mensaje", "Se borraron las actividades seleccionadas exitosamente.");
	        			request.setAttribute("tipo_mensaje", "success");
	        		}
	        		
	        		// Si la eliminación fallo, se envia un mensaje de error como respuesta.
	        		else {
	        			request.setAttribute("mensaje", "Imposible eliminar las actividades seleccionadas, intenta nuevamente.");
	        			request.setAttribute("tipo_mensaje", "error");
	        		}
	        		// En cualquier caso ir a la página donde se encontraba el usuario.
	        		this.redirectBack( request, response);
	        		break;
	        	// Permite actualizar la base de datos.
	        	case "actualizar":
	        		
	        		System.out.println("ServletActividades: actualizar");
	        		
	        		try {
	        			
		        		String[] valores = request.getParameterValues( "elementos" );
		        		// Solo se puede actualizar un elemento (una actividad) a la vez
		        		if (valores.length == 1) {	        			
		        			
		        			Actividad actividad = new Actividad();		        			
		        			actividad = accesoDAO.consultar( Integer.parseInt( valores[0] ));	// El elemento a consultar siempre sera el primero de la lista de valores
		        			
		        			// Se envia la actividad al formulario genda.nuevaActividad
		        			request.setAttribute( "actividad", actividad );	        			
		        			request.setAttribute( "actualizar", true );
		        			
		        			// redirección
		        			request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaActividad" ).forward(request, response);
		        		}
		        		
		        		else {
		        			// Si especifico más de un elemento o ningún elemento se envia un mensaje de error al cliente.
		        			request.setAttribute("mensaje", "Solo puede actualizar una actividad a la vez, por favor seleccione solo una de la lista.");
		        			request.setAttribute("tipo_mensaje", "error" );
		        			this.redirectBack( request, response);
		        		}
	        		}
	        		catch (NullPointerException e) {
	        			// Si ocurre una excepción durante esta solicitud se informa
	        			request.setAttribute("mensaje", "No se puedo redireccionar a la actualización de la actividad, intenta nuevamente.");
	        			request.setAttribute("tipo_mensaje", "error" );
	        			this.redirectBack( request, response);
	        		}
	        		
	        		break;
	        	case "nueva_actividad":
	        		// Redirije al formulario genda.nuevaActividad
	        		System.out.println("ServletActividades: nueva_actividad");
	        		response.sendRedirect("index.jsp?pagina=agenda&seccion=nuevaActividad");
	        		break;
	        	default:
	        		// Llama al metodo doPost de la clase superior para finalizar los casos
	        		// (ver ServletListaDatos), allí le permite procesar las solicitudes de buscar y cancelar una busqueda 
	        		// en la lista de datos.
	        		super.doPost(request, response);   
	        		break;
        	}
        } finally {}
	}

}
