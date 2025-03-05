package com.filxconnect.service;

import com.filxconnect.entity.User;
import com.filxconnect.repository.UserRepository;
import org.springframework.stereotype.Service;

import com.filxconnect.entity.Follower;
import com.filxconnect.repository.FollowerRepository;

import java.util.List;
import java.util.UUID;

@Service
public class FollowerService {

    private final FollowerRepository followerRepository;
    private final UserRepository userRepository;

    // Constructor for dependency injection
    public FollowerService(FollowerRepository followerRepository, UserRepository userRepository) {
        this.followerRepository = followerRepository;
        this.userRepository = userRepository;
    }

    public long countByFollowerId(UUID followerId) {
        return followerRepository.countByFollowerId(followerId);
    }

    public long countByFollowingId(UUID followingId) {
        return followerRepository.countByFollowingId(followingId);
    }

    // Method to follow a user
    public Follower followUser(UUID followerId, UUID followingId) {
        Follower newFollower = new Follower();
        newFollower.setFollowerId(followerId);
        newFollower.setFollowingId(followingId);
        return followerRepository.save(newFollower);
    }

    // Method to unfollow a user
    public void unfollowUser(UUID followerId, UUID followingId) {
        followerRepository.deleteByFollowerIdAndFollowingId(followerId, followingId);
        System.out.println("User unfollowed");
    }

    // Get the list of users followed by a given user
    public List<User> getFollowedUsers(UUID userId) {
        List<UUID> followedUserIds = followerRepository.findByFollowerId(userId)
                .stream().map(Follower::getFollowingId).toList();
        return userRepository.findAllById(followedUserIds);
    }

    // Get the list of users NOT followed by a given user
    public List<User> getNotFollowedUsers(UUID userId) {
        List<UUID> followedUserIds = followerRepository.findByFollowerId(userId)
                .stream().map(Follower::getFollowingId).toList();
        List<User> allUsers = userRepository.findAll();

        return allUsers.stream()
                .filter(user -> !followedUserIds.contains(user.getId()) && !user.getId().equals(userId))
                .toList();
    }
    // Method to get all followers of a user
    public List<Follower> getFollowersByUser(UUID userId) {
        return followerRepository.findByFollowingId(userId);
    }

    // Method to get all users a user is following
    public List<Follower> getFollowingByUser(UUID userId) {
        return followerRepository.findByFollowerId(userId);
    }
}
