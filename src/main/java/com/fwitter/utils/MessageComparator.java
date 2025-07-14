//package com.fwitter.utils;
//
//import java.util.Comparator;
//
//import com.fwitter.models.Message;
//
//public class MessageComparator implements Comparator<Message>{
//
//    @Override
//    public int compare(Message arg0, Message arg1) {
//        // TODO Auto-generated method stub
//        return arg0.getSentAt().compareTo(arg1.getSentAt());
//    }
//
//}


package com.fwitter.utils;

import java.util.Comparator;

import com.fwitter.models.Message;

public class MessageComparator implements Comparator<Message> {

    @Override
    public int compare(Message m1, Message m2) {
        if (m1.getSentAt() == null && m2.getSentAt() == null) return 0;
        if (m1.getSentAt() == null) return -1; // Treat null as the oldest message
        if (m2.getSentAt() == null) return 1;

        return m1.getSentAt().compareTo(m2.getSentAt()); // Sort ASC (oldest to newest)
    }
}
