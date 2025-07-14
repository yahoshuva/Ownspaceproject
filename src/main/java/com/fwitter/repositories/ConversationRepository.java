package com.fwitter.repositories;

import java.util.List;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Conversation;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Integer>{
    List<Conversation> findAllByConversationUsersIn(List<ApplicationUser> users);
}
