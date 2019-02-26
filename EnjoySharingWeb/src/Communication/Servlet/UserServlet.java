package Communication.Servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Hibernate.HibernateOperation;
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
		try
		{
			super.doPost(request, response);
			String message=null;
			String requestType = GetRequestParameter("RequestType");
			ParameterCollection params = new ParameterCollection();
			params.Add("UserId", currentUser.getUserId());
			params.Add("Name", GetRequestParameter("Name"));
			params.Add("Surname", GetRequestParameter("Surname"));
			switch(requestType)
			{
				case "UP":  // Update Profile
					ErrorMessage = "UpdateProfileError";
					UpdateProfile(params);
					message = "ProfileUpdated";
					break;
				default:
					ErrorMessage = "WrongRequest";
					throw new Exception();
			}
			PrepareJSON(message);
		}
		catch(Exception e)
		{
			PrepareErrorJSON();
		}
		ReturnJson();
	}
	
	protected void UpdateProfile(ParameterCollection params)
	{
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		Long UserId = currentUser.getUserId();
		String Name = params.Get("Name").toString();
		String Surname = params.Get("Surname").toString();
		whereParams.Add("UserId", UserId);
		updateParams.Add("Name", Name);
		updateParams.Add("Surname", Surname);
		updateParams.Add("UserName", Surname+" "+Name);
		updateParams.Add("UpdateDate", business.GetNow());
		updateParams.Add("UpdateUser", UserId);
		new HibernateOperation().Update("User",updateParams,whereParams);
	}

}
