package com.springboot.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyResourceIT {

    @Autowired
    private TestRestTemplate template;

    private static String SPECIFIC_QUESTION_URL = "/survey/Survey1/questions/question1";
    private static String QUESTIONS_URL = "/survey/Survey1/questions";
    private static String SPECIFIC_SURVEY_URL = "/survey/survey1";
    private static String SURVEYS_URL = "/surveys";

    @Test
    void retrieveSurveys_BasicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(SURVEYS_URL, String.class);
        String expectedResponse = """
                [{"id":"Survey1","title":"My Favorite Survey","description":"Description of the Survey","questions":[
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"},
                {"id":"Question2","description":"Fastest Growing Cloud Platform","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"Google Cloud"},
                {"id":"Question3","description":"Most Popular DevOps Tool","options":["Kubernetes","Docker","Terraform","Azure DevOps"],"correctAnswer":"Kubernetes"}]}] """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }


    @Test
    void retrieveSurveyId_BasicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(SPECIFIC_SURVEY_URL, String.class);
        String expectedResponse = """
                {"id":"Survey1","title":"My Favorite Survey","description":"Description of the Survey",
                "questions":[{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"},
                {"id":"Question2","description":"Fastest Growing Cloud Platform","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"Google Cloud"},
                {"id":"Question3","description":"Most Popular DevOps Tool","options":["Kubernetes","Docker","Terraform","Azure DevOps"],"correctAnswer":"Kubernetes"}]}
                  """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    void retrieveSpecificQuestionByID_BasicScenario() throws JSONException {
        ResponseEntity<String> forEntity = template.getForEntity(SPECIFIC_QUESTION_URL, String.class);
        String expectedResponse = """
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;

        assertTrue(forEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", forEntity.getHeaders().get("Content-Type").get(0));
        //Use trim! Trimming makes the expected response more exact :)
        assertEquals(expectedResponse.trim(), forEntity.getBody());
        //Another kind of testing. Don't forget to throw exceptions! :)
        JSONAssert.assertEquals(expectedResponse, forEntity.getBody(), false);
    }

    @Test
    void retrieveQuestionsBySurveyId_BasicScenario() throws JSONException {
        ResponseEntity<String> responseEntity = template.getForEntity(QUESTIONS_URL, String.class);
        String expectedResponse = """
                [{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"},
                {"id":"Question2","description":"Fastest Growing Cloud Platform","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"Google Cloud"},
                {"id":"Question3","description":"Most Popular DevOps Tool","options":["Kubernetes","Docker","Terraform","Azure DevOps"],"correctAnswer":"Kubernetes"}]
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }
}
