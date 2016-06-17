package Controlador;

import Dao.NotaDAO;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.ContactoDAO;
import Modelo.Nota;
import Modelo.Util;

/**
 * Servlet implementation class ServletNotas
 */
@WebServlet("/ServletNotas")
public class ServletNotas extends ServletListaDatos {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see ServletListaDatos#ServletListaDatos()
     */
    public ServletNotas() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// TODO Auto-generated method stub
		HttpSession session = request.getSession(true);
		NotaDAO accesoDAO = new NotaDAO();
		accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario"));
		
        try {
        	
        	switch (request.getParameter("action")){
	        	// Borrar una nota de la base de datos
	    		// basada en la lista de elementos seleccionados de la lista (ver template.listaDatos)
	    		// El nombre del parametro es elementos.
	        	case "borrar":
	          		String codigos = Util.ArrayStringToCSV( request.getParameterValues("elementos") );
	          		// Si la eliminación de las notas es correcta envie un mensaje de confirmación.
	        		if (accesoDAO.eliminar( codigos )) {
	        			request.setAttribute("mensaje", "Se borraron las notas seleccionadas exitosamente.");
	        			request.setAttribute("tipo_mensaje", "success");
	        		}
	        		
	        		// Si la eliminación fallo, se envia un mensaje de error como respuesta.
	        		else {
	        			request.setAttribute("mensaje", "Imposible eliminar los notas seleccionadas, intenta nuevamente.");
	        			request.setAttribute("tipo_mensaje", "error");
	        		}
	        		
	        		// En cualquier caso ir a la página donde se encontraba el usuario.
	        		this.redirectBack( request, response);
	        		break;
	        	// Permite actualizar la base de datos
	        	case "actualizar":
	        		try {
		        		String[] valores = request.getParameterValues( "elementos" );
		        		// Solo se puede actualizar un elemento (una nota) a la vez
		        		if (valores.length == 1) {	        			
		        			Nota nota = new Nota();
		        			nota = accesoDAO.consultar( Integer.parseInt( valores[0] ));
		        			
		        			// Se envia la nota al formulario genda.nuevaNota
		        			request.setAttribute( "nota", nota );
		        			request.setAttribute( "actualizar", true );
		        			request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaNota" ).forward(request, response);
		        		}
		        		else {
		        			// Si especifico más de un elemento o ningún elemento se envia un mensaje de error al cliente.
		        			request.setAttribute("mensaje", "Solo puede actualizar una nota a la vez, por favor seleccione solo una de la lista.");
		        			request.setAttribute("tipo_mensaje", "error" );
		        			this.redirectBack( request, response);
		        		}
		        		}
		        		catch (NullPointerException e) {
		        			// Si ocurre una excepción durante esta solicitud se informa
		        			request.setAttribute("mensaje", "No se puedo redireccionar a la actualización de la nota, intenta nuevamente.");
		        			request.setAttribute("tipo_mensaje", "error" );
		        			this.redirectBack( request, response);
		        		}
	        		break;
	        	case "nueva_nota":
	        		// Redirije al formulario genda.nuevaNota
	        		response.sendRedirect("index.jsp?pagina=agenda&seccion=nuevaNota");
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
