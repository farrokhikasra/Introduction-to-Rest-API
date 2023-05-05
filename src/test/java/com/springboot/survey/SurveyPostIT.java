package com.springboot.survey;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SurveyPostIT {

    @Autowired
    private TestRestTemplate template;

    private static String ADD_QUESTIONS_URL = "/survey/Survey1/questions";

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

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "application/json");
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, httpHeaders);
        ResponseEntity<String> responseEntity = template.exchange(ADD_QUESTIONS_URL, HttpMethod.POST, httpEntity, String.class);
        assertTrue(responseEntity.getStatusCode().is2xxSuccessful());
        String locationHeader = responseEntity.getHeaders().get("Location").get(0);
        assertTrue(locationHeader.contains("/survey/Survey1/questions"));
        template.delete(locationHeader);
    }


}
