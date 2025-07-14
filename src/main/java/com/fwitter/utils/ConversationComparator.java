//package com.fwitter.utils;
//
//import java.util.Comparator;
//
//import com.fwitter.models.Conversation;
//
//public class ConversationComparator implements Comparator<Conversation> {
//
//    @Override
//    public int compare(Conversation arg0, Conversation arg1) {
//        // TODO Auto-generated method stub
//        if (arg0.getConversationMessage().size() == 0 && arg1.getConversationMessage().size() == 0)
//            return 0;
//        if (arg0.getConversationMessage().size() != 0 && arg1.getConversationMessage().size() == 0)
//            return 1;
//        if (arg0.getConversationMessage().size() == 0 && arg1.getConversationMessage().size() != 0)
//            return -1;
//
//        Integer lastMessageIn0 = arg0.getConversationMessage().size() -1;
//        Integer lastMessageIn1 = arg1.getConversationMessage().size() - 1;
//
//        return arg0.getConversationMessage().get(lastMessageIn0).getSentAt()
//            .compareTo(arg1.getConversationMessage().get(lastMessageIn1).getSentAt());
//    }
//
//}

package com.fwitter.utils;

import java.util.Comparator;
import java.util.List;

import com.fwitter.models.Conversation;
import com.fwitter.models.Message;

public class ConversationComparator implements Comparator<Conversation> {

    @Override
    public int compare(Conversation c1, Conversation c2) {
        List<Message> messages1 = c1.getConversationMessage() != null ? c1.getConversationMessage() : List.of();
        List<Message> messages2 = c2.getConversationMessage() != null ? c2.getConversationMessage() : List.of();

        int size1 = messages1.size();
        int size2 = messages2.size();

        if (size1 == 0 && size2 == 0) return 0;
        if (size1 != 0 && size2 == 0) return -1; // c1 has messages, c2 does not → c1 comes first
        if (size1 == 0 && size2 != 0) return 1;  // c2 has messages, c1 does not → c2 comes first

        // Compare last messages (most recent timestamp)
        return messages2.get(size2 - 1).getSentAt().compareTo(messages1.get(size1 - 1).getSentAt()); // Sort DESC
    }
}

