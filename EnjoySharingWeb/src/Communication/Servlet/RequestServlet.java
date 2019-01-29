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
import Hibernate.DataObjectClass.RequestUser;
import Hibernate.Tables.Request;
import Hibernate.Tables.RequestId;
import WebProject.DataObject.ParameterCollection;

@WebServlet("/RequestServlet")
public class RequestServlet extends ServletCommunication {
	private static final long serialVersionUID = 1L;
       
    public RequestServlet() {
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
				case "R":  // REQUEST USERS
					params.Add("EventId", GetRequestParameter("EventId"));
					params.Add("UserName", GetRequestParameter("UserName"));
					LoadRequestUsers(params);
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
			params.Add("UserId", GetRequestParameter("UserId"));
			switch(requestType)
			{
				case "NR":  // New Request
					ErrorMessage = "NewRequestError";
					NewRequest(params);
					message = "NewRequestInserted";
					break;
				case "DR":  // Delete Request
					ErrorMessage = "DeleteRequestError";
					DeleteRequest(params);
					message = "RequestDeleted";
					break;
				case "URS":  // Update Request Status
					params.Add("Status", GetRequestParameter("Status"));
					ErrorMessage = "UpdateRequestStatusError";
					UpdateRequestStatus(params);
					message = "RequestStatusUpdated";
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
	protected void LoadRequestUsers(ParameterCollection params)
	{
		List<RequestUser> lstRet = (List<RequestUser>) ExecuteSP("GetRequestList",params,RequestUser.class);
		if(lstRet != null)
		{
			dataTable = new DataTable(lstRet);
		}
		else
			dataTable = null;
	}
	
	protected void NewRequest(ParameterCollection params)
	{
		Long UserId = Long.parseLong(params.Get("UserId").toString());
		int EventId = Integer.parseInt(params.Get("EventId").toString());
		int RequestStatusId = 2;  // Suspended
		byte ActiveFlg = (byte)1;
		Date UpdateDate = business.GetNow();
		RequestId rId = new RequestId(UserId, EventId);
		Request r = new Request(rId, RequestStatusId, ActiveFlg, UpdateDate);
		new HibernateOperation().Save(r);
	}
	
	protected void DeleteRequest(ParameterCollection params)
	{
		ParameterCollection whereParams = new ParameterCollection();
		int EventId = Integer.parseInt(params.Get("EventId").toString());
		Long UserId = Long.parseLong(params.Get("UserId").toString());
		whereParams.Add("eventId", EventId);
		whereParams.Add("userId", UserId);
		new HibernateOperation().DeleteComposite("Request",whereParams);
	}
	
	protected void UpdateRequestStatus(ParameterCollection params)
	{
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		int EventId = Integer.parseInt(params.Get("EventId").toString());
		Long UserId = Long.parseLong(params.Get("UserId").toString());
		int RequestStatusId = Integer.parseInt(params.Get("Status").toString());  // 1 = Accepted, 3 = Refused
		whereParams.Add("eventId", EventId);
		whereParams.Add("userId", UserId);
		updateParams.Add("RequestStatusId", RequestStatusId);
		updateParams.Add("UpdateDate", business.GetNow());
		new HibernateOperation().UpdateComposite("Request",updateParams,whereParams);
	}

}