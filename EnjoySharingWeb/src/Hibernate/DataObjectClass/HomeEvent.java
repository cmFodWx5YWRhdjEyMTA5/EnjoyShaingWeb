package Hibernate.DataObjectClass;

import java.util.Date;

public class HomeEvent {

	public String CardType;
	public java.math.BigInteger EventId;
	public java.math.BigInteger UserId;
	public String UserName;
	public String Title;
	public String Content;
	public java.math.BigInteger AcceptedRequest;
	public java.lang.Integer MaxRequest;
	public java.math.BigInteger GenderEventId;
	public Date DateEvent;
	public boolean RequestSubmitted;
	
	public void setDateEvent(Date dateEvent) {
		DateEvent = dateEvent;
	}
	
	public void setRequestSubmitted(java.math.BigInteger RequestSubmitted) {
		this.RequestSubmitted = RequestSubmitted == java.math.BigInteger.valueOf((long)1);
	}

}
