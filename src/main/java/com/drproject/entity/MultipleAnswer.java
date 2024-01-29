package com.drproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import javax.management.QueryEval;

@Entity
@DiscriminatorValue("MultipleAnswer")
public class MultipleAnswer extends Question {

    @Column(name = "text")
    String text;

    @Column(name = "option1")
    String option1;
    @Column(name= "option2")
    String option2;
    @Column(name = "option3")
    String option3;
    @Column(name = "option4")
    String option4;
    @Column(name="correctOption")
    String correctOption;



}
