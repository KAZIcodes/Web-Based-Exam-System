package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name="sections")
public class Section {
    @Id
    @Column(name = "UUID")
    private String id;

    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL)
    private List<Activity> activities;

    @Column(name = "Title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "classroom")
    private Classroom classroom;
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Activity> getActivities() {
        return activities;
    }

    public void setActivities(List<Activity> activities) {
        this.activities = activities;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
