package com.fwitter.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fwitter.models.Poll;
import com.fwitter.models.PollChoice;

public interface PollRepository extends JpaRepository<Poll, Integer>{
}
