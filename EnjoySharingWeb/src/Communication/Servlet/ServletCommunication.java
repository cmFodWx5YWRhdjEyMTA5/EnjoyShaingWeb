package Communication.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.gson.JsonObject;
import Hibernate.DataObjectClass.DataTable;
import WebProject.Custom.AuthenticationException;
import WebProject.DataObject.ParameterCollection;
import WebProject.DataObject.User;
import WebProject.Servlet.ServletBase;

/*
 * Tipicamente questo tipo di servlet serve per la comunicazione con l'App ed il suo ciclio di vita è
 * - Init
 * - Business
 * - Response
 * - Destroy
 * Quindi non serve la gestione delle sessioni (se non per l'interrogazione DB) perchè una chiamata nasce e muore in un ciclo
 * In sostanza è una ServletBase PARTICOLARE
 */

@WebServlet("/ServletCommunication")
public class ServletCommunication extends ServletBase {
	private static final long serialVersionUID = 1L;
	
	protected String requestType = null;
	protected DataTable dataTable = null;
       
    public ServletCommunication() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doGet(request, response);
		SetUserParams();
		try 
		{
			if(!CheckUser())
			{
				ErrorMessage = "AuthenticationError";
				throw new AuthenticationException();
			}
			if(!CheckVersion())
			{
				ErrorMessage = "VersionError";
				throw new ServletException();
			}
			requestType = GetRequestParameter("RequestType");
			//System.out.println("Richiesta di GET da "+currentUser.getUsername());
			//System.out.println("RequestType "+requestType);
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
			if(!CheckUser())
			{
				ErrorMessage = "AuthenticationError";
				throw new AuthenticationException();
			}
			if(!CheckVersion())
			{
				ErrorMessage = "VersionError";
				throw new ServletException();
			}
			requestType = GetRequestParameter("RequestType");
			//System.out.println("Richiesta di POST da "+currentUser.getUsername());
			//System.out.println("RequestType "+requestType);
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
		// La password arriva già criptata!
//		if(password!=null)
//			password = new BusinessBase().encrypt(password);
		currentUser.setPassword(password);
	}
	
	protected boolean CheckVersion()
	{
		String version = GetRequestParameter("V");
		String currentVersion = businessDB.GetParameter("CurrentVersion");
		if(currentVersion != null && !currentVersion.equals(version))
			return false;
		return true;
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
	
	@Override
	protected void ReturnJson() throws IOException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		jsonReturn = new JsonObject();
		addJsonBoolean("stateResponse",stateResponse);
		addJsonString("message",returnMessage);
		LogInfo();
		response.getWriter().write(jsonReturn.toString());
//		System.out.println("risposta a "+currentUser.getUsername());
//		System.out.println(jsonReturn.toString());
	}

}
