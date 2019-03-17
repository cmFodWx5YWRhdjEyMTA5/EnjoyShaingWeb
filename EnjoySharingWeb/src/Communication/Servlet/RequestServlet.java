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
import Hibernate.DataObjectClass.RequestRecieved;
import Hibernate.DataObjectClass.RequestSent;
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
				case "RR":  // REQUEST RECIEVED
					params.Add("UserId", GetRequestParameter("UserId"));
					LoadRequestRecieved(params);
					break;
				case "RS":  // REQUEST SENT
					params.Add("UserId", GetRequestParameter("UserId"));
					LoadRequestSent(params);
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
				case "DR":  // Deactivate Request
					ErrorMessage = "DeactivateRequestError";
					DeactivateRequest(params);
					message = "RequestDeactivated";
					break;
				case "URS":  // Update Request Status
					params.Add("Status", GetRequestParameter("Status"));
					ErrorMessage = "UpdateRequestStatusError";
					UpdateRequestStatus(params);
					message = "RequestStatusUpdated";
					break;
				case "AR":  // Accept All Requests
					params.Add("EventId", GetRequestParameter("EventId"));
					ErrorMessage = "AcceptAllError";
					AcceptRefuseRequests(params,true);
					message = "AllAccepted";
					break;
				case "RR":  // Refuse All Requests
					params.Add("EventId", GetRequestParameter("EventId"));
					ErrorMessage = "RefuseAllError";
					AcceptRefuseRequests(params,false);
					message = "AllRefused";
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
	
	@SuppressWarnings("unchecked")
	protected void LoadRequestRecieved(ParameterCollection params)
	{
		List<RequestRecieved> lstRet = (List<RequestRecieved>) ExecuteSP("GetRequestRecievedList",params,RequestRecieved.class);
		if(lstRet != null)
		{
			dataTable = new DataTable(lstRet);
		}
		else
			dataTable = null;
	}
	
	@SuppressWarnings("unchecked")
	protected void LoadRequestSent(ParameterCollection params)
	{
		List<RequestSent> lstRet = (List<RequestSent>) ExecuteSP("GetRequestSentList",params,RequestSent.class);
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
		int RequestStatusId = Integer.parseInt(businessDB.GetParameter("RequestStatusId_Suspended"));  // Suspended
		byte ActiveFlg = (byte)1;
		Date UpdateDate = business.GetNow();
		// Prima controllo se esiste e la riattivo...
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		whereParams.Add("eventId", EventId);
		whereParams.Add("userId", UserId);
		updateParams.Add("requestStatusId", RequestStatusId);
		updateParams.Add("activeFlg", ActiveFlg);
		updateParams.Add("UpdateDate", business.GetNow());
		if(!new HibernateOperation().UpdateComposite("Request",updateParams,whereParams))
		{
			// Se non esiste la creo ATTIVATA
			RequestId rId = new RequestId(UserId, EventId);
			Request r = new Request(rId, RequestStatusId, ActiveFlg, UpdateDate);
			new HibernateOperation().Save(r);
		}
	}
	
	protected void DeactivateRequest(ParameterCollection params)
	{
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		int EventId = Integer.parseInt(params.Get("EventId").toString());
		Long UserId = Long.parseLong(params.Get("UserId").toString());
		byte ActiveFlg = (byte)0;
		whereParams.Add("eventId", EventId);
		whereParams.Add("userId", UserId);
		updateParams.Add("activeFlg", ActiveFlg);
		updateParams.Add("UpdateDate", business.GetNow());
		new HibernateOperation().UpdateComposite("Request",updateParams,whereParams);
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
	
	protected void AcceptRefuseRequests(ParameterCollection params, boolean state)
	{
		ParameterCollection whereParams = new ParameterCollection();
		ParameterCollection whereParamsTable = new ParameterCollection();
		ParameterCollection updateParams = new ParameterCollection();
		int EventId = Integer.parseInt(params.Get("EventId").toString());
		int RequestStatusId_Suspended = Integer.parseInt(businessDB.GetParameter("RequestStatusId_Suspended"));  // Suspended
		int RequestStatusId_Accepted = Integer.parseInt(businessDB.GetParameter("RequestStatusId_Accepted"));  // Accepted
		int RequestStatusId_Refused = Integer.parseInt(businessDB.GetParameter("RequestStatusId_Refused"));  // Refused
		int RequestStatusId = state?RequestStatusId_Accepted:RequestStatusId_Refused;  // 1 = Accepted, 3 = Refused
		whereParams.Add("eventId", EventId);
		whereParamsTable.Add("requestStatusId", RequestStatusId_Suspended);  // 2 = Suspended
		updateParams.Add("RequestStatusId", RequestStatusId);
		updateParams.Add("UpdateDate", business.GetNow());
		new HibernateOperation().UpdateComposite("Request",updateParams,whereParams,whereParamsTable);
	}

}