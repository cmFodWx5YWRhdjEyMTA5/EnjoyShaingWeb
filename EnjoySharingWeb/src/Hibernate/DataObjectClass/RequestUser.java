package Hibernate.DataObjectClass;

import java.util.Date;

public class RequestUser {
	
	public java.math.BigInteger UserId;
	public String UserName;
	public java.math.BigInteger Status;
	public Date LastUpdateProfileImage;
	
	public void setLastUpdateProfileImage(Date lastUpdateProfileImage) {
		LastUpdateProfileImage = lastUpdateProfileImage;
	}

}
