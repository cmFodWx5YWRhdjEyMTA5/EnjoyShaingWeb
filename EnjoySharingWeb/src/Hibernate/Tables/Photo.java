package Hibernate.Tables;

import java.util.Date;

public class Photo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long photoId;
	private byte[] photo;
	private Byte activeFlg;
	private Date insertDate;
	private long insertUser;
	private Date updateDate;
	private long updateUser;

	public Photo() {
	}

	public Photo(byte[] photo, long insertUser, long updateUser) {
		this.photo = photo;
		this.insertUser = insertUser;
		this.updateUser = updateUser;
	}

	public Photo(byte[] photo, Byte activeFlg, Date insertDate, long insertUser, Date updateDate, long updateUser) {
		this.photo = photo;
		this.activeFlg = activeFlg;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.updateDate = updateDate;
		this.updateUser = updateUser;
	}

	public Long getPhotoId() {
		return this.photoId;
	}

	public void setPhotoId(Long photoId) {
		this.photoId = photoId;
	}

	public byte[] getPhoto() {
		return this.photo;
	}

	public void setPhoto(byte[] photo) {
		this.photo = photo;
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
