package com.drproject.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("longAnswer")
public class LongAnswer extends Question  {
    @Column(name = "text")
    String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
