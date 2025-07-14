package com.fwitter.repositories;

import com.fwitter.models.Message;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer>{

}


//package com.fwitter.repositories;
//
//import com.fwitter.models.Message;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//import java.util.List;
//
//@Repository
//public interface MessageRepository extends JpaRepository<Message, Integer> {
//    // Custom query to find messages by conversation ID
//    List<Message> findByConversation_ConversationId(Integer conversationId);
//}
