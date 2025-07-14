package com.fwitter.controllers;

import java.util.List;

import com.fwitter.dto.FindConversationRequest;
import com.fwitter.exceptions.ConversationDoesNotExistException;
import com.fwitter.models.Conversation;
import com.fwitter.services.ConversationService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/conversation")
public class ConversationController{

    private final ConversationService conversationService;


    @Autowired
    public ConversationController(ConversationService conversationService){
        this.conversationService = conversationService;
    }

    @GetMapping("/")
    List<Conversation> getUsersConversations(@RequestParam(name="userId") Integer userId){
        return conversationService.readAllConversationsWithUser(userId);
    }

    @PostMapping("/")
    public Conversation getOrCreateConversation(@RequestBody FindConversationRequest body){
        return conversationService.readOrCreateConversation(body.getUserIds());
    }

    @ExceptionHandler({ConversationDoesNotExistException.class})
    public ResponseEntity<String> handleConversationDoesNotExistException(){
        return new ResponseEntity<String>("Conversation not found", HttpStatus.NOT_FOUND);
    }
}
