package com.drproject.entity;

import jakarta.persistence.*;

import java.util.List;

@Entity
@DiscriminatorValue("longAnswer")
public class LongAnswer extends Question  {
    @Column(name = "text")
    String text;

    @Column(name = "studentLongAnswer")
    @OneToMany(mappedBy = "longAnswer", cascade = CascadeType.ALL)
    List<StudentLongAnswer> studentLongAnswers;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<StudentLongAnswer> getStudentLongAnswers() {
        return studentLongAnswers;
    }

    public void setStudentLongAnswers(List<StudentLongAnswer> studentLongAnswers) {
        this.studentLongAnswers = studentLongAnswers;
    }
}
