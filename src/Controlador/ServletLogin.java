package Controlador;
import java.sql.*;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Modelo.Usuario;
import Dao.UsuarioDAO;

import javax.servlet.http.*;

import Modelo.Util;

/**
 * Servlet implementation class ServletLogin
 */
@WebServlet(name="ServletLogin", urlPatterns={"/ServletLogin"}) 
public class ServletLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletLogin() {
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
		
		// Acceso a datos
		UsuarioDAO accesoDAO = new UsuarioDAO();
		
		// Usuario que se obtiene como validación del inicio de sesión
		Usuario usuario = null;
		usuario = accesoDAO.validarLogin(request.getParameter("nombre_usuario"), request.getParameter("password"));
		
		// Si el usuario no es nulo despues de realizar la consulta
		// entonces el inicio de sesión es válido.
		if (usuario != null) {
			
			// Inicia la sesión y redirije a la agenda en el inicio.
			session.setAttribute( "codigo_usuario", usuario.getCodigo() );
			session.setAttribute( "nombre_usuario", usuario.getNombreUsuario() );
			response.sendRedirect("index.jsp?pagina=agenda&section=inicio");
		}
		else {
	
			// No puede iniciar la sesión y se redirije de nuevo al formulario de inicio de sesión.
			request.setAttribute( "mensaje", "Imposible iniciar sesión, intenta nuevamente." );
			request.setAttribute( "tipo_mensaje", "error" );
			request.getRequestDispatcher("index.jsp?pagina=iniciarSesion").forward(request, response);
		}

	}

}
