package com.fwitter.repositories;

import com.fwitter.models.ApplicationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DiscoveryRepository extends JpaRepository<ApplicationUser, Long> {
    // Case-insensitive search for usernames
    List<ApplicationUser> findByUsernameContainingIgnoreCase(String username);

    // Alternatively, you can use a custom query
    @Query("SELECT u FROM ApplicationUser u WHERE LOWER(u.username) LIKE LOWER(CONCAT('%', :username, '%'))")
    List<ApplicationUser> searchByUsernameIgnoreCase(@Param("username") String username);
}