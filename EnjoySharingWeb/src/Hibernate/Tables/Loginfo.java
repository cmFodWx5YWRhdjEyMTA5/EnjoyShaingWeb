package Hibernate.Tables;

import java.util.Date;

public class Loginfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long logInfoId;
	private Long userId;
	private String servletName;
	private String request;
	private String response;
	private Date logDate;

	public Loginfo() {
	}

	public Loginfo(String servletName, String request, String response, Date logDate) {
		this.servletName = servletName;
		this.request = request;
		this.response = response;
		this.logDate = logDate;
	}

	public Loginfo(Long userId, String servletName, String request, String response, Date logDate) {
		this.userId = userId;
		this.servletName = servletName;
		this.request = request;
		this.response = response;
		this.logDate = logDate;
	}

	public Long getLogInfoId() {
		return this.logInfoId;
	}

	public void setLogInfoId(Long logInfoId) {
		this.logInfoId = logInfoId;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getServletName() {
		return this.servletName;
	}

	public void setServletName(String servletName) {
		this.servletName = servletName;
	}

	public String getRequest() {
		return this.request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return this.response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Date getLogDate() {
		return this.logDate;
	}

	public void setLogDate(Date logDate) {
		this.logDate = logDate;
	}

}
