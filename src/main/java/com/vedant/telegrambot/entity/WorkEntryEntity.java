package com.vedant.telegrambot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "work_entry")
public class WorkEntryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_name")
    private String workName;

    private Long amount;

    public WorkEntryEntity() {
    }

    public WorkEntryEntity(String workName, Long amount) {
        this.workName = workName;
        this.amount = amount;
    }

    public Long getId() {
        return id;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}