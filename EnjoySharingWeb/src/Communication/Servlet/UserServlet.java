package Communication.Servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Hibernate.HibernateOperation;
import Hibernate.Tables.User;
import WebProject.Business.BusinessMail;
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
			params.Add("Name", GetRequestParameter("Name"));
			params.Add("Surname", GetRequestParameter("Surname"));
			switch(requestType)
			{
				case "UP":  // Update Profile
					ErrorMessage = "UpdateProfileError";
					if(UpdateProfile(params))
						message = "ProfileUpdated";
					else
						throw new Exception();
					break;
				case "RU":  // Register User
					params.Add("Email", GetRequestParameter("RegisterEmail"));
					params.Add("Password", GetRequestParameter("RegisterPassword"));
					ErrorMessage = "RegisterUserError";
					if(RegisterUser(params))
						message = "UserRegistered";
					else
						throw new Exception();
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
	
	protected boolean UpdateProfile(ParameterCollection params)
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
		try
		{
			new HibernateOperation().Update("User",updateParams,whereParams);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	protected boolean RegisterUser(ParameterCollection params)
	{
		if(CheckUserForRegistration(params))
		{
			Long UserId = currentUser.getUserId();
			String Name = params.Get("Name").toString();
			String Surname = params.Get("Surname").toString();
			String Email = params.Get("Email").toString();
			String Password = params.Get("Password").toString();
			String Username = Surname + " " + Name;
			try
			{
				User u = new User(Name, Surname, Username, Email, Password, "", 0, (byte) 1, business.GetNow(), UserId, business.GetNow(), UserId);
				if(new BusinessMail().SendMessage(Email, "Complimenti!", "Testo di prova per registrazione effettuata"))
					new HibernateOperation().Save(u);
				else
				{
					ErrorMessage = "EmailNotValid";
					return false;
				}
				return true;
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}
		else
		{
			ErrorMessage = "UserAlreadyRegistered";
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected boolean CheckUserForRegistration(ParameterCollection params)
	{
		ParameterCollection whereParams = new ParameterCollection();
		String Email = params.Get("Email").toString();
		whereParams.Add("Email", Email);
		try
		{
			List<User> lstUser = (List<User>)new HibernateOperation().GetTableData("User",whereParams);
			return lstUser.isEmpty();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

}
