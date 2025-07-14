package com.fwitter.dto;

import com.fwitter.models.ApplicationUser;
import com.fwitter.models.PollChoice;

public class PollVoteDTO {

	private Integer choiceId;
	private Integer userId;
	
	public PollVoteDTO() {
		super();
	}

	public PollVoteDTO(Integer choiceId, Integer userId) {
		super();
		this.choiceId = choiceId;
		this.userId = userId;
	}

	public Integer getChoiceId() {
		return choiceId;
	}

	public void setChoiceId(Integer choiceId) {
		this.choiceId = choiceId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "PollVoteDTO [choiceId=" + choiceId + ", userId=" + userId + "]";
	}
}
