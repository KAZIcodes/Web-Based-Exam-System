package com.drproject.entity;

import jakarta.persistence.*;

import javax.management.QueryEval;
import java.util.List;

@Entity
@DiscriminatorValue("multipleAnswer")
public class MultipleAnswer extends Question {

    @Column(name = "text")
    String text;


    @Column(name = "choiceList")
    @OneToMany(mappedBy = "multipleAnswer", cascade = CascadeType.ALL)
    List<Choice> choiceList;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<Choice> getChoiceList() {
        return choiceList;
    }

    public void setChoiceList(List<Choice> choiceList) {
        this.choiceList = choiceList;
    }
}
