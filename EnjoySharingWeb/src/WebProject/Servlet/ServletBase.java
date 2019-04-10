package WebProject.Servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import com.google.gson.JsonObject;
import Hibernate.HibernateOperation;
import Hibernate.DataObjectClass.DataTable;
import Hibernate.DataObjectClass.StandardResult;
import Hibernate.Tables.Loginfo;
import WebProject.Business.BusinessBase;
import WebProject.Business.BusinessDB;
import WebProject.DataObject.ParameterCollection;
import WebProject.DataObject.User;

@WebServlet("/ServletBase")
public class ServletBase extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected HttpSession session;
	protected BusinessBase business;
	protected BusinessDB businessDB;
	protected User currentUser;
	protected String servletName;
	protected JsonObject jsonReturn = null;
	protected String returnMessage = null;
	protected boolean stateResponse = true;
	protected String ErrorMessage = null;
	protected ParameterCollection requestParams;
       
    public ServletBase() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.request = request;
		this.response = response; 
		session = request.getSession();
		business = new BusinessBase();
		businessDB = new BusinessDB();
		LoadRequestParams();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	this.request = request;
		this.response = response; 
		session = request.getSession();
		business = new BusinessBase();
		businessDB = new BusinessDB();
		LoadRequestParams();
	}
	
	protected void LoadRequestParams()
	{
		requestParams = new ParameterCollection();
		
		Enumeration<String> parameterNames = request.getParameterNames();
		 
        while (parameterNames.hasMoreElements()) {
 
            String paramName = parameterNames.nextElement();
 
            String[] paramValues = request.getParameterValues(paramName);
            for (int i = 0; i < paramValues.length; i++) {
                String paramValue = paramValues[i];
                //System.out.println(paramName +":"+paramValue);
                requestParams.Add(paramName, paramValue);
            }
 
        }
	}
	
	public String GetRequestParameter(String pName)
	{
		try
		{
			return requestParams.Get(pName).toString();
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
	
	@SuppressWarnings("unchecked")
	protected boolean CheckUser()
	{
		boolean usernameFound = false;
		ErrorMessage = null;
		ParameterCollection whereParams = new ParameterCollection();
		whereParams.Add("email", currentUser.getEmail());
		List<Hibernate.Tables.User> listUser = (List<Hibernate.Tables.User>) new HibernateOperation().GetTableData("User",whereParams);
		if(listUser != null)
		{
			List<Hibernate.Tables.User> listUserPassword;
			for(Hibernate.Tables.User U : listUser)
			{
				usernameFound = true;
				// non posso controllare qui la password perchè ci sono caratteri speciali, quindi eseguo la query sulla tabella mettendo la password in input
				whereParams = new ParameterCollection();
				whereParams.Add("email", U.getEmail());
				whereParams.Add("password", currentUser.getPassword());
				listUserPassword = (List<Hibernate.Tables.User>) new HibernateOperation().GetTableData("User",whereParams);
				if(listUserPassword != null && !listUserPassword.isEmpty())
				{
					currentUser.setUserId(U.getUserId());
					currentUser.setUsername(U.getUserName());
					currentUser.setName(U.getName());
					currentUser.setSurname(U.getSurname());
					currentUser.setProfilePhotoId(U.getProfilePhotoId());
					return true;
				}
			}
		}
		if(!usernameFound)  // User non trovato -> non registrato
			ErrorMessage = "UserNotRegister";
		else  // Email o Password errata
			ErrorMessage = "UserWrong";
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	protected List<?> ExecuteSP(String storedName, ParameterCollection params,Class resultClass)
	{
		return new HibernateOperation().ExecuteSPQuery(storedName,params,resultClass);
	}
	
	protected Object ExecuteSPCheck(String storedName, ParameterCollection params)
	{
		List<?> lstRet = new HibernateOperation().ExecuteSPQuery(storedName,params,StandardResult.class);
		DataTable dt = new DataTable(lstRet);
		try
		{
			return dt.GetCell(0, "ReturnValue");
		}
		catch(Exception e)
		{
			return null;
		}
	}
	
	protected void addJsonBoolean(String name,boolean value)
	{
		jsonReturn.addProperty(name, value);
	}
	protected void addJsonString(String name,String value)
	{
		jsonReturn.addProperty(name, value);
	}
	
	protected void ReturnJson() throws IOException { }
	
	protected void LogInfo()
	{
		if(currentUser.getUserId() == null) return;
		String strRequestParams = business.CreateJSONObject(requestParams);
		Loginfo li = new Loginfo(currentUser.getUserId(), servletName, strRequestParams, jsonReturn.toString(), business.GetNow());
		new HibernateOperation().Save(li);
	}

}
