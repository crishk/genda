package Controlador;

import java.io.IOException;
import Modelo.Usuario;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Dao.UsuarioDAO;

/**
 * Servlet implementation class ServletIngresarNuevoPassword
 */
@WebServlet("/ServletIngresarNuevoPassword")
public class ServletIngresarNuevoPassword extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletIngresarNuevoPassword() {
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
		
		// sesión
		HttpSession session = request.getSession(true);
		
		// Acceso a datos
		UsuarioDAO accesoDAO = new UsuarioDAO();
			
		// Parametros del formulario para recuperar el password
		String nuevoPassword = request.getParameter("nuevo_password");		
		String codigoRecuperacion = request.getParameter("codigo_recuperacion");
		String nombreUsuario = request.getParameter("nombre_usuario");
		
		
		// Reestablece la contraseña de la cuenta de usuario invocando el método reestablecerPassword
		accesoDAO.reestablecerPassword( nuevoPassword, codigoRecuperacion, nombreUsuario );
		
		// Una vez la contraseña ha sido reestablecida se redirecciona al inicio de sesión
		request.getRequestDispatcher("index.jsp?pagina=iniciarSesion" ).forward(request, response);

	}

}
