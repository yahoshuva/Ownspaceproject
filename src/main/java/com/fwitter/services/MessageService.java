package com.fwitter.services;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fwitter.dto.CreateMessageDTO;
import com.fwitter.dto.MessageDTO;
import com.fwitter.dto.ReadMessageResponseDTO;
import com.fwitter.exceptions.InvalidMessageException;
import com.fwitter.exceptions.MessageDoesNotExistException;
import com.fwitter.exceptions.UnableToCreateMessageException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Conversation;
import com.fwitter.models.Image;
import com.fwitter.models.Message;
import com.fwitter.models.MessageReaction;
import com.fwitter.models.MessageType;
import com.fwitter.models.Notification;
import com.fwitter.repositories.MessageReactionRepository;
import com.fwitter.repositories.MessageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
public class MessageService {

    private final MessageRepository messageRepository;
    private final MessageReactionRepository messageReactionRepository;
    private final ImageService imageService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final ConversationService conversationService;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageReactionRepository messageReactionRepository,
                          ImageService imageService, NotificationService notificationService, UserService userService,
                          ConversationService conversationService) {
        this.messageRepository = messageRepository;
        this.messageReactionRepository = messageReactionRepository;
        this.imageService = imageService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.conversationService = conversationService;
    }

    public Message createMessage(String messagePayload, List<MultipartFile> files) {
        Message message;
        String messageImage = "";

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            CreateMessageDTO dto = objectMapper.readValue(messagePayload, CreateMessageDTO.class);

            boolean isUserInConversation = dto.getConversation().getConversationUsers().stream()
                    .map(ApplicationUser::getUserId)
                    .collect(Collectors.toList())
                    .contains(dto.getSentBy().getUserId());

            if (!isUserInConversation) {
                throw new InvalidMessageException();
            }

            if (dto.getGifUrl() != null) {
                messageImage = imageService.saveGifFromMessage(dto.getGifUrl());
            }

            if (files != null && !files.isEmpty() && !files.get(0).isEmpty()) {
                Image savedImage = imageService.uploadImage(files.get(0), "msg");
                messageImage = savedImage.getImageUrl();
            }

            message = (messageImage.isEmpty())
                    ? new Message(dto.getMessageType(), dto.getSentBy(), dto.getConversation(), dto.getText())
                    : new Message(dto.getMessageType(), dto.getSentBy(), dto.getConversation(), dto.getText(), messageImage);

            message = messageRepository.save(message);

            List<ApplicationUser> notificationList = message.getConversation().getConversationUsers()
                    .stream()
                    .filter(user -> !user.getUserId().equals(dto.getSentBy().getUserId()))
                    .collect(Collectors.toList());

            notificationService.createAndSendMessageNotifications(notificationList, message.getSentBy(), message);

            return message;
        } catch (InvalidMessageException e) {
            throw e;
        } catch (Exception e) {
            throw new UnableToCreateMessageException();
        }
    }

    public Message createReply(String messagePayload, List<MultipartFile> files, String replyTo) {
        Integer replyToId = Integer.parseInt(replyTo);
        Message replyToMessage = messageRepository.findById(replyToId)
                .orElseThrow(MessageDoesNotExistException::new);
        Message message = this.createMessage(messagePayload, files);

        message.setReplyTo(replyToMessage);
        message.setMessageType(MessageType.REPLY);

        return messageRepository.save(message);
    }

    public ReadMessageResponseDTO readMessages(Integer userId, Integer conversationId) {
        ApplicationUser user = userService.getUserById(userId);
        Conversation conversation = conversationService.findById(conversationId);

        List<Message> messagesToRead = conversation.getConversationMessage()
                .stream()
                .filter(message -> !message.getSeenBy().contains(user) &&
                        !message.getSentBy().getUserId().equals(userId))
                .map(message -> {
                    message.getSeenBy().add(user);
                    return message;
                })
                .collect(Collectors.toList());

        List<Notification> notifications = notificationService.readMessageNotifications(messagesToRead, user);

        messagesToRead = messageRepository.saveAll(messagesToRead);
        List<MessageDTO> readMessagesDTO = messagesToRead.stream()
                .map(MessageDTO::new)
                .collect(Collectors.toList());

        conversation = conversationService.findById(conversationId);

        return new ReadMessageResponseDTO(readMessagesDTO, conversation, notifications);
    }

    public Message reactToMessage(ApplicationUser user, Integer messageId, String reaction) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(MessageDoesNotExistException::new);

        Set<MessageReaction> reactions = message.getReactions();
        Optional<MessageReaction> currentReaction = reactions.stream()
                .filter(r -> r.getReactionUser().getUserId().equals(user.getUserId()))
                .findFirst();

        if (currentReaction.isPresent()) {
            MessageReaction currentMessageReaction = currentReaction.get();
            reactions.remove(currentMessageReaction);
            messageReactionRepository.delete(currentMessageReaction);

            if (!currentMessageReaction.getReaction().equals(reaction)) {
                MessageReaction newReaction = messageReactionRepository.save(new MessageReaction(user, reaction));
                reactions.add(newReaction);
            }
        } else {
            MessageReaction newReaction = messageReactionRepository.save(new MessageReaction(user, reaction));
            reactions.add(newReaction);
        }

        message.setReactions(reactions);
        return messageRepository.save(message);
    }

    public Message hideMessageForUser(ApplicationUser user, Integer messageId) {
        Message message = messageRepository.findById(messageId)
                .orElseThrow(MessageDoesNotExistException::new);

        if (message.getHiddenBy().add(user)) {
            return messageRepository.save(message);
        }
        return message;
    }
}