package com.filxconnect.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.filxconnect.dto.LikeResponseDTO;
import com.filxconnect.service.LikeService;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    private final LikeService likeService;

    public LikeController(LikeService likeService) {
        this.likeService = likeService;
    }

    // ✅ Like a post (Proper Response Message)
    @PostMapping("/{postId}/{userId}")
    public ResponseEntity<?> likePost(@PathVariable UUID postId, @PathVariable UUID userId) {
        return likeService.likePost(postId, userId);
    }

    // ✅ Get likes for a post
    @GetMapping("/posts/{postId}")
    public ResponseEntity<List<LikeResponseDTO>> getLikesByPost(@PathVariable UUID postId) {
        return ResponseEntity.ok(likeService.getLikesByPost(postId));
    }

    // ✅ Get likes by a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LikeResponseDTO>> getLikesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(likeService.getLikesByUser(userId));
    }

    // ✅ Unlike a post
    @DeleteMapping("/{postId}/{userId}")
    public ResponseEntity<?> unlikePost(@PathVariable UUID postId, @PathVariable UUID userId) {
        return likeService.unlikePost(postId, userId);
    }
}
