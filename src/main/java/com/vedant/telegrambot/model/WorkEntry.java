package com.vedant.telegrambot.model;

public class WorkEntry {

    private String workName;
    private Long amount;

    public WorkEntry(String workName, Long amount) {
        this.workName = workName;
        this.amount = amount;
    }

    public String getWorkName() {
        return workName;
    }

    public Long getAmount() {
        return amount;
    }
}