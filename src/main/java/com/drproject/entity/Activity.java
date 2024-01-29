package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name="actvities")
public abstract class Activity {
    @Id
    @Column(name = "UUID", columnDefinition = "VARCHAR(36)")
    private String id;

    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }

    @ManyToOne
    @JoinColumn(name = "section")
    Section section;

    @Column(name = "startDate")
    String startDate;

    @Column(name="endDate")
    String endDate;

    @Column(name="title")
    String title;

    @Column(name="description")
    String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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
