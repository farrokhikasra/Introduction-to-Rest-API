package com.springboot.firstrestapi.survey;

import java.util.List;
import java.util.Objects;

public class Question {
    private String id;
    private String description;
    private List<String> options;
    private String correctAnswer;

    public Question(String id, String description, List<String> options, String correctAnswer) {
        super();
        this.id = id;
        this.description = description;
        this.options = options;
        this.correctAnswer = correctAnswer;
    }

    public Question() {

    }

    public String getDescription() {
        return description;
    }

    public List<String> getOptions() {
        return options;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", description='" + description + '\'' +
                ", options=" + options +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
