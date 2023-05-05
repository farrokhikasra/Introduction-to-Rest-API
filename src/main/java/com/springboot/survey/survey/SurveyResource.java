package com.springboot.survey.survey;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class SurveyResource {
    private SurveyService surveyService;

    public SurveyResource(SurveyService surveyService) {
        super();
        this.surveyService = surveyService;
    }

    @RequestMapping("/surveys")
    public List<Survey> retrieveAllSurveys() {
        return surveyService.retrieveAllSurveys();
    }

    @RequestMapping("/survey/{id}")
    public Survey retrieveSurveyById(@PathVariable String id) {
        Survey survey = surveyService.retrieveSurveyById(id);
        System.out.println(survey);
        if (survey == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return survey;
    }

    @RequestMapping("/survey/{id}/questions")
    public List<Question> retrieveQuestionsBySurveyId(@PathVariable String id) {
        List<Question> questions = surveyService.retrieveSurveyQuestionsById(id);
        System.out.println(questions);
        if (questions == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return questions;
    }

    @RequestMapping("/survey/{surveyId}/questions/{questionId}")
    public Question retrieveSpecificQuestionByID(@PathVariable String surveyId, @PathVariable String questionId) {
        Question question = surveyService.retrieveSpecificQuestionById(surveyId, questionId);
        if (question == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return question;
    }

    @RequestMapping(value = "/survey/{surveyId}/questions", method = RequestMethod.POST)
    public ResponseEntity<Object> addNewSurveyQuestion(@PathVariable String surveyId, @RequestBody Question question) {
        String questionId = surveyService.addNewSurveyQuestion(surveyId, question);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{questionId}").buildAndExpand(questionId).toUri();
//        URI uri = ServletUriComponentsBuilder.fromPath("/survey/" + surveyId + "/questions/" + questionId).build().toUri();
        return ResponseEntity.created(uri).build();
    }

    @RequestMapping(value = "/survey/{surveyId}/questions/{questionId}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> deleteSpecificSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
        surveyService.deleteSpecificQuestionById(surveyId, questionId);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/survey/{surveyId}/questions/{questionId}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateSpecificSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId, @RequestBody Question question) {
        surveyService.updateSpecificQuestionById(surveyId, questionId, question);
        return ResponseEntity.noContent().build();
    }
}
