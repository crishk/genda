package Controlador;
import Modelo.Resumen;
import Modelo.Actividad;
import Dao.ActividadDAO;
import Modelo.Util;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class ServletResumen
 */
@WebServlet("/ServletResumen")
public class ServletResumen extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletResumen() {
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
		
		// Se crea al resumen
		Resumen resumen = new Resumen();
		
		String fechaInicio = request.getParameter("inicio");
		String fechaFin = request.getParameter("fin");
		
		// ¿Desde el inicio de los tiempos?
		String sinInicio = new String();		
		if (request.getParameterMap().containsKey("sin_inicio"))			
			sinInicio = request.getParameter("sin_inicio");
		else
			sinInicio = null;
		
		// ¿Hasta el fin de los tiempos?
		String sinFin = new String();
		if (request.getParameterMap().containsKey("sin_fin"))			
			sinFin = request.getParameter("sin_fin");
		else
			sinFin = null;
		
		System.out.println( "Valor de inicio " + sinInicio );
						
		fechaInicio = fechaInicio.replaceAll("T",  " ");
		fechaFin = fechaFin.replaceAll("T",  " ");		
		
		resumen.setInicio( Util.formatearFecha( fechaInicio ) );
		resumen.setFin( Util.formatearFecha( fechaFin ) );
				
		ActividadDAO accesoDAO = new ActividadDAO();		
		accesoDAO.setParametro("codigo_usuario", session.getAttribute("codigo_usuario") );
		
		ArrayList<ArrayList<Actividad>> actividades = new ArrayList<ArrayList<Actividad>>();
		
		// Quiere decir que especificaron fecha de inicio y fecha de finalización
		if (sinFin == null && sinInicio == null)
			actividades = accesoDAO.consultarActividadesResumen( resumen.getInicio(), resumen.getFin() );
		
		// Quiere decir que especificaron una fecha de finalización desde el inicio de los tiempos
		if (sinFin == null && sinInicio != null)
			actividades = accesoDAO.consultarActividadesResumen( null, resumen.getFin() );
		
		// Quiere decir que especificaron desde una fecha de inicio hasta el fin de los tiempos.
		if (sinFin != null && sinInicio == null)
			actividades = accesoDAO.consultarActividadesResumen( resumen.getInicio(), null );
		
		// Quiere decir que especificaron desde el inicio de los tiempos hasta el fin de los tiempos
		if (sinFin != null && sinInicio != null)
			actividades = accesoDAO.consultarActividadesResumen(  null,  null );
		
		// Se envian los parametros como atributos
		// de nuevo al formulario para que sea procesado y liste el resumen corrspondiente
		
		request.setAttribute("fecha_inicio", request.getParameter( "inicio") );
		request.setAttribute("fecha_fin", request.getParameter( "fin") );			
		request.setAttribute("sin_inicio", request.getParameter("sin_inicio"));
		request.setAttribute("sin_fin", request.getParameter("sin_fin"));
		
		
		// Si la validación del resumen no es correcta (por ejemplo fechas invalidas)
		// Se informa con un mensaje de error.
		if (!resumen.validar()) { 		
			request.setAttribute( "mensaje", "Las fechas seleccionadas para generar el resumen no son válidas." );
			request.setAttribute( "tipo_mensaje", "error" );			
		}
		else {
			request.setAttribute("actividades", actividades );
		}
		
		// Se redirecciona al resumen
		request.getRequestDispatcher("index.jsp?pagina=agenda&seccion=resumen").forward(request, response);				
		
	}

}

