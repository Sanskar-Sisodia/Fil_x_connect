package com.filxconnect.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.filxconnect.dto.PostDTO;
import com.filxconnect.entity.Media;
import com.filxconnect.entity.MediaType;
import com.filxconnect.entity.Post;
import com.filxconnect.entity.User;
import com.filxconnect.repository.MediaRepository;
import com.filxconnect.repository.PostRepository;
import com.filxconnect.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.UUID;

@Service
public class PostService {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MediaRepository mediaRepository;
    
    @Transactional
    public Post createPost(PostDTO postDTO) {
        logger.info("Creating a new post for user: {}", postDTO.getUserId());

        // Fetch user
        User user = userRepository.findById(postDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + postDTO.getUserId()));

        // Save the post
        Post post = new Post();
        post.setUser(user);
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setStatus('3');
        post.setCaption(postDTO.getCaption());

        Post savedPost = postRepository.save(post);
        postRepository.flush();  // ✅ Ensure post is saved before media is inserted

        logger.info("Post saved successfully with ID: {}", savedPost.getId());

        // ✅ Ensure post ID is available before adding media
        if (postDTO.getMediaUrls() != null && !postDTO.getMediaUrls().isEmpty()) {
            for (String mediaUrl : postDTO.getMediaUrls()) {
                Media media = new Media();
                media.setMediaUrl(mediaUrl);
                media.setPost(savedPost);

                // ✅ Determine media type based on file extension
                media.setMediaType(mediaUrl.endsWith(".mp4") ? MediaType.VIDEO : MediaType.IMAGE);

                mediaRepository.save(media);
                logger.info("Media saved successfully: {}", mediaUrl);
            }
        }

        return savedPost;
    }

    // ✅ Get all posts
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    
    // ✅ Get post by ID
    public Post getPostById(UUID id) {
        return postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
    }

    public long getPostCount(UUID user_id){
        return postRepository.countByUser_Id(user_id);
    }
    
    public long countActivePosts() {
    	return postRepository.countByStatus('1'); // 1 for Active
    }
    
    public List<Post> getPendingPosts(){
    	return postRepository.findByStatus('3'); // 3 for Pending
    }
    
    public List<Post> getApprovedPosts(){
    	return postRepository.findByStatus('1'); // 1 for Approved
    }
    
    public List<Post> getRejectedPosts(){
    	return postRepository.findByStatus('0'); // 0 for Rejected
    }
    

    // ✅ Get all posts by a user
    public List<Post> getPostsByUser(UUID userId) {
        return postRepository.findByUserId(userId);
    }

    // ✅ Update a post
    public Post updatePost(UUID id, PostDTO postDTO) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
        
        post.setTitle(postDTO.getTitle());
        post.setContent(postDTO.getContent());
        post.setCaption(postDTO.getCaption());
        
        return postRepository.save(post);
    }
    
    public char approvePost(UUID id) {
    	Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
        
        post.setStatus('1'); // Approved
        postRepository.save(post);
        return '1';
    }
    
    public char rejectPost(UUID id) {
    	Post post = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post not found with ID: " + id));
        
        post.setStatus('0'); // Rejected - Can not be shown on the feed.
        postRepository.save(post);
        return '0';
    }


    // ✅ Delete a post
    public void deletePost(UUID id) {
        postRepository.deleteById(id);
    }
}
