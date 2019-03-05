package Communication.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Hibernate.DataObjectClass.DataTable;
import WebProject.Custom.AuthenticationException;
import WebProject.DataObject.ParameterCollection;
import WebProject.DataObject.User;
import WebProject.Servlet.ServletBase;

/*
 * Tipicamente questo tipo di servlet serve per la comunicazione con l'App ed il suo ciclio di vita �
 * - Init
 * - Business
 * - Response
 * - Destroy
 * Quindi non serve la gestione delle sessioni (se non per l'interrogazione DB) perch� una chiamata nasce e muore in un ciclo
 * In sostanza � una ServletBase PARTICOLARE
 */

@WebServlet("/ServletCommunication")
public class ServletCommunication extends ServletBase {
	private static final long serialVersionUID = 1L;
	
	protected DataTable dataTable = null;
       
    public ServletCommunication() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
		SetUserParams();
		try 
		{
			if(!CheckUser()) throw new AuthenticationException();
		} 
		catch (AuthenticationException e)
		{
			throw new ServletException(); 
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
		SetUserParams();
		try 
		{
			if(!CheckUser()) throw new AuthenticationException();
		} 
		catch (AuthenticationException e)
		{
			throw new ServletException();
		}
	}
	@Override
	protected void SetUserParams()
	{
		currentUser = new User();
		currentUser.setEmail(GetRequestParameter("Email"));
		String password = GetRequestParameter("Password");
		// La password arriva gi� criptata!
//		if(password!=null)
//			password = new BusinessBase().encrypt(password);
		currentUser.setPassword(password);
	}
	
	protected void PrepareJSON(boolean state, ParameterCollection params)
	{
		stateResponse = state;
		returnMessage = business.CreateJSONObject(params);
	}
	
	protected void PrepareJSON()
	{
		stateResponse = true;
		returnMessage = business.CreateJSONObject(dataTable);
	}
	// Typically used on PostRequests
	protected void PrepareJSON(String message)
	{
		stateResponse = true;
		returnMessage = message;
	}
	
	protected void PrepareErrorJSON()
	{
		stateResponse = false;
		returnMessage = ErrorMessage;
	}

}
