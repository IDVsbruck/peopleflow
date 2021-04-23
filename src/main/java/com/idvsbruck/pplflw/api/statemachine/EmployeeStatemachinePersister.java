package com.idvsbruck.pplflw.api.statemachine;

import java.util.HashMap;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.stereotype.Component;

@Component
public class EmployeeStatemachinePersister implements StateMachinePersist<EmployeeState, EmployeeEvent, String> {

    private final HashMap<String, StateMachineContext<EmployeeState, EmployeeEvent>> contexts = new HashMap<>();

    @Override
    public void write(final StateMachineContext<EmployeeState, EmployeeEvent> context, String email) {
        contexts.put(email, context);
    }

    @Override
    public StateMachineContext<EmployeeState, EmployeeEvent> read(final String email) {
        return contexts.get(email);
    }
}
