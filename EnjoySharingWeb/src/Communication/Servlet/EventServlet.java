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
import Hibernate.Tables.Event;
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
			switch(requestType)
			{
				case "H":  // HOME
				case "S":  // SEARCH
					params.Add("UserId", currentUser.getUserId());
					params.Add("Title", GetRequestParameter("Title"));
					params.Add("MaxRequest", GetRequestParameter("MaxRequest"));
					params.Add("GenderEventId", GetRequestParameter("GenderEventId"));
					LoadHomeEvent(params);
					break;
				case "M":  // MY EVENTS
					params.Add("UserId", currentUser.getUserId());
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
		try
		{
			super.doPost(request, response);
			String message=null;
			String requestType = GetRequestParameter("RequestType");
			ParameterCollection params = new ParameterCollection();
			params.Add("EventId", GetRequestParameter("EventId"));
			params.Add("Title", GetRequestParameter("Title"));
			params.Add("UserId", currentUser.getUserId());
			params.Add("Content", GetRequestParameter("Content"));
			params.Add("MaxRequest", GetRequestParameter("MaxRequest"));
			params.Add("GenderEventId", GetRequestParameter("GenderEventId"));
			params.Add("DateEvent", GetRequestParameter("DateEvent"));
			switch(requestType)
			{
				case "NE":  // New Event
					ErrorMessage = "NewEventError";
					NewEvent(params);
					message = "NewEventInserted";
					break;
				case "UE":  // Update Event
					ErrorMessage = "UpdateEventError";
					UpdateEvent(params);
					message = "EventUpdated";
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
	
	protected void NewEvent(ParameterCollection params)
	{
		String Title = params.Get("Title").toString();
		Long UserId = (Long) params.Get("UserId");
		String Content = params.Get("Content").toString();
		int MaxRequest = Integer.parseInt(params.Get("MaxRequest").toString());
		Long GenderEventId = Long.parseLong(params.Get("GenderEventId").toString());
		Date DateEvent = business.GetDate(params.Get("DateEvent").toString());
		Event e = new Event(Title, UserId, Content, MaxRequest, GenderEventId, DateEvent,
				(byte) 1, business.GetNow(), currentUser.getUserId(), business.GetNow(), currentUser.getUserId());
		new HibernateOperation().Save(e);
	}
	
	protected void UpdateEvent(ParameterCollection params)
	{
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		Long EventId = Long.parseLong(params.Get("EventId").toString());
		String Title = params.Get("Title").toString();
		String Content = params.Get("Content").toString();
		int MaxRequest = Integer.parseInt(params.Get("MaxRequest").toString());
		Long GenderEventId = Long.parseLong(params.Get("GenderEventId").toString());
		Date DateEvent = business.GetDate(params.Get("DateEvent").toString());
		whereParams.Add("EventId", EventId);
		updateParams.Add("Title", Title);
		updateParams.Add("Content", Content);
		updateParams.Add("MaxRequest", MaxRequest);
		updateParams.Add("GenderEventId", GenderEventId);
		updateParams.Add("DateEvent", DateEvent);
		updateParams.Add("UpdateDate", business.GetNow());
		updateParams.Add("UpdateUser", currentUser.getUserId());
		new HibernateOperation().Update("Event",updateParams,whereParams);
	}

}
