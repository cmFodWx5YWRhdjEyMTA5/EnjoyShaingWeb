package Communication.Servlet;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import Hibernate.HibernateOperation;
import Hibernate.DataObjectClass.DataTable;
import Hibernate.DataObjectClass.HomeEvent;
import Hibernate.DataObjectClass.UserFriend;
import Hibernate.Tables.Photo;
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
			String message=null;
			String requestType = GetRequestParameter("RequestType");
			ParameterCollection params = new ParameterCollection();
			switch(requestType)
			{
				case "LI":  // Log In
					params.Add("UserId", currentUser.getUserId());
					params.Add("UserName", currentUser.getUsername());
					params.Add("Name", currentUser.getName());
					params.Add("Surname", currentUser.getSurname());
					params.Add("ProfileImage", GetProfileImage(currentUser.getProfilePhotoId()));
					message = business.CreateJSONObject(params);
					PrepareJSON(message);
					break;
				case "GI":  // Get Image
					params.Add("UserId", GetRequestParameter("UserIdImage"));
					ErrorMessage = "GetImageError";
					params.Add("Photo", GetProfileImage(GetUserImageId(params)));
					message = business.CreateJSONObject(params);
					PrepareJSON(message);
					break;
				case "GF":  // Get Friends
					params.Add("UserId", currentUser.getUserId());
					ErrorMessage = "GetFriendsError";
					LoadFriends(params);
					PrepareJSON();
					break;
				default:
					ErrorMessage = "WrongRequest";
					throw new Exception();
			}
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
			switch(requestType)
			{
				case "UP":  // Update Profile
					params.Add("Name", GetRequestParameter("Name"));
					params.Add("Surname", GetRequestParameter("Surname"));
					ErrorMessage = "UpdateProfileError";
					if(UpdateProfile(params))
						message = "ProfileUpdated";
					else
						throw new Exception();
					break;
				case "RU":  // Register User
					params.Add("Name", GetRequestParameter("Name"));
					params.Add("Surname", GetRequestParameter("Surname"));
					params.Add("Email", GetRequestParameter("RegisterEmail"));
					params.Add("Password", GetRequestParameter("RegisterPassword"));
					ErrorMessage = "RegisterUserError";
					if(RegisterUser(params))
						message = "UserRegistered";
					else
						throw new Exception();
					break;
				case "SP":  // Save Photo
					params.Add("Photo", GetRequestParameter("Photo"));
					ErrorMessage = "SavePhotoError";
					if(SavePhoto(params))
						message = "PhotoSaved";
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
	
	@SuppressWarnings("unchecked")
	protected String GetProfileImage(Integer PhotoId)
	{
		ParameterCollection whereParams = new ParameterCollection();
		whereParams.Add("PhotoId", PhotoId);
		List<Photo> listPhoto = (List<Photo>) new HibernateOperation().GetTableData("Photo",whereParams);
		if(listPhoto != null)
		{
			for(Photo P : listPhoto)
			{
				return business.ArrayToBase64(P.getPhoto());
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	protected Integer GetUserImageId(ParameterCollection params)
	{
		ParameterCollection whereParams = new ParameterCollection();
		Long UserId = Long.valueOf(params.Get("UserId").toString());
		whereParams.Add("UserId", UserId);
		List<User> listUser = (List<User>) new HibernateOperation().GetTableData("User",whereParams);
		if(listUser != null)
		{
			for(User U : listUser)
			{
				return U.getProfilePhotoId();
			}
		}
		return 0;
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
				User u = new User(Name, Surname, Username, Email, Password, "", 0, (byte) 1, business.GetNow(), UserId, business.GetNow(), UserId, null);
				if(new BusinessMail().SendMessage(Email, "Complimenti!", Username+" ti sei registrato correttamente! Ora puoi creare eventi o chiedere di partecipare a quelli dei tuoi amici!"))
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
	
	protected boolean SavePhoto(ParameterCollection params)
	{
		String strPhoto = params.Get("Photo").toString();
		byte[] Photo = business.StringBase64ToByteArray(strPhoto);//strPhoto.getBytes();//business.String64ToByteArray(strPhoto);
		if(currentUser.getProfilePhotoId() != null)
		{
			return UpdateProfilePhotoId(Photo);
		}
		Long PhotoId = NewProfilePhotoId(Photo);
		if(PhotoId == null) return false;
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		Long UserId = currentUser.getUserId();
		whereParams.Add("UserId", UserId);
		updateParams.Add("ProfilePhotoId", PhotoId.intValue());
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
	
	protected Long NewProfilePhotoId(byte[] photo)
	{
		Long UserId = currentUser.getUserId();
		Date now = business.GetNow();
		Photo p = new Photo(photo, (byte) 1, now, UserId, now, UserId);
		if(new HibernateOperation().Save(p))
			return p.getPhotoId();
		else
			return null;
	}
	
	protected boolean UpdateProfilePhotoId(byte[] photo)
	{
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		Integer PhotoId = currentUser.getProfilePhotoId();
		Long UserId = currentUser.getUserId();
		whereParams.Add("PhotoId", PhotoId);
		updateParams.Add("Photo", photo);
		updateParams.Add("UpdateDate", business.GetNow());
		updateParams.Add("UpdateUser", UserId);
		try
		{
			new HibernateOperation().Update("Photo",updateParams,whereParams);
			return true;
		}
		catch(Exception e)
		{
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected void LoadFriends(ParameterCollection params)
	{
		List<UserFriend> lstRet = (List<UserFriend>) ExecuteSP("GetFriends",params,UserFriend.class);
		if(lstRet != null)
		{
			dataTable = new DataTable(lstRet);
		}
		else
			dataTable = null;
	}

}
