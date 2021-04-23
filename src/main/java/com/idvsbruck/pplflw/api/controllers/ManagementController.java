package com.idvsbruck.pplflw.api.controllers;

import com.idvsbruck.pplflw.api.dto.Employee;
import com.idvsbruck.pplflw.api.services.EmployeeService;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

//TODO: Expected that user(s) with manager role already added in auth server
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/management", produces = MediaType.APPLICATION_JSON_VALUE)
public class ManagementController {

    private final EmployeeService employeeService;

    @GetMapping(path = "/employee/{employee}")
    @PreAuthorize(value = "#oauth2.hasScope('api:manager')")
    @CrossOrigin(methods = {RequestMethod.GET, RequestMethod.OPTIONS})
    public ResponseEntity<Employee> getEmployee(@PathVariable(name = "employee") String email) {
        var employee = employeeService.loadEmployee(email);
        return ResponseEntity.ok(employee);
    }

    @GetMapping(path = "/employee/state/{employee}")
    @PreAuthorize(value = "#oauth2.hasScope('api:manager')")
    @CrossOrigin(methods = {RequestMethod.GET, RequestMethod.OPTIONS})
    public ResponseEntity<EmployeeState> getEmployeeStatus(@PathVariable(name = "employee") String email) {
        return ResponseEntity.ok(employeeService.getEmployeeState(email));
    }

    @PutMapping(path = "/employee/approvement/{employee}")
    @PreAuthorize(value = "#oauth2.hasScope('api:manager')")
    @CrossOrigin(methods = {RequestMethod.PUT, RequestMethod.OPTIONS})
    public ResponseEntity<Void> setEmployeeState(@PathVariable(name = "employee") String email) {
        employeeService.approveEmployee(email);
        return ResponseEntity.noContent().build();
    }
}
