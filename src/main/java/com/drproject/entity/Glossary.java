package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name="glossary")
public class Glossary {
    // no need to put @Id because field is already defined in parent class
    @Id
    @Column(name = "UUID", columnDefinition = "VARCHAR(36)")
    private String id;

    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }

    @Column(name="title")
    String title;

    @Column(name="description")
    String description;



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
