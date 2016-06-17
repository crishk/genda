package Controlador;

import Dao.ContactoDAO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Modelo.Contacto;
import Modelo.Util;

/**
 * Servlet implementation class ServletContactos
 * Extiende o hereda de la clase ServletListaDatos
 */
@WebServlet("/ServletContactos")
public class ServletContactos extends ServletListaDatos {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see ServletListaDatos#ServletListaDatos()
     */
    public ServletContactos() {
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
		
		// Establece el parametro global a nivel DAO de codigo de usuario para que pueda ser usado 
		// por los diferentes objetos y metodos de acceso a datos en las diferentes consultas.
		ContactoDAO accesoDAO = new ContactoDAO();
		accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario"));
		
        try {
        	
        	switch (request.getParameter("action")){
	        	// Borrar un contacto de la base de datos
	    		// basada en la lista de elementos seleccionados de la lista (ver template.listaDatos)
	    		// El nombre del parametro es elementos.
	        	case "borrar":
	        		String codigos = Util.ArrayStringToCSV( request.getParameterValues("elementos") );
	        		// Si la eliminación de los contactos es correcta envie un mensaje de confirmación.
	        		if (accesoDAO.eliminar( codigos )) {
	        			request.setAttribute("mensaje", "Se borraron los contactos seleccionados exitosamente.");
	        			request.setAttribute("tipo_mensaje", "success");
	        		}
	        		// Si la eliminación fallo, se envia un mensaje de error como respuesta.
	        		else {
	        			request.setAttribute("mensaje", "Imposible eliminar los contactos seleccionados, intenta nuevamente.");
	        			request.setAttribute("tipo_mensaje", "error");
	        		}
	        		// En cualquier caso ir a la página donde se encontraba el usuario.
	        		this.redirectBack( request, response);
	        		break;
	        	// Permite actualizar la base de datos.
	        	case "actualizar":
	        		try {
		        		String[] valores = request.getParameterValues( "elementos" );
		        		// Solo se puede actualizar un elemento (un contacto) a la vez
		        		if (valores.length == 1) {	        			
		        			Contacto contacto = new Contacto();
		        			contacto = accesoDAO.consultar( Integer.parseInt( valores[0] )); // El elemento a consultar siempre sera el primero de la lista de valores
		        			
		        			// Se envia el contacto al formulario genda.nuevoContacto
		        			request.setAttribute( "contacto", contacto );
		        			request.setAttribute( "actualizar", true );
		        			
		        			// redirección
		        			request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevoContacto" ).forward(request, response);
		        		}
		        		else {
		        			// Si especifico más de un elemento o ningún elemento se envia un mensaje de error al cliente.
		        			request.setAttribute("mensaje", "Solo puede actualizar un contacto a la vez, por favor seleccione solo uno de la lista.");
		        			request.setAttribute("tipo_mensaje", "error" );
		        			this.redirectBack( request, response);
		        		}
		        		}
		        		catch (NullPointerException e) {
		        			// Si ocurre una excepción durante esta solicitud se informa
		        			request.setAttribute("mensaje", "No se puedo redireccionar a la actualización del contacto, intenta nuevamente.");
		        			request.setAttribute("tipo_mensaje", "error" );
		        			this.redirectBack( request, response);
		        		}
	        		this.redirectBack( request, response);
	        		break;
	        	case "nuevo_contacto":
	        		// Redirije al formulario genda.nuevoContacto
	        		response.sendRedirect("index.jsp?pagina=agenda&seccion=nuevoContacto");	    
	        		break;
	        	default:
	        		// Llama al metodo doPost de la clase superior para finalizar los casos
	        		// (ver ServletListaDatos), allí le permite procesar las solicitudes de buscar y cancelar una busqueda 
	        		// en la lista de datos.
	        		super.doPost(request, response);   
	        		break;
        	}
        } finally {   
        
        }
		
	}

}
