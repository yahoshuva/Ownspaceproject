package com.fwitter.repositories;

import com.fwitter.models.MessageReaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageReactionRepository extends JpaRepository<MessageReaction, Integer>{
}

//
//package com.fwitter.repositories;
//
//import com.fwitter.models.MessageReaction;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface MessageReactionRepository extends JpaRepository<MessageReaction, Integer> {
//}
