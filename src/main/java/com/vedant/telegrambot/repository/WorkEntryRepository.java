package com.vedant.telegrambot.repository;

import com.vedant.telegrambot.entity.WorkEntryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkEntryRepository
        extends JpaRepository<WorkEntryEntity, Long> {
}