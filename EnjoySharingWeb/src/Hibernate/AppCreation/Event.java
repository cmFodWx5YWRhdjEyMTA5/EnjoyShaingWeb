package Hibernate.AppCreation;

import java.util.Date;

public class Event implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	
	private Long eventId;
	private String title;
	private Long userId;
	private String content;
	private int maxRequest;
	private Long genderEventId;
	private Date dateEvent;
	private byte activeFlg;
	private Date insertDate;
	private long insertUser;
	private Date updateDate;
	private long updateUser;

	public Event() {
	}

	public Event(String title, String content, int maxRequest, Date dateEvent, byte activeFlg, Date insertDate,
			long insertUser, Date updateDate, long updateUser) {
		this.title = title;
		this.content = content;
		this.maxRequest = maxRequest;
		this.dateEvent = dateEvent;
		this.activeFlg = activeFlg;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.updateDate = updateDate;
		this.updateUser = updateUser;
	}

	public Event(String title, Long userId, String content, int maxRequest, Long genderEventId, Date dateEvent,
			byte activeFlg, Date insertDate, long insertUser, Date updateDate, long updateUser) {
		this.title = title;
		this.userId = userId;
		this.content = content;
		this.maxRequest = maxRequest;
		this.genderEventId = genderEventId;
		this.dateEvent = dateEvent;
		this.activeFlg = activeFlg;
		this.insertDate = insertDate;
		this.insertUser = insertUser;
		this.updateDate = updateDate;
		this.updateUser = updateUser;
	}

	public Long getEventId() {
		return this.eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getMaxRequest() {
		return this.maxRequest;
	}

	public void setMaxRequest(int maxRequest) {
		this.maxRequest = maxRequest;
	}

	public Long getGenderEventId() {
		return this.genderEventId;
	}

	public void setGenderEventId(Long genderEventId) {
		this.genderEventId = genderEventId;
	}

	public Date getDateEvent() {
		return this.dateEvent;
	}

	public void setDateEvent(Date dateEvent) {
		this.dateEvent = dateEvent;
	}

	public byte getActiveFlg() {
		return this.activeFlg;
	}

	public void setActiveFlg(byte activeFlg) {
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
