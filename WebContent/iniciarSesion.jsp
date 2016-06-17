<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>  
<form action="ServletLogin" method="post">

	<h2>Iniciar sesión</h2>
	
	<c:if test="${requestScope.mensaje != null}">  
		<div class="error">
			${requestScope.mensaje} 
		</div> 
	</c:if>
	
	<label>Nombre de usuario</label>
	<input type="text" value="" name="nombre_usuario" />
	
	<label>Password</label>
	<input type="password" value="" name="password" />
	
	<input type="hidden" name="action" value="login"/>
	<input type="submit" value="Iniciar sesión" />
	
	<ul>
		<li>	
			<a href="index.jsp?pagina=olvidoPassword">¿Olvido su contraseña?</a></li>
		<li>
			<a href="index.jsp?pagina=registrarme">Registrarme</a>
		</li>
	</ul>
</form>

