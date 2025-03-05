package com.filxconnect.entity;

import jakarta.persistence.*;
import java.util.UUID;

@Entity
public class Post {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;


    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // Ensure correct column mapping
    private User user;

    private String title;
    private String content;
    private String caption;
    private char status;

    // Getters & Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
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

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }
    public char getStatus() {
    	return this.status;
    }
    
    public void setStatus(char status) {
    	this.status = status;
    }
}
