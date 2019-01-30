package Hibernate.DataObjectClass;

import java.util.Date;

public class RequestRecieved {

	public java.math.BigInteger EventId;
	public java.math.BigInteger UserId;
	public String UserName;
	public String Title;
	public String Content;
	public java.lang.Integer MaxRequest;
	public java.math.BigInteger GenderEventId;
	public Date DateEvent;
	
	public void setDateEvent(Date dateEvent) {
		DateEvent = dateEvent;
	}

}
