package Hibernate.DataObjectClass;

import java.util.Date;

public class RequestSent {

	public java.math.BigInteger EventId;
	public java.math.BigInteger UserId;
	public String UserName;
	public String Title;
	public String Content;
	public java.math.BigInteger AcceptedRequest;
	public java.lang.Integer MaxRequest;
	public java.math.BigInteger GenderEventId;
	public Date DateEvent;
	public java.lang.Integer RequestStatusId;
	public String RequestStatus;
	public Date LastUpdateProfileImage;
	
	public void setDateEvent(Date dateEvent) {
		DateEvent = dateEvent;
	}
	
	public void setLastUpdateProfileImage(Date lastUpdateProfileImage) {
		LastUpdateProfileImage = lastUpdateProfileImage;
	}

}
