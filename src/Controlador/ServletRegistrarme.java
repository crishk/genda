package Controlador;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.UsuarioDAO;
import Dao.RegistroUsuarioDAO;
import Modelo.Usuario;

/**
 * Servlet implementation class ServletRegistrarme
 */
@WebServlet(name="ServletRegistrarme", urlPatterns={"/ServletRegistrarme"}) 
public class ServletRegistrarme extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletRegistrarme() {
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
		// Creamos el usuario con los parametros en el request
		Usuario usuario = new Usuario();
		usuario.setCodigo( request.getParameter("codigo") );
		usuario.setNombres( request.getParameter("nombres") );
		usuario.setApellidos( request.getParameter("apellidos") );
		usuario.setTelefono( request.getParameter("telefono") );		
		usuario.setDireccion( request.getParameter("direccion") );
		usuario.setCorreo( request.getParameter("correo") );
		usuario.setNombreUsuario( request.getParameter("nombre_usuario") );
		usuario.setPassword( request.getParameter("password") );
		
		// accion a realizar por el servlet
		String accion = request.getParameter("action");
		
		// Si la acción a realizar es 'validate' es porque se ha solicitado
		// una validación desde AJAX (jquery), se realiza la validación y se entrega 
		// un objeto JSON con los valores de verdad de cada atributo validado.
		// Por ejemplo:
		// "correo" : "false"	// indica que el correo no es valido
		if (accion.equalsIgnoreCase("validate")) {			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write( usuario.jsonValidar().toString() );			
		}
		else {
		
			
			// TODO Auto-generated method stub
			
			
			// valida si el usuario es correcto
			if (usuario.validar()) {
			
				RegistroUsuarioDAO accesoDAO = new RegistroUsuarioDAO();
				// Inserta el usuario de la base de datos
				if (accesoDAO.insertar( usuario )) { 	
					// Si el registro fue satisfactorio envia un mensajed de confirmación y se redirije directamente
					// a la cuenta de usuario.
					request.setAttribute( "mensaje", "Su registro ha sido exitoso, bienvenido a su nueva agenda." );
					request.setAttribute( "tipo_mensaje", "success" );
					request.getRequestDispatcher("ServletLogin").forward(request,response);
				}
				else {
					// Si el registro no fue exitoso se envia un mensaje de error.
					request.setAttribute( "mensaje", "Imposible registrarse, ocurrio un error. Intenta nuevamente. <br/>" );
					request.setAttribute( "tipo_mensaje", "error" );	
					request.getRequestDispatcher("index.jsp?pagina=registrarme").forward(request,response);
				}
			}
			else {
				// En cualquier caso se envia un mensaje de errory se redirije al formulario de nuevo.
				request.setAttribute( "mensaje", "La información ingresada para registrarse es inválida, por favor asegurese de corregirla para continuar.");
				request.setAttribute( "tipo_mensaje", "error");
				request.getRequestDispatcher("index.jsp?pagina=registrarme").forward(request, response);
			}
				
	
			
		}
		
	
		
	}

}
