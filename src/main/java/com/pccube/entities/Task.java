package com.pccube.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String description;

	@Column
	private String type;

//	public Task() {
//		
//	}
//	public Task(String description, String type, User user) {
//		super();
//		this.description = description;
//		this.type = type;
//		//this.children = children;
//		this.user = user;
//	}

	@OneToMany
	private List<Task> children = new ArrayList<>();

	// utente a cui viene assegnato il task
	@ManyToOne
	@JoinColumn(name = "username")
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescrizione(String description) {
		this.description = description;
	}
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
