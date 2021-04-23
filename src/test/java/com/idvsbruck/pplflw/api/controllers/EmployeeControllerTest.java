package com.idvsbruck.pplflw.api.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import com.idvsbruck.pplflw.api.PplflwTestConfiguration;
import com.idvsbruck.pplflw.api.dto.Employee;
import com.idvsbruck.pplflw.api.handlers.ResponseExceptionHandler;
import com.idvsbruck.pplflw.api.models.EmployeeModel;
import com.idvsbruck.pplflw.api.persistence.repository.EmployeeRepository;
import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import java.util.List;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.MethodMode;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.Validator;

@SpringBootTest
@WebAppConfiguration
public class EmployeeControllerTest {

    private static final String TEST_EMPLOYEE_URL = "/employee";
    public static final String DUMMY_EMAIL = "valid@test.com";
    public static final String DUMMY_NAME = "Dummy Name";

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private EmployeeController controller;

    @Autowired
    private ResponseExceptionHandler responseExceptionHandler;

    @Autowired
    private MockRestServiceServer mockServer;

    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    @Autowired
    @Qualifier(value = "validatorBean")
    private Validator validator;

    @Autowired
    private EmployeeRepository repository;

    @MockBean
    @SuppressWarnings("unused")
    private StateMachinePersister<EmployeeState, EmployeeEvent, String> stringStateMachinePersister;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockServer.reset();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(responseExceptionHandler)
                .setValidator(validator).addFilter(springSecurityFilterChain).build();
    }

    @Test
    @DirtiesContext(methodMode = MethodMode.BEFORE_METHOD)
    @DisplayName("Case 1 :: Valid Access Token, create new Employee")
    public void givenValidAccessTokenNoCustomerInDB_whenGetProfile_thenSuccessfulResult() throws Exception {
        //arrange
        var employee = repository.findByEmail(DUMMY_EMAIL);
        assertNull(employee);
        var headers = new HttpHeaders();
        headers.setBearerAuth(PplflwTestConfiguration.getToken(DUMMY_EMAIL, List.of("api:employee")));
        var body = mapper.writeValueAsString(new EmployeeModel(DUMMY_NAME, null));
        //act
        var response = mockMvc.perform(post(TEST_EMPLOYEE_URL).headers(headers)
                .contentType(MediaType.APPLICATION_JSON).content(body)).andReturn().getResponse();
        //assert
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        var employeeResponse = mapper.readValue(response.getContentAsString(), Employee.class);
        assertEquals(DUMMY_EMAIL, employeeResponse.getEmail());
        assertEquals(DUMMY_NAME, employeeResponse.getName());
        assertNull(employeeResponse.getAge());
        assertFalse(employeeResponse.getActive());
        employee = repository.findByEmail(DUMMY_EMAIL);
        assertNotNull(employee);
    }
}
