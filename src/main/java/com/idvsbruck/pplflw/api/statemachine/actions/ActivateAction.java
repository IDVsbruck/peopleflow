package com.idvsbruck.pplflw.api.statemachine.actions;

import com.idvsbruck.pplflw.api.config.PplflwConfiguration;
import com.idvsbruck.pplflw.api.persistence.repository.EmployeeRepository;
import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ActivateAction implements Action<EmployeeState, EmployeeEvent> {

    private final EmployeeRepository employeeRepository;

    @Override
    public void execute(StateContext<EmployeeState, EmployeeEvent> context) {
        //TODO: add some actions before activation
        var email = (String) context.getExtendedState().getVariables().get(PplflwConfiguration.VARIABLE_EMAIL);
        var employee = employeeRepository.findByEmail(email);
        employee.setActive(true);
        employeeRepository.save(employee);
    }
}
