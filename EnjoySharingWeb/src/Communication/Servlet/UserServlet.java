package Communication.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import WebProject.DataObject.ParameterCollection;

@WebServlet("/UserServlet")
public class UserServlet extends ServletCommunication {
	private static final long serialVersionUID = 1L;
       
    public UserServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			super.doGet(request, response);
			ParameterCollection params = new ParameterCollection();
			params.Add("UserId", currentUser.getUserId());
			params.Add("UserName", currentUser.getUsername());
			params.Add("Name", currentUser.getName());
			params.Add("Surname", currentUser.getSurname());
			PrepareJSON(true, params);
		}
		catch(Exception e)
		{
			PrepareErrorJSON();
		}
		ReturnJson();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		super.doPost(request, response);
	}

}
