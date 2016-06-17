package Controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.ActividadDAO;
import Dao.NotaDAO;
import Modelo.Actividad;
import Modelo.Nota;
import Modelo.Usuario;
import Modelo.Util;

/**
 * Servlet implementation class ServletNuevaNota
 * Este servlet puede ser usado para agregar una nueva nota o para actualizar un contacto existente.
 */
@WebServlet("/ServletNuevaNota")
public class ServletNuevaNota extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletNuevaNota() {
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
		// Creamos la nota con los parametros en el request
		Nota nota = new Nota();
		nota.setCodigo( request.getParameter("codigo") );
		nota.setNota( request.getParameter("nota") );
		
		Actividad actividad = new Actividad();
		actividad.setCodigo( request.getParameter("actividad") );
		nota.setActividad( actividad );		
		
		Usuario usuario = new Usuario();
		usuario.setCodigo( session.getAttribute("codigo_usuario").toString() );
		nota.setUsuario( usuario );
		// accion a realizar por el servlet
		String accion = request.getParameter("action");
		System.out.println( "Acción a realizar " + accion );
		NotaDAO accesoDAO = new NotaDAO();

		accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario").toString());

		// Si la acción a realizar es 'validate' es porque se ha solicitado
		// una validación desde AJAX (jquery), se realiza la validación y se entrega 
		// un objeto JSON con los valores de verdad de cada atributo validado.
		// Por ejemplo:
		// "correo" : "false"	// indica que el correo no es valido
		if (accion.equalsIgnoreCase("validate")) {			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write( nota.jsonValidar().toString() );			
		}
		else {
		
			
			// TODO Auto-generated method stub
			
			// validar el contacto es correcta
			if (nota.validar()) {
				
				switch (accion.toLowerCase()) {
				
					// Crear un contacto
					case "crear":
					{
						// Si la insercción de la nota fue correcta se envia un mensaje de confirmación
						if (accesoDAO.insertar( nota )) { 			
							request.setAttribute( "mensaje", "La nota fuea agregada correctamente." );
							request.setAttribute( "tipo_mensaje", "success" );
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=notas").forward(request, response);
						}
						// Si fallo la insercción se envia un mensaje de error
						else {
							request.setAttribute( "mensaje", "Ocurrio un error al crear la nota.");
							request.setAttribute( "tipo_mensaje", "error");
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaNota").forward(request, response);
						}
					}
						break;
						// Actualizar un contacto
					case "actualizar":
					{
						// Si la actualización de la nota fue correcta se envia un mensaje de confirmación
						if (accesoDAO.actualizar( nota )) { 			
							request.setAttribute( "mensaje", "La nota fue actualizada correctamente." );
							request.setAttribute( "tipo_mensaje", "success" );
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=notas").forward(request, response);
						}
						// Si fallo la actualización de la nota se envia un mensaje de error
						else {
							request.setAttribute( "mensaje", "Ocurrio un error al intentar actualizar la nota.");
							request.setAttribute( "tipo_mensaje", "error");
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaNota").forward(request, response);
						}
					}
						break;
				}
		
			}
			else {
				/**
				 * En cualquier caso se envia un mensaje de error y como es el caso de la actualización se regresa la nota
				 * como atributo nota en el request.
				 */
				request.setAttribute( "mensaje", "La información ingresada para la nota no es válida, por favor asegurese de corregirla para continuar.");
				request.setAttribute( "tipo_mensaje", "error");
				request.setAttribute( "nota", nota );
				request.setAttribute( "actualizar", true );
				request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaNota").forward(request, response);
			}
			

			
		}
		
		
		
		
	}

}
