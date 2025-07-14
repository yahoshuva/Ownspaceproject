package com.fwitter.dto;

import java.util.List;
import java.util.Objects;

public class FindConversationRequest{

    private List<Integer> userIds;

    public FindConversationRequest() {
    }

    public FindConversationRequest(List<Integer> userIds) {
        this.userIds = userIds;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIds);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        FindConversationRequest other = (FindConversationRequest) obj;
        return Objects.equals(userIds, other.userIds);
    }

    @Override
    public String toString() {
        return "FindConversationRequest{userIds=" + userIds + "}";
    }     

}
