package com.example.sportal.repository;

import com.example.sportal.model.entity.ResetPasswordLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordLinkRepository extends JpaRepository<ResetPasswordLink, String> {
    List<ResetPasswordLink> findAll();

    @Modifying
    @Query(value = "DELETE FROM reset_password_links WHERE expires_at < NOW();",
            nativeQuery = true)
    void clearExpiredLinks();
}
