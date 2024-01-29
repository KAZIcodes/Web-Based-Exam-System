package com.drproject.entity;


import jakarta.persistence.*;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "studentChoices")
public class StudentChoice {

    @Id
    @Column(name = "UUID")
    private String id;
    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }


    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name= "choice")
    private Choice choice;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Choice getChoice() {
        return choice;
    }

    public void setChoice(Choice choice) {
        this.choice = choice;
    }

    public String toBase64(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(md5Bytes);
        }
        catch (Exception e) {
            return null;
        }
    }

}
