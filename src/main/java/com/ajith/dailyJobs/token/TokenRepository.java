package com.ajith.dailyJobs.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("""
            SELECT t FROM Token t 
            WHERE t.user.id = :userId AND (t.expired = false OR t.revoked = false)
            """)
    List<Token> findAllValidTokensByUser(Long userId);

    Optional <Token> findByToken(String token);
}
