package Controlador;

import Dao.UsuarioDAO;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Modelo.Usuario;

/**
 * Servlet implementation class ServletRecuperarPassword
 */
@WebServlet("/ServletRecuperarPassword")
public class ServletRecuperarPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletRecuperarPassword() {
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
		
		
		UsuarioDAO accesoDAO = new UsuarioDAO();
		
		String nombreUsuario = request.getParameter("nombre_usuario");
		// Si el nombre de usuario existe se puede realizar una recuperación
		if (accesoDAO.nombreUsuarioExiste(nombreUsuario)) {
			// realiza la recuperación
			accesoDAO.recuperarPassword( nombreUsuario );
			// Se redirecciona a la confirmación de la recuperación 
			request.setAttribute("nombre_usuario", request.getParameter("nombre_usuario"));			
			request.getRequestDispatcher("index.jsp?pagina=confirmacionRecuperarPassword").forward(request, response);
		}
		else {
			// Si no es posible realizar la recuperación (solicitud) se muestra un mensaje de error
			request.setAttribute("mensaje", "Error al intentar solicitar una contraseña. El usuario ingresado no existe." );
			request.setAttribute("tipo_mensaje",  "error");
			request.getRequestDispatcher("index.jsp?pagina=olvidoPassword").forward(request, response);
		}
	}

}
