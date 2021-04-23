package com.idvsbruck.pplflw.api.controllers;

import com.idvsbruck.pplflw.api.dto.Employee;
import com.idvsbruck.pplflw.api.models.EmployeeModel;
import com.idvsbruck.pplflw.api.services.EmployeeService;
import java.security.Principal;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/employee", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping
    @PreAuthorize(value = "#oauth2.hasScope('api:employee')")
    @CrossOrigin(methods = {RequestMethod.POST, RequestMethod.OPTIONS})
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid final EmployeeModel model,
            final Principal principal) {
        var employee = employeeService.updateEmployee(model, principal.getName());
        return ResponseEntity.ok(employee);
    }
}
