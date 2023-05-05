package com.springboot.survey;

import com.springboot.survey.survey.Question;
import com.springboot.survey.survey.SurveyResource;
import com.springboot.survey.survey.SurveyService;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = SurveyResource.class)
public class SurveyResourceTest {
    @MockBean
    private SurveyService surveyService;
    @Autowired
    private MockMvc mockMvc;

    private static String SPECIFIC_QUESTION_URL = "http://localhost:8080/survey/Survey1/questions/question1";
    private static String GENERIC_QUESTION_URL = "http://localhost:8080/survey/Survey1/questions";

    @Test
    void retrieveSpecificQuestion_404Scenario() throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        assertEquals(404, mvcResult.getResponse().getStatus());
        assertEquals("", mvcResult.getResponse().getContentAsString());
    }

    @Test
    void retrieveSpecificQuestion_basicScenario() throws Exception {
        RequestBuilder requestBuilder =
                MockMvcRequestBuilders.get(SPECIFIC_QUESTION_URL).accept(MediaType.APPLICATION_JSON);

        Question question = new Question("Question1", "Most Popular Cloud Platform Today",
                Arrays.asList("AWS", "Azure", "Google Cloud", "Oracle Cloud"), "AWS");

        when(surveyService.retrieveSpecificQuestionById("Survey1", "question1")).thenReturn(question);
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String expectedAnswer = """
                {"id":"Question1","description":"Most Popular Cloud Platform Today","options":["AWS","Azure","Google Cloud","Oracle Cloud"],"correctAnswer":"AWS"}
                """;
        MockHttpServletResponse response = mvcResult.getResponse();
        JSONAssert.assertEquals(expectedAnswer, response.getContentAsString(), false);
        assertEquals(200, response.getStatus());
    }

    @Test
    void addNewSurveyQuestion_basicScenario() throws Exception {
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
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post(GENERIC_QUESTION_URL)
                .accept(MediaType.APPLICATION_JSON).content(requestBody)
                .contentType(MediaType.APPLICATION_JSON);

        when(surveyService.addNewSurveyQuestion(anyString(), any())).thenReturn("newID");

        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(201, response.getStatus());
        String locationHeader = mvcResult.getResponse().getHeaders("Location").toString();
        System.out.println(locationHeader);
        assertTrue(locationHeader.contains("/survey/Survey1/questions/newID"));
    }
}
