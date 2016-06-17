package Controlador;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Modelo.Util;

import javax.servlet.ServletException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Servlet implementation class ServletListaDatos
 * 
 * ServletListaDatos es una clase base (superior) de la cual heredan todas las
 * listas de datos.
 * 
 * Una lista de datos ofrece las siguientes funcionalidades:
 * 
 * 1. Una lista de datos (las filas de la tabla).
 * 2. Un campo de busquedas sobre los datos.
 * 3. Unos botones de acción para realizar sobre los elementos de la lista de datos.
 * 4. Un checkbox por cada fila de la lista y uno general para seleccionar o deseleccionar todas las filas.
 * 
 * Este Servlet por defecto realiza 2 acciones:
 * 1. Buscar.
 * 2. Cancelar la busqueda.
 * (A futuro podria realizar mas acciones)
 * 
 * Todos los servlet deben heredar de este para realizar las acciones por defecto de la lista de datos invocando
 * a doPost superior así:
 * 
 * super.doPost( request, response );
 * 
 * 
 */
@WebServlet("/ServletListaDatos")
public class ServletListaDatos extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletListaDatos() {
        super();
        // TODO Auto-generated constructor stub
    }
    
 
    /**
     * Este metodo por defecto lo que hace es regresar al cliente a la página de la cual proviene. 
     * @param request
     * @param response
     */
    public void redirectBack(HttpServletRequest request, HttpServletResponse response)
    {
    	try {
	    	String refererURL = request.getParameter("referer");
	    	String refererURI = Util.getRefererURI(refererURL );
	    	
	    	request.getRequestDispatcher(refererURI).forward(request, response);
    	} 
    	catch (ServletException e) {
    		
    	}
    	catch (IOException e) {
    		
    	}
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
		
        try {
        	switch (request.getParameter("action")){
        		// Permite buscar cadenas de texto en la lista de datos
        		// Toma el parametro query y lo envia al formulario, cuando regresa
        		// la lista de datos (formulario) toma el query y lo agrega a la consulta
        		// mostrando los resultados encontrados.
	        	case "buscar":
	        		request.setAttribute("query", request.getParameter("query") );
	        		this.redirectBack(request, response);
	        		break;	   
	        	// Cancela la busqueda actual en curso.
	        	case "cancelar_buscar": 
	        		request.removeAttribute("query");

	        		this.redirectBack(request, response);
	        		break;
        	}
        } finally {}

	}

}
