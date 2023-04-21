package com.springboot.firstrestapi.survey;

import java.util.List;

public class Survey {
    private String id;
    private String title;
    private String description;
    private List<Question> questions;

    public Survey(String id, String title, String description, List<Question> questions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.questions = questions;
    }

    public Survey() {
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Survey{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", questions=" + questions +
                '}';
    }
}
