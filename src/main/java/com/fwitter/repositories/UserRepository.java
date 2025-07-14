package com.fwitter.repositories;

import java.util.List;
import java.util.Optional;

import com.fwitter.dto.CellDTO;
import com.fwitter.models.ApplicationUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<ApplicationUser, Integer>{

	Optional<ApplicationUser> findByUsername(String username);
	Optional<ApplicationUser> findByEmailOrPhoneOrUsername(String email, String phone, String username);
	List<ApplicationUser> findByUsernameLikeIgnoreCase(String username);
	List<ApplicationUser> findByNicknameLikeIgnoreCase(String nickname);
	List<ApplicationUser> findByBioLikeIgnoreCase(String bio);
	// UserRepository.java
	Optional<ApplicationUser> findByCellRowAndCellCol(Integer cellRow, Integer cellCol);

}

