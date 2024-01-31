package com.drproject.entity;


import jakarta.persistence.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "choices")
public class Choice {

    @Id
    @Column(name = "UUID") // database id
    private String id;

    @ManyToOne
    @JoinColumn(name = "mutltipleAnswer")
    MultipleAnswer multipleAnswer;

    @Column(name = "studentChoices")
    @OneToMany(mappedBy = "choice", cascade = CascadeType.ALL)
    List<StudentChoice> studentChoices;


    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }


    @Column(name = "correctness")
    private String correctness;

    @Column(name = "text")
    private String text;

    public MultipleAnswer getMultipleAnswer() {
        return multipleAnswer;
    }

    public void setMultipleAnswer(MultipleAnswer multipleAnswer) {
        this.multipleAnswer = multipleAnswer;
    }

    public List<StudentChoice> getStudentChoices() {
        return studentChoices;
    }

    public void setStudentChoices(List<StudentChoice> studentChoices) {
        this.studentChoices = studentChoices;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getCorrectness() {
        return correctness;
    }

    public void setCorrectness(String correctness) {
        this.correctness = correctness;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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
