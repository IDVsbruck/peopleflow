package com.idvsbruck.pplflw.api.services;

import com.idvsbruck.pplflw.api.config.PplflwConfiguration;
import com.idvsbruck.pplflw.api.dto.Employee;
import com.idvsbruck.pplflw.api.exceptions.PplflwEmployeeException;
import com.idvsbruck.pplflw.api.exceptions.PplflwStatemachineException;
import com.idvsbruck.pplflw.api.exceptions.PplflwStatemachineException.Messages;
import com.idvsbruck.pplflw.api.exceptions.PplflwUnexpectedException;
import com.idvsbruck.pplflw.api.models.EmployeeModel;
import com.idvsbruck.pplflw.api.persistence.repository.EmployeeRepository;
import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import lombok.RequiredArgsConstructor;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.persist.StateMachinePersister;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final PplflwConversionService conversionService;
    private final EmployeeRepository employeeRepository;
    private final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory;
    private final StateMachinePersister<EmployeeState, EmployeeEvent, String> employeeStateMachinePersister;

    public Employee updateEmployee(final EmployeeModel model, final String email) {
        var employee = conversionService.convert(model, Employee.class);
        if (employee == null) {
            throw new PplflwUnexpectedException(PplflwUnexpectedException.Messages.CONVERSATION_ERROR);
        }
        employee.setEmail(email);
        var persistemEmployee = employeeRepository.save(employee);
        final var stateMachine = stateMachineFactory.getStateMachine();
        stateMachine.getExtendedState().getVariables().put(PplflwConfiguration.VARIABLE_EMAIL, email);
        stateMachine.sendEvent(EmployeeEvent.CHECK);
        try {
            employeeStateMachinePersister.persist(stateMachine, email);
        } catch (Exception exception) {
            throw new PplflwStatemachineException(Messages.PERSIST_STATE_ERROR, EmployeeState.IN_CHECK, email);
        }
        return persistemEmployee;
    }

    public Employee loadEmployee(final String email) {
        var employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            throw new PplflwEmployeeException(PplflwEmployeeException.Messages.NOT_FOUND, email);
        }
        return employee;
    }

    public EmployeeState getEmployeeState(final String email) {
        final var stateMachine = stateMachineFactory.getStateMachine();
        try {
            employeeStateMachinePersister.restore(stateMachine, email);
            return stateMachine.getState().getId();
        } catch (Exception exception) {
            throw new PplflwStatemachineException(PplflwStatemachineException.Messages.NOT_PERSISTED_STATE, email);
        }
    }

    public void approveEmployee(final String email) {
        final var stateMachine = stateMachineFactory.getStateMachine();
        try {
            employeeStateMachinePersister.restore(stateMachine, email);
        } catch (Exception exception) {
            throw new PplflwStatemachineException(PplflwStatemachineException.Messages.NOT_PERSISTED_STATE, email);
        }
        var employeeState = stateMachine.getState().getId();
        if (employeeState != EmployeeState.IN_CHECK) {
            throw new PplflwStatemachineException(PplflwStatemachineException.Messages.CHECK_ERROR, employeeState);
        }
        stateMachine.sendEvent(EmployeeEvent.APPROVE);
        try {
            employeeStateMachinePersister.persist(stateMachine, email);
        } catch (Exception exception) {
            throw new PplflwStatemachineException(Messages.PERSIST_STATE_ERROR, EmployeeState.APPROVED, email);
        }
    }
}
