package com.idvsbruck.pplflw.api.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.idvsbruck.pplflw.api.config.annotations.EmailPattern;
import com.idvsbruck.pplflw.api.config.annotations.VerificationCodePattern;
import com.idvsbruck.pplflw.api.models.ActivationModel;
import com.idvsbruck.pplflw.api.models.CredentialsModel;
import com.idvsbruck.pplflw.api.services.AuthorizationService;
import java.util.Map;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/authorization", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS})
    @PostMapping(path = "/signup", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> signup(@RequestBody @Valid final CredentialsModel model)
            throws JsonProcessingException {
        var email = model.getEmail().toLowerCase();
        model.setEmail(email);
        authorizationService.registerCustomer(model);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/confirm")
    @CrossOrigin(methods = {RequestMethod.GET, RequestMethod.OPTIONS})
    public ResponseEntity<Void> confirm(@RequestParam(name = "email") @EmailPattern final String email,
            @RequestParam(name = "code") @VerificationCodePattern final String code) throws JsonProcessingException {
        var model = new ActivationModel(email.toLowerCase(), code);
        authorizationService.confirmRegistration(model);
        return ResponseEntity.ok().build();
    }

    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS})
    @PostMapping(path = "/token", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getToken(@RequestBody @Valid final CredentialsModel model) {
        var response = authorizationService.getAccessToken(model);
        return ResponseEntity.ok(response);
    }
}
