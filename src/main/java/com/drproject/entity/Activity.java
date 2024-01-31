package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Activity_Type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "activities")
public abstract class Activity {
    @Id
    @Column(name = "UUID")
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


    // The final grade for each activity
    @Column(name = "studentLongAnswers")
    @OneToMany(mappedBy = "activity", cascade = CascadeType.ALL)
    List<StudentLongAnswer> studentLongAnswers;

    public List<StudentLongAnswer> getStudentLongAnswers() {
        return studentLongAnswers;
    }

    public void setStudentLongAnswers(List<StudentLongAnswer> studentLongAnswers) {
        this.studentLongAnswers = studentLongAnswers;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

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
             return new String(Base64.getUrlEncoder().encode(md5Bytes), StandardCharsets.UTF_8);
        }
        catch (Exception e) {
            return null;
        }
    }
}
