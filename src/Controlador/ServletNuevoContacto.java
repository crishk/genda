package Controlador;

import java.util.*;


import Modelo.Contacto;
import Modelo.Usuario;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.ContactoDAO;

/**
 * Servlet implementation class ServletNuevoContacto
 * 
 *  Este servlet puede ser usado para agregar un nuevo contacto o para actualizar un contacto existente.
 * 
 */
@WebServlet("/ServletNuevoContacto")
public class ServletNuevoContacto extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletNuevoContacto() {
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
		
	
		
		HttpSession session = request.getSession(true);
		
		// Creamos el contacto con los parametros en el request
		Contacto contacto = new Contacto();
		contacto.setCodigo( request.getParameter("codigo_usuario") );
		contacto.setNombres( request.getParameter("nombres") );
		contacto.setApellidos( request.getParameter("apellidos") );
		contacto.setNombrePersonalizado( request.getParameter("nombre_personalizado") );
		contacto.validarNombrePersonalizado(contacto.getNombrePersonalizado());
		contacto.setDireccion( request.getParameter("direccion") );
		contacto.setTelefono( request.getParameter("telefono") );
		contacto.setCelular( request.getParameter("celular") );		
		contacto.setCorreo( request.getParameter("correo") );
		
		Usuario usuario = new Usuario();
		usuario.setCodigo( session.getAttribute("codigo_usuario").toString() );
		contacto.setUsuario( usuario );
		
		// accion a realizar por el servlet
		String accion = request.getParameter("action");
		
		ContactoDAO accesoDAO = new ContactoDAO();
		accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario").toString() );
		
		// Si la acción a realizar es 'validate' es porque se ha solicitado
		// una validación desde AJAX (jquery), se realiza la validación y se entrega 
		// un objeto JSON con los valores de verdad de cada atributo validado.
		// Por ejemplo:
		// "correo" : "false"	// indica que el correo no es valido
		if (accion.equalsIgnoreCase("validate")) {			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write( contacto.jsonValidar().toString() );			
		}
		else {
		
			
			// TODO Auto-generated method stub
			
			
			// validar el contacto es correcta
			if (contacto.validar()) {
				
				switch (accion.toLowerCase()) {
					// Crear un contacto
					case "crear":
					{
						// Si la insercción del contacto fue correcta se envia un mensaje de confirmación
						if (accesoDAO.insertar( contacto )) { 			
							request.setAttribute( "mensaje", "El contacto fue agregado correctamente." );
							request.setAttribute( "tipo_mensaje", "success" );
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=contactos").forward(request, response);
						}
						// Si fallo la insercción se envia un mensaje de error
						else {
							request.setAttribute( "mensaje", "Ocurrio un error al crear el contacto.");
							request.setAttribute( "tipo_mensaje", "error");
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevoContacto").forward(request, response);
						}
					}
						break;
					// Actualizar un contacto
					case "actualizar":
					{
						// Si la actualización del contacto fue correcta se envia un mensaje de confirmación
						if (accesoDAO.actualizar( contacto )) { 			
							request.setAttribute( "mensaje", "El contacto fue actualizado correctamente." );
							request.setAttribute( "tipo_mensaje", "success" );
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=contactos").forward(request, response);
						}
						// Si fallo la actualización del contacto se envia un mensaje de error
						else {
							request.setAttribute( "mensaje", "Ocurrio un error al intentar actualizar el contacto.");
							request.setAttribute( "tipo_mensaje", "error");
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevoContacto").forward(request, response);
						}
					}
						break;
				}
		
			}
			else {
				/**
				 * En cualquier caso se envia un mensaje de error y como es el caso de la actualización se regresa el contacto
				 * como atributo contacto.
				 */
				request.setAttribute( "mensaje", "La información ingresada para el contacto no es válida, por favor asegurese de corregirla para continuar.");
				request.setAttribute( "tipo_mensaje", "error");
				request.setAttribute( "contacto", contacto );
				request.setAttribute( "actualizar", true );
				request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevoContacto").forward(request, response);
			}
				
	
			
		}

	}

}

