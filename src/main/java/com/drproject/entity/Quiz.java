package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.query.sqm.mutation.internal.cte.CteInsertStrategy;

import java.time.Duration;
import java.util.List;


@Entity
@DiscriminatorValue("Quiz")
public class Quiz extends Activity{

    @OneToMany(mappedBy = "quiz", cascade = CascadeType.ALL)
    private List<Question> Questions;

    @Column(name = "duration")
    String duration;

    public List<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<Question> questions) {
        Questions = questions;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
