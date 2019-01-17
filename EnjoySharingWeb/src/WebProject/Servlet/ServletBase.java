package WebProject.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.JsonObject;
import WebProject.Business.BusinessBase;
import WebProject.DataObject.User;

@WebServlet("/ServletBase")
public class ServletBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected BusinessBase business;
	protected User currentUser;
	protected JsonObject jsonReturn = null;
	protected String returnMessage = null;
	protected boolean stateResponse = true;
       
    public ServletBase() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response; 
		session = request.getSession();
		business = new BusinessBase();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	public String GetRequestParameter(String pName)
	{
		try
		{
			return request.getParameter(pName);
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	protected void SetUserParams()
	{
		currentUser = new User();
		currentUser.setEmail(GetRequestParameter("Email"));
		String password = GetRequestParameter("Password");
		if(password!=null)
			password = new BusinessBase().encrypt(password);
		currentUser.setPassword(password);
	}
	
	protected boolean CheckUser()
	{
		return true;
	}
	
	protected void addJsonBoolean(String name,boolean value)
	{
		jsonReturn.addProperty(name, value);
	}
	protected void addJsonString(String name,String value)
	{
		jsonReturn.addProperty(name, value);
	}
	
	protected void ReturnJson() throws IOException
	{
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		jsonReturn = new JsonObject();
		addJsonBoolean("stateResponse",stateResponse);
		addJsonString("message",returnMessage);
		response.getWriter().write(jsonReturn.toString());
	}

}
