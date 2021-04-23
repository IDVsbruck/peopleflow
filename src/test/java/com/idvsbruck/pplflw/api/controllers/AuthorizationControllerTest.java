package com.idvsbruck.pplflw.api.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.idvsbruck.pplflw.api.handlers.ResponseExceptionHandler;
import com.idvsbruck.pplflw.api.models.CredentialsModel;
import com.idvsbruck.pplflw.api.services.AuthorizationService;
import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

@SpringBootTest
@WebAppConfiguration
public class AuthorizationControllerTest {

    private static final String TEST_SIGNUP_URL = "/authorization/signup";
    public static final String DUMMY_EMAIL = "valid@test.com";
    public static final String DUMMY_VALID_PASSWORD = "passw0rd";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private AuthorizationController controller;

    @Autowired
    private ResponseExceptionHandler responseExceptionHandler;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    @Qualifier(value = "validatorBean")
    private Validator validator;

    @MockBean
    @SuppressWarnings("unused")
    private AuthorizationService authorizationService;

    @MockBean
    @SuppressWarnings("unused")
    private StateMachinePersister<EmployeeState, EmployeeEvent, String> stringStateMachinePersister;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockServer.reset();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(responseExceptionHandler)
                .setValidator(validator).build();
    }

    @Test
    @DisplayName("Case 1 :: Registration with valid email and password")
    public void givenValidParameters_whenSignup_thenExpectedNoContentResponse() throws Exception {
        //arrange
        var credentialsModel = new CredentialsModel(DUMMY_EMAIL, DUMMY_VALID_PASSWORD);
        var body = mapper.writeValueAsString(credentialsModel);
        //act & assert
        mockMvc.perform(post(TEST_SIGNUP_URL).contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isNoContent());
    }
}
