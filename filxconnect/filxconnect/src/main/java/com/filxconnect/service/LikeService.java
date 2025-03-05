package com.filxconnect.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.filxconnect.dto.ApiResponse;
import com.filxconnect.dto.LikeResponseDTO;
import com.filxconnect.entity.Like;
import com.filxconnect.entity.Post;
import com.filxconnect.entity.User;
import com.filxconnect.repository.LikeRepository;
import com.filxconnect.repository.PostRepository;
import com.filxconnect.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(LikeService.class);


    public LikeService(LikeRepository likeRepository, PostRepository postRepository, UserRepository userRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // ✅ Like a Post
    public ResponseEntity<?> likePost(UUID postId, UUID userId) {
        if (!postRepository.existsById(postId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(false, "🚨 ERROR: Post not found with ID: " + postId));
        }
        if (!userRepository.existsById(userId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(false, "🚨 ERROR: User not found with ID: " + userId));
        }

        // ✅ Check if user already liked the post
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        if (existingLike.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    new ApiResponse(false, "🚨 ERROR: User already liked this post"));
        }

        // ✅ Create and save like
        Like like = new Like(postId, userId);
        like = likeRepository.save(like);

        return ResponseEntity.ok(new ApiResponse(true, "✅ Success: Post liked!", new LikeResponseDTO(like)));
    }

    // ✅ Unlike a Post
    public ResponseEntity<?> unlikePost(UUID postId, UUID userId) {
        Optional<Like> existingLike = likeRepository.findByUserIdAndPostId(userId, postId);
        if (existingLike.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ApiResponse(false, "🚨 ERROR: User has not liked this post"));
        }

        likeRepository.delete(existingLike.get());
        return ResponseEntity.ok(new ApiResponse(true, "✅ Success: Post unliked!"));
    }

    public List<LikeResponseDTO> getLikesByPost(UUID postId) {
        List<LikeResponseDTO> likes = likeRepository.findByPostId(postId).stream()
                .map(like -> {
                    String username = userRepository.findById(like.getUserId())
                            .map(User::getUsername)
                            .orElse("Unknown User");
                    return new LikeResponseDTO(like, username);
                })
                .collect(Collectors.toList());

        logger.info("Fetched Likes for Post ID {}: {}", postId, likes); // ✅ Log response
        return likes;
    }


    public List<LikeResponseDTO> getLikesByUser(UUID userId) {
        return likeRepository.findByUserId(userId).stream()
                .map(like -> {
                    String username = userRepository.findById(like.getUserId())
                            .map(User::getUsername)
                            .orElse("Unknown User"); // ✅ Ensure valid username
                    return new LikeResponseDTO(like, username);
                })
                .collect(Collectors.toList());
    }

 

}
