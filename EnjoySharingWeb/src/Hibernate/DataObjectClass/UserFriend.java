package Hibernate.DataObjectClass;

import java.util.Date;

public class UserFriend {

	public java.math.BigInteger UserId;
	public String UserName;
	public Date LastUpdateProfileImage;
	
	public void setLastUpdateProfileImage(Date lastUpdateProfileImage) {
		LastUpdateProfileImage = lastUpdateProfileImage;
	}

}
