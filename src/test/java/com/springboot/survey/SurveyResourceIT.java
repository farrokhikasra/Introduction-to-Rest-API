package com.springboot.survey;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Base64;

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
    private static String ADD_QUESTIONS_URL = "/survey/Survey1/questions";

    @Test
    void retrieveSurveys_BasicScenario() throws JSONException {
        HttpHeaders httpHeaders = createHttpAuthAndContentType();

        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> responseEntity = template.exchange(SURVEYS_URL, HttpMethod.POST, httpEntity, String.class);
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
        HttpHeaders httpHeaders = createHttpAuthAndContentType();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> responseEntity = template.exchange(SPECIFIC_SURVEY_URL, HttpMethod.GET, httpEntity, String.class);
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
        HttpHeaders httpHeaders = createHttpAuthAndContentType();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> forEntity = template.exchange(SPECIFIC_QUESTION_URL, HttpMethod.GET, httpEntity, String.class);
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
        HttpHeaders httpHeaders = createHttpAuthAndContentType();
        HttpEntity<String> httpEntity = new HttpEntity<>(null, httpHeaders);

        ResponseEntity<String> responseEntity = template.exchange(QUESTIONS_URL, HttpMethod.GET, httpEntity, String.class);
        String expectedResponse = """
                [{"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"},
                {"id":"Question2","description":"Fastest Growing Cloud Platform","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"Google Cloud"},
                {"id":"Question3","description":"Most Popular DevOps Tool","options":["Kubernetes","Docker","Terraform","Azure DevOps"],"correctAnswer":"Kubernetes"}]
                """;
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
        JSONAssert.assertEquals(expectedResponse, responseEntity.getBody(), false);
    }

    @Test
    void addNewSurveyQuestion_basicScenario() {
        String requestBody = """
                {
                  "description":"Your Facourite Language?",
                  "options":[
                  "Java",
                  "Python",
                  "C++",
                  "JavaScript"
                  ],
                  "correctAnswer":"Java"
                }
                """;

        HttpHeaders httpHeaders = createHttpAuthAndContentType();

        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, httpHeaders);
        ResponseEntity<String> responseEntity = template.exchange(ADD_QUESTIONS_URL, HttpMethod.POST, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(locationHeader.contains("/survey/Survey1/questions"));

        ResponseEntity<String> responseEntityDelete = template.exchange(ADD_QUESTIONS_URL, HttpMethod.DELETE, httpEntity, String.class);
        assertTrue(responseEntityDelete.getStatusCode().is2xxSuccessful());
    }

    private HttpHeaders createHttpAuthAndContentType() {
        String encodedUserPassword = performBasicAuthEncoding("kasra", "farrokhi");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Authorization", "Basic " + encodedUserPassword);
        httpHeaders.add("Content-Type", "application/json");
        return httpHeaders;
    }

    private String performBasicAuthEncoding(String username, String password) {
        String combined = username + ":" + password;
        String encodedString = Base64.getEncoder().encodeToString(combined.getBytes());

        return encodedString;
    }


}
