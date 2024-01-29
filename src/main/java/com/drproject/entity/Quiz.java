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
    Duration duration;

    public List<Question> getQuestions() {
        return Questions;
    }

    public void setQuestions(List<Question> questions) {
        Questions = questions;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }
}
