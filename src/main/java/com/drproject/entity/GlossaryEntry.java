package com.drproject.entity;

import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;
@Entity
@Table(name = "glossaryEntries")
public class GlossaryEntry {

    @Id
    @Column(name = "tUUID")
    private String id;
    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }


    @Column(name = "glossaryKey")
    String glossaryKey;

    @Column(name = "glossaryValue")
    String glossaryValue;

    @ManyToOne
    @JoinColumn(name = "classroom")
    Classroom classroom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGlossaryKey() {
        return glossaryKey;
    }

    public void setGlossaryKey(String glossaryKey) {
        this.glossaryKey = glossaryKey;
    }

    public String getGlossaryValue() {
        return glossaryValue;
    }

    public void setGlossaryValue(String glossaryValue) {
        this.glossaryValue = glossaryValue;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String toBase64(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(input.getBytes());
            return new String(Base64.getUrlEncoder().encode(md5Bytes), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            return null;
        }
    }

}
