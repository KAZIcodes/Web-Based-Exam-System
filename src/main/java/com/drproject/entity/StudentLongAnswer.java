package com.drproject.entity;


import jakarta.persistence.*;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name = "studentLongAnswers")
public class StudentLongAnswer {

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

    @Column(name = "studentAnswer")
    private String studentAnswer;

    @Column(name = "grade")
    private String grade; // Nomrei ke teacher vared mikone

    @ManyToOne
    @JoinColumn(name= "longAnswer")
    private LongAnswer longAnswer;

    @ManyToOne
    @JoinColumn(name="activity")
    private Activity activity;



    public String getStudentAnswer() {
        return studentAnswer;
    }

    public void setStudentAnswer(String studentAnswer) {
        this.studentAnswer = studentAnswer;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

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

    public LongAnswer getLongAnswer() {
        return longAnswer;
    }

    public void setLongAnswer(LongAnswer longAnswer) {
        this.longAnswer = longAnswer;
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
