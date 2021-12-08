package com.springMVC.lab3.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Quiz {
    @Id
    private int id;
    private String subject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
