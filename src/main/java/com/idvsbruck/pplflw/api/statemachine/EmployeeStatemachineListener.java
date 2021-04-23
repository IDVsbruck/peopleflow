package com.idvsbruck.pplflw.api.statemachine;

import org.springframework.messaging.Message;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListener;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

public class EmployeeStatemachineListener implements StateMachineListener<EmployeeState, EmployeeEvent> {

    @Override
    public void stateChanged(State<EmployeeState, EmployeeEvent> from, State<EmployeeState, EmployeeEvent> to) {
    }

    @Override
    public void stateEntered(State<EmployeeState, EmployeeEvent> state) {
    }

    @Override
    public void stateExited(State<EmployeeState, EmployeeEvent> state) {
    }

    @Override
    public void eventNotAccepted(Message<EmployeeEvent> event) {
    }

    @Override
    public void transition(Transition<EmployeeState, EmployeeEvent> transition) {
    }

    @Override
    public void transitionStarted(Transition<EmployeeState, EmployeeEvent> transition) {
    }

    @Override
    public void transitionEnded(Transition<EmployeeState, EmployeeEvent> transition) {
    }

    @Override
    public void stateMachineStarted(StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
    }

    @Override
    public void stateMachineStopped(StateMachine<EmployeeState, EmployeeEvent> stateMachine) {
    }

    @Override
    public void stateMachineError(StateMachine<EmployeeState, EmployeeEvent> stateMachine, Exception exception) {
    }

    @Override
    public void extendedStateChanged(Object key, Object value) {
    }

    @Override
    public void stateContext(StateContext<EmployeeState, EmployeeEvent> stateContext) {
    }
}
