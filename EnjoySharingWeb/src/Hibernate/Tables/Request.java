package Hibernate.Tables;

// default package
// Generated 17-gen-2019 21.26.40 by Hibernate Tools 5.2.3.Final

/**
 * Request generated by hbm2java
 */
public class Request implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private RequestId id;
	private int requestStatusId;
	private byte activeFlg;

	public Request() {
	}

	public Request(RequestId id, int requestStatusId, byte activeFlg) {
		this.id = id;
		this.requestStatusId = requestStatusId;
		this.activeFlg = activeFlg;
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

}