package com.springboot.survey.survey;

import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Service
public class SurveyService {

    private static List<Survey> surveys = new ArrayList<>();

    static {
        Question question1 = new Question("Question1", "Most Popular Cloud Platform Today", Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");
        Question question2 = new Question("Question2", "Fastest Growing Cloud Platform", Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "Google Cloud");
        Question question3 = new Question("Question3", "Most Popular DevOps Tool", Arrays.asList("Kubernetes", "Docker", "Terraform", "Azure DevOps"), "Kubernetes");

        List<Question> questions = new ArrayList<>(Arrays.asList(question1, question2, question3));

        Survey survey = new Survey("Survey1", "My Favorite Survey", "Description of the Survey", questions);

        surveys.add(survey);
    }

    public List<Survey> retrieveAllSurveys() {
        return surveys;
    }

    public Survey retrieveSurveyById(String id) {
        Predicate<? super Survey> predicate = survey -> survey.getId().equalsIgnoreCase(id);
        Optional<Survey> optionalSurvey = surveys.stream().filter(predicate).findFirst();
        System.out.println(optionalSurvey.get());

        if (optionalSurvey.isEmpty()) {
            return null;
        }
        return optionalSurvey.get();
    }

    public List<Question> retrieveSurveyQuestionsById(String id) {
        Survey survey = retrieveSurveyById(id);
        if (survey == null) {
            return null;
        }
        return survey.getQuestions();
    }

    public Question retrieveSpecificQuestionById(String surveyId, String questionId) {
        List<Question> questions = retrieveSurveyQuestionsById(surveyId);
        if (questions == null) {
            return null;
        }
        Predicate<? super Question> predicate = question -> question.getId().equalsIgnoreCase(questionId);
        Optional<Question> optionalQuestion = questions.stream().filter(predicate).findFirst();
        System.out.println(optionalQuestion);
        if (optionalQuestion.isEmpty()) {
            return null;
        }
        return optionalQuestion.get();
    }

    public String addNewSurveyQuestion(String surveyId, Question question) {
        List<Question> questions = retrieveSurveyQuestionsById(surveyId);
        question.setId(generateRandomId());
        questions.add(question);
        return question.getId();
    }

    private static String generateRandomId() {
        SecureRandom secureRandom = new SecureRandom();
        String questionID = new BigInteger(32, secureRandom).toString();
        return questionID;
    }

    public boolean deleteSpecificQuestionById(String surveyId, String questionId) {
        List<Question> questions = retrieveSurveyQuestionsById(surveyId);
        if (questions == null) return false;
        Predicate<? super Question> predicate = question -> question.getId().equalsIgnoreCase(questionId);
        boolean removed = questions.removeIf(predicate);
        if (!removed) return false;
        return true;
    }

    public boolean updateSpecificQuestionById(String surveyId, String questionId, Question question) {
        List<Question> questions = retrieveSurveyQuestionsById(surveyId);
        if (questions == null) return false;
        boolean removed = questions.removeIf(question1 -> question1.getId().equalsIgnoreCase(questionId));
        if (!removed) return false;
        question.setId(questionId);
        questions.add(question);
        return true;
    }
}
