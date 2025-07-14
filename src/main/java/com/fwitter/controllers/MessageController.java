package com.fwitter.controllers;

import java.util.List;

import com.fwitter.dto.HideMessageRequestDTO;
import com.fwitter.dto.MessageDTO;
import com.fwitter.dto.MessageReactDTO;
import com.fwitter.dto.ReadMessageResponseDTO;
import com.fwitter.exceptions.InvalidMessageException;
import com.fwitter.exceptions.MessageDoesNotExistException;
import com.fwitter.exceptions.UnableToCreateMessageException;
import com.fwitter.models.Message;
import com.fwitter.services.MessageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/message")
public class MessageController{

    private final MessageService messageService;

    @Autowired
    public MessageController(MessageService messageService){
        this.messageService = messageService;
    }

    @PostMapping(value="/", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public MessageDTO sendMessage(@RequestPart("messagePayload") String messagePayload, @RequestPart("image") List<MultipartFile> image){
        Message message = messageService.createMessage(messagePayload, image);
        return new MessageDTO(
                message
        );
    }

    @PostMapping(value="reply", consumes={MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public MessageDTO sendReply(@RequestPart("messagePayload")String messagePayload, @RequestPart("image")List<MultipartFile> image, @RequestPart("replyTo")String replyTo){
        Message message = messageService.createReply(messagePayload, image, replyTo);
        return new MessageDTO(message);
    }

    @GetMapping(value="/read")
    public ReadMessageResponseDTO readMessages(@RequestParam("userId")Integer userId, @RequestParam("conversationId")Integer conversationId){
        return messageService.readMessages(userId, conversationId);
    }

    @PostMapping(value="/react")
    public MessageDTO reactToMessage(@RequestBody MessageReactDTO body){
        System.out.println(body);
        Message message = messageService.reactToMessage(body.getUser(), body.getMessageId(), body.getReaction());
        return new MessageDTO(message);
    }

    @PostMapping(value="/hide")
    public MessageDTO hideMessageForUser(@RequestBody HideMessageRequestDTO body){
        return new MessageDTO(messageService.hideMessageForUser(body.getUser(), body.getMessageId()));
    }

    @ExceptionHandler({UnableToCreateMessageException.class})
    public ResponseEntity<String> handleUnableToCreateMessage(){
        return ResponseEntity.status(500).body("Unable to create a message at this time, please try again");
    }

    @ExceptionHandler({InvalidMessageException.class})
    public ResponseEntity<String> handleInvalidMessage(){
        return ResponseEntity.status(403).body("You attempted to create an invalid message in this conversation");
    }

    @ExceptionHandler({MessageDoesNotExistException.class})
    public ResponseEntity<String> handleMessageDoesNotExistException(){
        return ResponseEntity.status(404).body("Message does not exist");
    }

}
