package Communication.Servlet;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Hibernate.DataObjectClass.DataTable;
import Hibernate.DataObjectClass.HomeEvent;
import WebProject.DataObject.ParameterCollection;

@WebServlet("/EventServlet")
public class EventServlet extends ServletCommunication {
	private static final long serialVersionUID = 1L;
       
    public EventServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try
		{
			super.doGet(request, response);
			String requestType = GetRequestParameter("RequestType");
			ParameterCollection params = new ParameterCollection();
			params.Add("userId", currentUser.getUserId());
			switch(requestType)
			{
				case "H":
					LoadHomeEvent(params);
					break;
				case "M":
					LoadMyEvent(params);
					break;
				default:
					ErrorMessage = "WrongRequest";
					throw new Exception();
			}
			PrepareJSON();
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
	
	@SuppressWarnings("unchecked")
	protected void LoadHomeEvent(ParameterCollection params)
	{
		List<HomeEvent> lstRet = (List<HomeEvent>) ExecuteSP("GetHomeEvents",params,HomeEvent.class);
		if(lstRet != null)
		{
			dataTable = new DataTable(lstRet);
		}
		else
			dataTable = null;
	}
	
	@SuppressWarnings("unchecked")
	protected void LoadMyEvent(ParameterCollection params)
	{
		List<HomeEvent> lstRet = (List<HomeEvent>) ExecuteSP("GetMyEvents",params,HomeEvent.class);
		if(lstRet != null)
		{
			dataTable = new DataTable(lstRet);
		}
		else
			dataTable = null;
	}

}
