package com.idvsbruck.pplflw.api.statemachine.actions;

import com.idvsbruck.pplflw.api.statemachine.EmployeeEvent;
import com.idvsbruck.pplflw.api.statemachine.EmployeeState;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.action.Action;
import org.springframework.stereotype.Component;

@Component
public class ApproveAction implements Action<EmployeeState, EmployeeEvent> {

    @Override
    public void execute(StateContext<EmployeeState, EmployeeEvent> context) {
        //TODO: add some actions on approve, e.g. notification Employee about success approvement
        context.getStateMachine().sendEvent(EmployeeEvent.ACTIVATE);
    }
}
