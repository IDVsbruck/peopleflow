package com.idvsbruck.pplflw.api.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.idvsbruck.pplflw.api.config.PplflwConfiguration;
import com.idvsbruck.pplflw.api.exceptions.PplflwStatemachineException;
import com.idvsbruck.pplflw.api.exceptions.PplflwStatemachineException.Messages;
import com.idvsbruck.pplflw.api.models.ActivationModel;
import com.idvsbruck.pplflw.api.models.CredentialsModel;
import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    @Value(value = "${security.oauth2.client.create-employee-uri}")
    private String createEmployeeUri;

    @Value(value = "${security.oauth2.client.confirm-employee-uri}")
    private String confirmEmployeeUri;

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper;
    private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
    private final StateMachinePersister<EmployeeState, EmployeeEvent, String> employeeStateMachinePersister;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public void registerCustomer(final CredentialsModel model) throws JsonProcessingException {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var credentialsModel = new CredentialsModel(model.getEmail(),
                "{bcrypt}" + passwordEncoder.encode(model.getPassword()));
        var request = new HttpEntity<>(mapper.writeValueAsString(credentialsModel), headers);
        restTemplate.postForObject(createEmployeeUri, request, Object.class);
        //TODO: Send notification
    }

    public void confirmRegistration(final ActivationModel model) throws JsonProcessingException {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        var request = new HttpEntity<>(mapper.writeValueAsString(model), headers);
        restTemplate.postForObject(confirmEmployeeUri, request, Object.class);
        final var email = model.getEmail();
        final var stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.getExtendedState().getVariables().put(PplflwConfiguration.VARIABLE_EMAIL, email);
        try {
            employeeStateMachinePersister.persist(stateMachine, email);
        } catch (Exception exception) {
            throw new PplflwStatemachineException(Messages.PERSIST_STATE_ERROR, EmployeeState.ADDED, email);
        }
    }

    public Map<String, Object> getAccessToken(final CredentialsModel model) {
        var resourceDetails = ((OAuth2RestTemplate) restTemplate).getResource();
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(resourceDetails.getClientId(), resourceDetails.getClientSecret());
        var form = new LinkedMultiValueMap<String, String>();
        form.add("grant_type", "password");
        form.add("username", model.getEmail());
        form.add("password", model.getPassword());
        var request = new HttpEntity<>(form, headers);
        return restTemplate.exchange(resourceDetails.getAccessTokenUri(), HttpMethod.POST, request,
                new ParameterizedTypeReference<Map<String, Object>>() {}).getBody();
    }
}
