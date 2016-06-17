package Controlador;

import Modelo.Actividad;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

import Dao.ActividadDAO;

import Modelo.Actividad;
import Modelo.Usuario;
import Modelo.Util;

import java.sql.Date;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;  
//import org.apache.commons.lang3.time.*;

/**
 * Servlet implementation class ServletNuevaActividad
 * 
 * Este servlet puede ser usado para agregar una nueva actividad o para actualizar una actividad existente.
 */
@WebServlet("/ServletNuevaActividad")
public class ServletNuevaActividad extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletNuevaActividad() {
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

		// Creamos la actividad con los parametros en el request
		Actividad actividad = new Actividad();
		actividad.setCodigo( request.getParameter("codigo") );
		actividad.setNombre( request.getParameter("nombre_actividad") );
		actividad.setDescripcion( request.getParameter("descripcion") );
		actividad.setTipoActividad( request.getParameter("tipo_actividad") );
		
		// Importante: las fechas de inicio y fin 
		String fechaInicio = request.getParameter("fecha_inicio");
		fechaInicio = fechaInicio.replaceAll("T",  " ");

		String fechaFin = request.getParameter("fecha_fin");
		fechaFin = fechaFin.replaceAll("T",  " ");
		
		// Debug
		System.out.println( "Fecha de inicio " + fechaInicio );
		System.out.println( "Fecha de fin " + fechaFin );
		
		actividad.setInicio( Util.formatearFecha( fechaInicio )  );
		actividad.setFin( Util.formatearFecha( fechaFin )  );

		// Establecemos el usuario de la actividad
		Usuario usuario = new Usuario();
		usuario.setCodigo( session.getAttribute("codigo_usuario").toString() );
		actividad.setUsuario( usuario );
		
		
		String accion = request.getParameter("action");
		
		ActividadDAO accesoDAO = new ActividadDAO();

		accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario").toString() );

		// Si la acción a realizar es 'validate' es porque se ha solicitado
		// una validación desde AJAX (jquery), se realiza la validación y se entrega 
		// un objeto JSON con los valores de verdad de cada atributo validado.
		// Por ejemplo:
		// "correo" : "false"	// indica que el correo no es valido
		if (accion.equalsIgnoreCase("validate")) {			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write( actividad.jsonValidar().toString() );			
		}
		else {
			// validar la actividad es correcta
			if (actividad.validar()) {
				
				switch (accion.toLowerCase()) {
					// Crear una actividad
					case "crear":
					{
						System.out.println("ServletNuevaActividad: crear");
						// Si la insercción de la actividad fue correcta se envia un mensaje de confirmación
						if (accesoDAO.insertar( actividad )) { 			
							request.setAttribute( "mensaje", "La actividad fuea agregada correctamente." );
							request.setAttribute( "tipo_mensaje", "success" );
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=actividades").forward(request, response);
						}
						// Si fallo la insercción se envia un mensaje de error
						else {
							request.setAttribute( "mensaje", "Ocurrio un error al crear la actividad.");
							request.setAttribute( "tipo_mensaje", "error");
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaActividad").forward(request, response);
						}
					}
						break;
					// Actualizar la actividad
					case "actualizar":
					{
						System.out.println("ServletNuevaActividad: actualizar");
						// Si la actualización de la actividad fue correcta se envia un mensaje de confirmación
						if (accesoDAO.actualizar( actividad )) { 			
							request.setAttribute( "mensaje", "La actividad fue actualizada correctamente." );
							request.setAttribute( "tipo_mensaje", "success" );
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=actividades").forward(request, response);
						}
						// Si fallo la actualización de la actividad se envia un mensaje de error
						else {
							request.setAttribute( "mensaje", "Ocurrio un error al intentar actualizar la actividad.");
							request.setAttribute( "tipo_mensaje", "error");
							request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaActividad").forward(request, response);
						}
					}
						break;
				}
		
			}
			else {
				/**
				 * En cualquier caso se envia un mensaje de error y como es el caso de la actualización se regresa la actividad
				 * como atributo actividad.
				 */
				request.setAttribute( "mensaje", "La información ingresada para la actividad no es válida, por favor asegurese de corregirla para continuar.");
				request.setAttribute( "tipo_mensaje", "error");
				request.setAttribute( "actividad", actividad );
				request.setAttribute( "actualizar", true );
				request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=nuevaActividad").forward(request, response);
			}
			

			
		}

		
	}

}
