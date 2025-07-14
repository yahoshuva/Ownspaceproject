package com.fwitter.services;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import com.fwitter.exceptions.ConversationDoesNotExistException;
import com.fwitter.models.ApplicationUser;
import com.fwitter.models.Conversation;
import com.fwitter.models.Message;
import com.fwitter.repositories.ConversationRepository;
import com.fwitter.utils.ConversationComparator;
import com.fwitter.utils.MessageComparator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ConversationService{

    private final ConversationRepository conversationRepository;
    private final UserService userService;

    @Autowired
    public ConversationService(ConversationRepository conversationRepository, UserService userService){
        this.conversationRepository = conversationRepository;
        this.userService = userService;
    }

    public Conversation findById(Integer id){
        return conversationRepository.findById(id).orElseThrow(ConversationDoesNotExistException::new);
    }

    public List<Conversation> readAllConversationsWithUser(Integer userId){
        ApplicationUser user = userService.getUserById(userId);
        List<ApplicationUser> userList = List.of(user);
        List<Conversation> allConversations = conversationRepository.findAllByConversationUsersIn(userList);

        ConversationComparator cc = new ConversationComparator();
        allConversations.stream().map(conversation -> {
                    List<Message> conversationMessages = conversation.getConversationMessage();
                    Collections.sort(conversationMessages, new MessageComparator());
                    conversation.setConversationMessage(conversationMessages);
                    return conversation;
                })
                .sorted(cc)
                .collect(Collectors.toList());  // Use collect instead of toList()

        return allConversations;
    }

    public Conversation readOrCreateConversation(List<Integer> conversationUserIds){
        List<ApplicationUser> conversationUsers = userService.getAllUserById(conversationUserIds);
        List<Conversation> conversations = conversationRepository.findAllByConversationUsersIn(conversationUsers);
        Conversation conversation = null;
        for(int i=0; i<conversations.size(); i++){
            if(usersListsAreTheSame(conversations.get(i).getConversationUsers(), conversationUsers)){
                conversation = conversations.get(i);
            }
        }

        if(conversation != null){
            List<Message> conversationMessages = conversation.getConversationMessage();
            Collections.sort(conversationMessages, new MessageComparator());
            conversation.setConversationMessage(conversationMessages);
        }

        if(conversation == null){
            conversation = new Conversation();
            conversation.setConversationUsers(conversationUsers);
            conversation.setConversationMessage(new ArrayList<>());
            conversation = conversationRepository.save(conversation);
        }

        return conversation;
    }

    private boolean usersListsAreTheSame(List<ApplicationUser> list1, List<ApplicationUser> list2){
        return new HashSet<>(list1).equals(new HashSet<>(list2));
    }

}
