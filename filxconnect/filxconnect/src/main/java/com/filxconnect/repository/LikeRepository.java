package com.filxconnect.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.filxconnect.entity.Like;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LikeRepository extends JpaRepository<Like, UUID> {
    
    // ✅ Corrected Query (UUID instead of byte[])
    @Query("SELECT l FROM Like l WHERE l.userId = :userId AND l.postId = :postId")
    Optional<Like> findByUserIdAndPostId(@Param("userId") UUID userId, @Param("postId") UUID postId);

    boolean existsByUserIdAndPostId(UUID userId, UUID postId);
    List<Like> findByPostId(UUID postId);
    List<Like> findByUserId(UUID userId);
}
