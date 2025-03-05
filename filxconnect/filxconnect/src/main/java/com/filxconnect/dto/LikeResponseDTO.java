package com.filxconnect.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.filxconnect.entity.Like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor // ✅ Ensure all fields are properly initialized
@JsonInclude(JsonInclude.Include.NON_NULL) // ✅ Prevents null values in response
public class LikeResponseDTO {
    private UUID id;
    private UUID postId;
    private UUID userId;
    private String username;
    private LocalDateTime createdAt;

    public UUID getId() { return id; }
    public UUID getPostId() { return postId; }
    public UUID getUserId() { return userId; }
    public String getUsername() { return username; }
    public LocalDateTime getCreatedAt() { return createdAt; }


    // ✅ Constructor
    public LikeResponseDTO(UUID id, UUID postId, UUID userId, LocalDateTime createdAt, String username) {
        this.id = id;
        this.postId = postId;
        this.userId = userId;
        this.createdAt = createdAt;
        this.username = username;
    }

 // ✅ Constructor to map `Like` entity
 public LikeResponseDTO(Like like, String username) {
     this.id = like.getId();
     this.postId = like.getPostId();
     this.userId = like.getUserId();
     this.username = username;
     this.createdAt = like.getCreatedAt();
 }
 // ✅ Constructor that takes `Like` entity
    public LikeResponseDTO(Like like) {
        this.id = like.getId();
        this.postId = like.getPostId();
        this.userId = like.getUserId();
        this.createdAt = like.getCreatedAt();
    }
}
