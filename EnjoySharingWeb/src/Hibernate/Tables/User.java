package Hibernate.Tables;

import java.util.Date;

public class User implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long userId;
	private String name;
	private String surname;
	private String userName;
	private String email;
	private String password;
	private String address;
	private Integer cap;
	private Byte activeFlg;
	private Date insertDate;
	private long insertUser;
	private Date updateDate;
	private long updateUser;

	public User() {
	}

	public User(String name, String surname, String email, String password, String address, long insertUser,
			long updateUser) {
		this.name = name;
		this.surname = surname;
		this.email = email;
		this.password = password;
		this.address = address;
		this.insertUser = insertUser;
		this.updateUser = updateUser;
	}

	public User(String name, String surname, String userName, String email, String password, String address,
			Integer cap, Byte activeFlg, Date insertDate, long insertUser, Date updateDate, long updateUser) {
		this.name = name;
		this.surname = surname;
		this.userName = userName;
		this.email = email;
		this.password = password;
		this.address = address;
		this.cap = cap;
		this.activeFlg = activeFlg;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.updateDate = updateDate;
		this.updateUser = updateUser;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCap() {
		return this.cap;
	}

	public void setCap(Integer cap) {
		this.cap = cap;
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
