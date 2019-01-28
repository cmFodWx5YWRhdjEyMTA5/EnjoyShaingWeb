package Hibernate.Tables;

import java.util.Date;

public class Request implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private RequestId id;
	private int requestStatusId;
	private byte activeFlg;
	private Date updateDate;

	public Request() {
	}

	public Request(RequestId id, int requestStatusId, byte activeFlg, Date updateDate) {
		this.id = id;
		this.requestStatusId = requestStatusId;
		this.activeFlg = activeFlg;
		this.updateDate = updateDate;
	}

	public RequestId getId() {
		return this.id;
	}

	public void setId(RequestId id) {
		this.id = id;
	}

	public int getRequestStatusId() {
		return this.requestStatusId;
	}

	public void setRequestStatusId(int requestStatusId) {
		this.requestStatusId = requestStatusId;
	}

	public byte getActiveFlg() {
		return this.activeFlg;
	}

	public void setActiveFlg(byte activeFlg) {
		this.activeFlg = activeFlg;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

}
