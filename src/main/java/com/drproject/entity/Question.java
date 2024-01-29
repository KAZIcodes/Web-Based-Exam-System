package com.drproject.entity;

import jakarta.persistence.*;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "Question_Type", discriminatorType = DiscriminatorType.STRING)
@Table(name = "questions")
public abstract class Question {

    @Id
    @Column(name = "UUID")
    private String id;
    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }

    @ManyToOne
    @JoinColumn(name="quiz")
    private Quiz quiz;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Quiz getQuiz() {
        return quiz;
    }

    public void setQuiz(Quiz quiz) {
        this.quiz = quiz;
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
