package com.bridgelabz.fundoonotes.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfilePic {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	private String profilePicName;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name = "userId")
	private UserModel userLabel;

	public ProfilePic(String profilePicName, UserModel user) {
		super();
		this.profilePicName = profilePicName;
		this.userLabel = user;
	}

}
