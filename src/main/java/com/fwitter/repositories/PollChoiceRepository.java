package com.fwitter.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fwitter.models.PollChoice;

public interface PollChoiceRepository extends JpaRepository<PollChoice, Integer>{

}
