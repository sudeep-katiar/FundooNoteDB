package com.bridgelabz.fundoonotes.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "note")
public class NoteModel {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String title;

	private String content;

	@Column(columnDefinition = "boolean default false")
	private boolean isPinned;

	@Column(columnDefinition = "boolean default false")
	private boolean isArchived;

	@Column(columnDefinition = "boolean default false")
	private boolean isDeleted;

	private Date createdAt;

	private Date updatedAt;

	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel createdBy;

	@Column(columnDefinition = "varchar(100) default '#ffffff'")
	private String NoteColor;

	private String reminder;

	@Column(columnDefinition = "boolean default false")
	private boolean reminderStatus;
	
	@ManyToMany
	@JsonIgnore
	private List<LabelModel> labels;

	public List<LabelModel> getLabels() {
		return labels;
	}

	public void setLabels(List<LabelModel> labels) {
		this.labels = labels;
	}

	public NoteModel() {

	}

	public NoteModel(String title, String content) {
		super();
		this.title = title;
		this.content = content;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public UserModel getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserModel createdBy) {
		this.createdBy = createdBy;
	}

	public String getNoteColor() {
		return NoteColor;
	}

	public void setNoteColor(String noteColor) {
		NoteColor = noteColor;
	}

	public String getReminder() {
		return reminder;
	}

	public void setReminder(String time) {
		this.reminder = time;
	}

	public boolean getReminderStatus() {
		return reminderStatus;
	}

	public void setReminderStatus(boolean reminderStatus) {
		this.reminderStatus = reminderStatus;
	}
	
	public NoteModel(String title, String content, boolean isPinned, boolean isArchived, boolean isDeleted,
			String noteColor, String reminder, boolean reminderStatus) {
		super();
		this.title = title;
		this.content = content;
		this.isPinned = isPinned;
		this.isArchived = isArchived;
		this.isDeleted = isDeleted;
		NoteColor = noteColor;
		this.reminder = reminder;
		this.reminderStatus = reminderStatus;
	}

	public NoteModel(long id, String title, String content, boolean isPinned, boolean isArchived, boolean isDeleted,
			Date createdAt, Date updatedAt, UserModel createdBy, String noteColor, String reminder,
			boolean reminderStatus) {
		super();
		this.id = id;
		this.title = title;
		this.content = content;
		this.isPinned = isPinned;
		this.isArchived = isArchived;
		this.isDeleted = isDeleted;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
		this.createdBy = createdBy;
		NoteColor = noteColor;
		this.reminder = reminder;
		this.reminderStatus = reminderStatus;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public void setPinned(boolean isPinned) {
		this.isPinned = isPinned;
	}

	public boolean isArchived() {
		return isArchived;
	}

	public void setArchived(boolean isArchived) {
		this.isArchived = isArchived;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public void setCreatedAt() {
		setUpdatedAt();
		this.createdAt = new Date();
	}


	public void setUpdatedAt() {
		this.updatedAt = new Date();
	}

}
