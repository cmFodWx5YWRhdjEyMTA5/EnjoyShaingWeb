package Hibernate.Tables;

import java.util.Date;

public class Parameter implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private String parameterCode;
	private String parameterValue;
	private String description;
	private Byte activeFlg;
	private Date insertDate;
	private long insertUser;
	private Date updateDate;
	private long updateUser;

	public Parameter() {
	}

	public Parameter(String parameterCode, String parameterValue, long insertUser, long updateUser) {
		this.parameterCode = parameterCode;
		this.parameterValue = parameterValue;
		this.insertUser = insertUser;
		this.updateUser = updateUser;
	}

	public Parameter(String parameterCode, String parameterValue, String description, Byte activeFlg, Date insertDate,
			long insertUser, Date updateDate, long updateUser) {
		this.parameterCode = parameterCode;
		this.parameterValue = parameterValue;
		this.description = description;
		this.activeFlg = activeFlg;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.updateDate = updateDate;
		this.updateUser = updateUser;
	}

	public String getParameterCode() {
		return this.parameterCode;
	}

	public void setParameterCode(String parameterCode) {
		this.parameterCode = parameterCode;
	}

	public String getParameterValue() {
		return this.parameterValue;
	}

	public void setParameterValue(String parameterValue) {
		this.parameterValue = parameterValue;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Byte getActiveFlg() {
		return this.activeFlg;
	}

	public void setActiveFlg(Byte activeFlg) {
		this.activeFlg = activeFlg;
	}

	public Date getInsertDate() {
		return this.insertDate;
	}

	public void setInsertDate(Date insertDate) {
		this.insertDate = insertDate;
	}

	public long getInsertUser() {
		return this.insertUser;
	}

	public void setInsertUser(long insertUser) {
		this.insertUser = insertUser;
	}

	public Date getUpdateDate() {
		return this.updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public long getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(long updateUser) {
		this.updateUser = updateUser;
	}

}
