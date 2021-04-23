package com.idvsbruck.pplflw.api.statemachine.actions;

import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class CheckAction implements Action<EmployeeState, EmployeeEvent> {

    @Override
    public void execute(StateContext<EmployeeState, EmployeeEvent> context) {
        //TODO: add some actions, e.g. notification the Manager(s)
    }
}
